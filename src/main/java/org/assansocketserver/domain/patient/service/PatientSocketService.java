package org.assansocketserver.domain.patient.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.assansocketserver.domain.patient.dto.PatientSocketDTO;
import org.assansocketserver.domain.patient.entity.Patient;
import org.assansocketserver.domain.patient.repository.PatientRepository;
import org.assansocketserver.domain.sector.repository.SectorRepository;
import org.assansocketserver.domain.watch.dto.WatchInfoDTO;
import org.assansocketserver.global.common.WebSocketMessage;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PatientSocketService {

    private final PatientRepository patientRepository;
    private final SectorRepository sectorRepository;

    public WebSocketMessage<List<PatientSocketDTO>> getPatientList() {
        List<Patient> patients = patientRepository.findAll();

        List<PatientSocketDTO> patientList = patients.stream().map(patient -> {
            Long wardId = patient.getWard().getId();

            // 만약 할당된 watch가 없다면 currentLocationId를 null로 설정
            Long locationId = null;
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
                    .watchStatus(2)
                    .watchBattery(100)
                    .watchCharging(false)
                    .riskGroup(patient.isRiskGroup())
                    .activeStatus(0)
                    .build();
        }).collect(Collectors.toList());

        WebSocketMessage<List<PatientSocketDTO>> message = WebSocketMessage.of("PATIENT_ALL", patientList);
        return message;
    }

    private Long getCurrentLocationId(Long wardId, String sectorName) {
        return sectorRepository.findByWardIdAndName(wardId, sectorName)
                .map(sector -> sector.getId())
                .orElse(null);
    }

    private Integer getActiveStatus() {
        return null;
    }

    private WatchInfoDTO getWatchInfo() {
        return null;
    }
}
