package org.assansocketserver.domain.patient.service;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.assansocketserver.domain.patient.dto.PatientSocketDTO;
import org.assansocketserver.domain.patient.entity.Patient;
import org.assansocketserver.domain.patient.repository.PatientRepository;
import org.assansocketserver.domain.sector.repository.SectorRepository;
import org.assansocketserver.domain.sleep.service.SleepService;
import org.assansocketserver.domain.watch.dto.WatchInfoDTO;
import org.assansocketserver.domain.watch.service.WatchService;
import org.assansocketserver.global.common.WebSocketMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PatientSocketService {

    private final WatchService watchService;
    private final PatientRepository patientRepository;
    private final SectorRepository sectorRepository;
    private final SleepService sleepService;

    @Transactional(readOnly = true)
    public WebSocketMessage<List<PatientSocketDTO>> getPatientList(Boolean isRisk) {
        List<Patient> patients = patientRepository.findAll();

        List<PatientSocketDTO> patientList = patients.stream().map(patient -> {
            Long wardId = patient.getWard().getId();
            Long locationId = null;

            Long watchId = patient.getWatch() != null ? patient.getWatch().getId() : null;
            Integer status = watchService.getWatchStatus(watchId);

            if (patient.getWatch() != null) {
                String currentLocation = patient.getWatch().getCurrentLocation();
                locationId = getCurrentLocationId(wardId, currentLocation);
            }

            return PatientSocketDTO.builder()
                    .id(patient.getId())
                    .name(patient.getName())
                    .number(patient.getNumber())
                    .sectorName(patient.getSector().getName())
                    .currentLocationId(locationId)
                    .watchStatus(status)
                    .watchBattery(100)
                    .watchCharging(false)
                    .riskGroup(patient.isRiskGroup())
                    .activeStatus(getActiveStatus(patient.getId(), isRisk))
                    .build();
        }).collect(Collectors.toList());

        return WebSocketMessage.of("PATIENT_ALL", patientList);
    }

    private Long getCurrentLocationId(Long wardId, String sectorName) {
        return sectorRepository.findByWardIdAndName(wardId, sectorName)
                .map(sector -> sector.getId())
                .orElse(null);
    }

    private Integer getActiveStatus(Long patientId, boolean isRisk) {
        // 1. 위급 환자
        if (isRisk) {
            return 2;
        }

        // 2. 현재 시간 (0~23 기준)
        int hour = LocalTime.now().getHour();
        boolean isNightTime = (hour >= 22 || hour < 6);

        if (isNightTime) {
            // 3. 예측 결과 조회
            List<Map<String, Object>> predictions = sleepService.getSleepPredictions();

            // 4. 해당 환자의 watch_id 확인
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new IllegalArgumentException("환자 ID " + patientId + "를 찾을 수 없습니다."));

            if (patient.getWatch() == null) {
                return 0;
            }
            Long watchId = patient.getWatch().getId();

            // 5. prediction 결과에서 해당 watch_id 찾아서 prediction == 1 인지 확인
            boolean isActive = predictions.stream()
                    .anyMatch(p -> watchId.equals(Long.valueOf(p.get("watch_id").toString()))
                            && Integer.valueOf(p.get("prediction").toString()) == 1);
            return isActive ? 1 : 0;
        }

        // 6. 나머지 시간대
        return 0;
    }

    private WatchInfoDTO getWatchInfo() {
        return null;
    }
}
