package org.assansocketserver.domain.stat.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.assansocketserver.batch.cdc.entity.SensorRow;
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

                List<SensorRow> sensorRowList = List.of(
                                SensorRow.builder()
                                                .accX(-0.4213795065879822f)
                                                .accY(0.33039984107017517f)
                                                .accZ(9.605537414550781f)
                                                .gyroX(-0.0012217304902151227f)
                                                .gyroY(0.004886921960860491f)
                                                .gyroZ(0.0f)
                                                .barometerValue(1020.915771484375f)
                                                .heartRateValue(80)
                                                .lightValue(316)
                                                .timestamp(LocalDateTime.now().toString())
                                                .build(),
                                SensorRow.builder()
                                                .accX(-0.4213795425232f)
                                                .accY(0.323457017517f)
                                                .accZ(9.781f)
                                                .gyroX(-0.031223151227f)
                                                .gyroY(0.00623860491f)
                                                .gyroZ(3.0f)
                                                .barometerValue(1020f)
                                                .heartRateValue(82)
                                                .lightValue(316)
                                                .timestamp(LocalDateTime.now().plusSeconds(1).toString())
                                                .build());

                StatDTO statDTO = StatDTO.builder()
                                .currentStorage((double) diskStat.get("used_space"))
                                .totalStorage((double) diskStat.get("total_space"))
                                .currentWatch(5)
                                .totalWatch(patientRepository.countByWardAndWatchIsNotNull(ward))
                                .currentPatient(5)
                                .totalPatient(patientRepository.countByWard(ward))
                                .sensorRowList(sensorRowList)
                                .build();

                // // 최종 반환 맵 구조를 원하는 형식으로 변경
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
