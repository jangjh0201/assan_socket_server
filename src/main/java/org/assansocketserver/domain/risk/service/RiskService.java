package org.assansocketserver.domain.risk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.assansocketserver.domain.risk.dto.RiskDTO;
import org.assansocketserver.domain.risk.dto.RiskTypeDTO;
import org.assansocketserver.domain.risk.entity.Risk;
import org.assansocketserver.domain.risk.entity.RiskType;
import org.assansocketserver.domain.risk.enums.Severity;
import org.assansocketserver.domain.risk.repository.RiskRepository;
import org.assansocketserver.domain.risk.repository.RiskTypeRepository;
import org.assansocketserver.domain.ward.entity.Ward;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RiskService {

        private final RiskRepository riskRepository;
        private final RiskTypeRepository riskTypeRepository;

        public void create(Ward ward, List<RiskTypeDTO> request) {
                riskTypeRepository.findAll().forEach(riskType -> {
                        Risk risk = Risk.builder()
                                        .ward(ward)
                                        .riskType(riskType)
                                        .severity(Severity.LOW) // 기본값
                                        .build();

                        // 요청한 RiskType이 있다면 사용가능하도록 변경
                        if (request.stream()
                                        .anyMatch(riskTypeDTO -> riskTypeDTO.getName().equals(riskType.getName()))) {
                                risk.changeAvailability(true);
                        } else {
                                risk.changeAvailability(false);
                        }

                        riskRepository.save(risk);
                });
        }

        /**
         * Ward에 속한 Risk 약식 목록 조회
         */
        public List<RiskDTO> getRisksInfo(Ward ward) {
                List<Risk> risks = riskRepository.findByWard(ward).stream()
                                .filter(risk -> Boolean.TRUE.equals(risk.getAvailability()))
                                .collect(Collectors.toList());

                List<RiskDTO> riskDTOs = risks.stream()
                                .map(risk -> RiskDTO.builder()
                                                .id(risk.getId())
                                                .name(risk.getRiskType().getName())
                                                .build())
                                .collect(Collectors.toList());

                return riskDTOs;
        }

        /**
         * Ward에 속한 Risk 목록 조회(avaliability True인 것만)
         */
        public Map<String, Object> getRisks(Ward ward) {
                List<Risk> risks = riskRepository.findByWard(ward).stream()
                                .filter(risk -> Boolean.TRUE.equals(risk.getAvailability()))
                                .collect(Collectors.toList());

                List<RiskDTO> riskDTOs = risks.stream()
                                .map(risk -> RiskDTO.builder()
                                                .id(risk.getId())
                                                .name(risk.getRiskType().getName())
                                                .severity(risk.getSeverity())
                                                .build())
                                .collect(Collectors.toList());

                return Map.of(
                                "total_count", riskDTOs.size(),
                                "risks", riskDTOs);
        }

        public Map<String, Object> getRisksAll(Ward ward) {
                List<Risk> risks = riskRepository.findByWard(ward).stream()
                                .collect(Collectors.toList());

                List<RiskDTO> riskDTOs = risks.stream()
                                .map(risk -> RiskDTO.builder()
                                                .id(risk.getId())
                                                .name(risk.getRiskType().getName())
                                                .severity(risk.getSeverity())
                                                .availability(risk.getAvailability())
                                                .build())
                                .collect(Collectors.toList());

                return Map.of(
                                "total_count", riskDTOs.size(),
                                "risks", riskDTOs);
        }

        /**
         * 현재 로그인한 사용자의 Ward에서 사용중인 RiskType 목록 조회
         */
        public Map<String, Object> getRiskTypes() {
                List<RiskTypeDTO> riskTypeDTOs = riskTypeRepository.findAll().stream()
                                .map(riskType -> RiskTypeDTO.builder()
                                                .id(riskType.getId())
                                                .name(riskType.getName())
                                                .build())
                                .collect(Collectors.toList());

                return Map.of(
                                "total_count", riskTypeDTOs.size(),
                                "types", riskTypeDTOs);
        }

        /**
         * 관리자 병동별 risk 사용여부 수정
         */
        public void updateAvailability(Ward ward, List<RiskDTO> request) {
                List<Risk> risks = riskRepository.findByWard(ward);

                for (RiskDTO riskDTO : request) {
                        Long riskId = riskDTO.getId();
                        Boolean availability = riskDTO.getAvailability();

                        // 기존 Risk 엔티티 찾아서 use 업데이트
                        risks.stream()
                                        .filter(risk -> risk.getId().equals(riskId))
                                        .findFirst()
                                        .ifPresent(risk -> {
                                                risk.changeAvailability(availability);
                                        });
                }
                riskRepository.saveAll(risks);
        }

        /**
         * 관리자 병동별 risk 심각도 수정
         */
        public void updateSeverity(Ward ward, List<RiskDTO> request) {
                List<Risk> risks = riskRepository.findByWard(ward);

                for (RiskDTO riskDTO : request) {
                        Long riskId = riskDTO.getId();
                        Severity newSeverity = riskDTO.getSeverity(); // Enum 직접 사용

                        // 기존 Risk 엔티티 찾아서 severity 업데이트
                        risks.stream()
                                        .filter(risk -> risk.getId().equals(riskId))
                                        .findFirst()
                                        .ifPresent(risk -> {
                                                risk.changeSeverity(newSeverity);
                                        });

                }
                riskRepository.saveAll(risks);
        }

        /**
         * 관리자 병동별 risk 사용여부, 심각도 수정
         */
        public void updateRisk(Ward ward, List<RiskDTO> request) {
                List<Risk> risks = riskRepository.findByWard(ward);

                for (RiskDTO riskDTO : request) {
                        Long riskId = riskDTO.getId();
                        Boolean availability = riskDTO.getAvailability();
                        Severity newSeverity = riskDTO.getSeverity(); // Enum 직접 사용

                        // 기존 Risk 엔티티 찾아서 use, severity 업데이트
                        risks.stream()
                                        .filter(risk -> risk.getId().equals(riskId))
                                        .findFirst()
                                        .ifPresent(risk -> {
                                                risk.changeAvailability(availability);
                                                risk.changeSeverity(newSeverity);
                                        });
                }
                riskRepository.saveAll(risks);
        }
}
