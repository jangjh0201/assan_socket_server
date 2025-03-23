package org.asan.domain.watch.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.asan.domain.patient.entity.Patient;
import org.asan.domain.patient.service.PatientService;
import org.asan.domain.watch.dto.WatchDTO;
import org.asan.domain.watch.repository.WatchRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WatchService {

        private final WatchRepository watchRepository;
        private final PatientService patientService;

        // watch to watchDTO
        public Map<String, Object> getWatches(Long patientId) {
                List<WatchDTO> watchDTOs = watchRepository.findByPatientIsNull().stream()
                                .map(watch -> WatchDTO.builder()
                                                .id(watch.getId())
                                                // .uuid(watch.getUuid())
                                                .battery(90)
                                                .charging(false)
                                                .build())
                                .collect(Collectors.toList());

                if (patientId != null) {
                        Patient patient = patientService.getPatient(patientId);
                        watchDTOs.add(
                                        WatchDTO.builder()
                                                        .id(patient.getWatch().getId())
                                                        // .uuid(patient.getWatch().getUuid())
                                                        .battery(100)
                                                        .charging(true)
                                                        .build());
                }
                return Map.of(
                                "total_count", watchDTOs.size(),
                                "watches", watchDTOs);
        }

}
