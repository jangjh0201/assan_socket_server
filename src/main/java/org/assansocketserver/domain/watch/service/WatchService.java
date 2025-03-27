package org.assansocketserver.domain.watch.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.assansocketserver.domain.patient.entity.Patient;
import org.assansocketserver.domain.patient.repository.PatientRepository;
import org.assansocketserver.domain.watch.dto.WatchDTO;
import org.assansocketserver.domain.watch.repository.WatchLiveRepository;
import org.assansocketserver.domain.watch.repository.WatchRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WatchService {

        private final WatchLiveRepository watchLiveRepository;
        private final WatchRepository watchRepository;
        private final PatientRepository patientRepository;

        // watch to watchDTO
        public Map<String, Object> getWatches(Long patientId) {
                List<WatchDTO> watchDTOs = watchRepository.findByPatientIsNull().stream()
                                .map(watch -> WatchDTO.builder()
                                                .id(watch.getId())
                                                .uuid(watch.getUuid())
                                                .battery(100)
                                                .charging(true)
                                                .build())
                                .collect(Collectors.toList());

                if (patientId != null) {
                        Patient patient = patientRepository.findById(patientId).orElseThrow();
                        watchDTOs.add(
                                        WatchDTO.builder()
                                                        .id(patient.getWatch().getId())
                                                        .uuid(patient.getWatch().getUuid())
                                                        .battery(100)
                                                        .charging(true)
                                                        .build());
                }
                return Map.of(
                                "total_count", watchDTOs.size(),
                                "watches", watchDTOs);
        }

        public Integer getWatchStatus(Long watchId) {
                Integer status = null;

                // 0: 연결 끊김, 1: 네트워크 밖, 2: 연결됨
                if (!watchLiveRepository.existsById(watchId)) {
                        status = 0;
                } else {
                        status = 2;
                }

                return status;
        }

        public Boolean isLive(Long watchId) {
                return watchLiveRepository.existsById(watchId);
        }

}
