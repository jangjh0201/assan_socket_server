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
import org.assansocketserver.domain.ward.entity.Ward;
import org.assansocketserver.domain.watch.dto.WatchInfoDTO;
import org.assansocketserver.domain.watch.service.WatchService;
import org.assansocketserver.global.common.WebSocketMessage;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional(readOnly = true)
    public WebSocketMessage<List<PatientSocketDTO>> getPatientList(Ward ward) {
        List<Patient> patients = patientRepository.findAllByWard(ward);

        List<PatientSocketDTO> patientList = patients.stream().map(patient -> {
            Long locationId = null;

            Long watchId = patient.getWatch() != null ? patient.getWatch().getId() : null;
            Integer status = watchService.getWatchStatus(watchId);

            if (patient.getWatch() != null) {
                String currentLocation = patient.getWatch().getCurrentLocation();
                locationId = getCurrentLocationId(ward.getId(), currentLocation);
            }

            return PatientSocketDTO.builder()
                    .id(patient.getId())
                    .name(patient.getName())
                    .number(patient.getNumber())
                    .sectorName(patient.getSector().getName())
                    .currentLocationId(locationId)
                    .watchStatus(status)
                    .watchBattery(status == 2 ? 100 : 0)
                    .watchCharging(false)
                    .riskGroup(patient.isRiskGroup())
                    .activeStatus(status == 2 ? getActiveStatus(patient.getId()) : null)
                    .build();
        }).collect(Collectors.toList());

        return WebSocketMessage.of("PATIENT_ALL", patientList);
    }

    private Long getCurrentLocationId(Long wardId, String sectorName) {
        return sectorRepository.findByWardIdAndName(wardId, sectorName)
                .map(sector -> sector.getId())
                .orElse(null);
    }

    private Integer getActiveStatus(Long patientId) {
        // 0 : 활동, 1 : 수면, 2 : 위험
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("환자 ID " + patientId + "를 찾을 수 없습니다."));
        if (patient.getWatch() == null) {
            return 0;
        }
        Long watchId = patient.getWatch().getId();
        // Redis 키: emergency:watch:{watchId}
        String key = "emergency:watch:" + watchId;
        Object value = redisTemplate.opsForValue().get(key);
        if (Boolean.TRUE.equals(value)) {
            return 2; // 위험 상태
        }
        // 위급 상태가 아닐 경우 기존 수면 예측 로직 수행
        int hour = LocalTime.now().getHour();
        boolean isNightTime = (hour >= 17 || hour < 18);
        if (isNightTime) {
            List<Map<String, Object>> predictions = sleepService.getSleepPredictions();
            boolean isActive = predictions.stream()
                    .anyMatch(p -> watchId.equals(Long.valueOf(p.get("watch_id").toString()))
                            && Integer.valueOf(p.get("prediction").toString()) == 1);
            return isActive ? 1 : 0;
        }
        return 0;
    }

    private WatchInfoDTO getWatchInfo() {
        return null;
    }
}
