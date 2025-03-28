package org.assansocketserver.domain.stat.service;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.assansocketserver.batch.cdc.entity.SensorRow;
import org.assansocketserver.batch.cdc.repository.SensorDataRepository;
import org.assansocketserver.domain.patient.entity.Patient;
import org.assansocketserver.domain.patient.repository.PatientRepository;
import org.assansocketserver.domain.stat.dto.StatDTO;
import org.assansocketserver.domain.ward.entity.Ward;
import org.assansocketserver.domain.ward.repository.WardRepository;
import org.assansocketserver.domain.ward.repository.WardSpecification;
import org.assansocketserver.domain.watch.repository.WatchLiveRepository;
import org.assansocketserver.domain.watch.repository.WatchRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StatService {
        private final WardRepository wardRepository;
        private final WatchLiveRepository watchLiveRepository;
        private final PatientRepository patientRepository;
        private final SensorDataRepository sensorDataRepository;

        @Value("${disk.root-path}")
        private String PATH;

        public Map<String, Object> getStats(Integer pageNo, String keyword) {
                Pageable pageable = PageRequest.of(pageNo - 1, 10, Sort.by("name").ascending());
                Map<String, Object> diskStat = getDiskStat();

                // Specification을 통해 검색 조건을 적용
                Page<Ward> wards = wardRepository.findAll(WardSpecification.nameContains(keyword), pageable);

                List<StatDTO> statDTOs = wards.stream()
                                .map(ward -> StatDTO.builder()
                                                .accountId(ward.getAccount().getId())
                                                .wardId(ward.getId())
                                                .wardName(ward.getName())
                                                .currentStorage((double) diskStat.get("used_space"))
                                                .totalStorage((double) diskStat.get("total_space"))
                                                .logsValid(true)
                                                .currentWatch(watchLiveRepository.findAllByLive(true).size())
                                                .totalWatch(patientRepository.countByWardAndWatchIsNotNull(ward))
                                                .currentPatient(watchLiveRepository.findAllByLive(true).size())
                                                .totalPatient(patientRepository.countByWard(ward))
                                                .build())
                                .collect(Collectors.toList());

                return Map.of(
                                "total_count", wards.getTotalElements(),
                                "total_pages", wards.getTotalPages(),
                                "current_page", pageNo,
                                "per_page", pageable.getPageSize(),
                                "stats", statDTOs);
        }

        public Map<String, Object> getStats(Ward ward) {
                Map<String, Object> diskStat = getDiskStat();
                List<Patient> patients = patientRepository.findAllByWard(ward);

                List<SensorRow> sensorRowList = patients.stream()
                                .filter(patient -> patient.getWatch() != null)
                                .flatMap(patient -> sensorDataRepository
                                                .findAllByWatchIdAndDateBetween(
                                                                patient.getWatch().getId(),
                                                                LocalDate.now().minusDays(1),
                                                                LocalDate.now().plusDays(1))
                                                .stream()
                                                .flatMap(sensorData -> sensorData.getSensorRowList().stream()))
                                .sorted(Comparator.comparing(row -> LocalDateTime.parse(row.getTimestamp())))
                                .limit(5) // 상위 5개만 가져오기
                                .collect(Collectors.toList());

                StatDTO statDTO = StatDTO.builder()
                                .currentStorage((double) diskStat.get("used_space"))
                                .totalStorage((double) diskStat.get("total_space"))
                                .currentWatch((int) patients.stream().filter(p -> p.getWatch() != null).count())
                                .totalWatch(patientRepository.countByWardAndWatchIsNotNull(ward))
                                .currentPatient((int) patients.stream().filter(p -> p.getWatch() != null).count())
                                .totalPatient(patientRepository.countByWard(ward))
                                .sensorRowList(sensorRowList)
                                .build();

                return Map.of(
                                "current_storage", statDTO.getCurrentStorage(),
                                "total_storage", statDTO.getTotalStorage(),
                                "current_patient", statDTO.getCurrentPatient(),
                                "total_patient", statDTO.getTotalPatient(),
                                "current_watch", statDTO.getCurrentWatch(),
                                "total_watch", statDTO.getTotalWatch(),
                                "stats", statDTO.getSensorRowList());
        }

        private Map<String, Object> getDiskStat() {
                File disk = new File(PATH);
                long totalSpace = disk.getTotalSpace(); // 총 디스크 크기 (바이트)
                long freeSpace = disk.getUsableSpace(); // 사용 가능한 공간 (바이트)
                long usedSpace = totalSpace - freeSpace; // 사용량

                return Map.of(
                                "total_space", bytesToGB(totalSpace),
                                "used_space", bytesToGB(usedSpace));
        }

        private double bytesToGB(long bytes) {
                return bytes / (1024.0 * 1024.0 * 1024.0);
        }
}
