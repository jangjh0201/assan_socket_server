package org.assansocketserver.domain.riskgroup.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.assansocketserver.domain.patient.repository.PatientRepository;
import org.assansocketserver.domain.riskgroup.dto.RiskGroupDTO;
import org.assansocketserver.domain.riskgroup.entity.RiskGroup;
import org.assansocketserver.domain.riskgroup.repository.RiskGroupRepository;
import org.assansocketserver.domain.ward.entity.Ward;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RiskGroupService {
        private final RiskGroupRepository riskGroupRepository;
        private final PatientRepository patientRepository;

        /**
         * RiskGroup 목록 조회
         */
        public Map<String, Object> getRiskGroups(Ward ward) {
                List<RiskGroupDTO> riskGroupDTOs = riskGroupRepository.findByWard(ward).stream()
                                .map(riskGroup -> RiskGroupDTO.builder()
                                                .id(riskGroup.getId())
                                                .name(riskGroup.getName())
                                                .build())
                                .collect(Collectors.toList());

                return Map.of(
                                "total_count", riskGroupDTOs.size(),
                                "riskgroups", riskGroupDTOs);
        }

        public RiskGroupDTO createRiskGroups(Ward ward, RiskGroupDTO request) {
                // 동일 병동 내에서 동일 이름의 리스크 그룹이 존재하는지 확인합니다.
                if (riskGroupRepository.existsByNameAndWard(request.getName(), ward)) {
                        throw new EntityExistsException("해당 병동에 이미 동일한 이름의 리스크 그룹이 존재합니다.");
                }

                RiskGroup riskGroup = RiskGroup.builder()
                                .ward(ward)
                                .name(request.getName())
                                .build();

                riskGroupRepository.save(riskGroup);

                return RiskGroupDTO.builder()
                                .id(riskGroup.getId())
                                .name(riskGroup.getName())
                                .build();
        }

        public void deleteRiskGroups(Ward ward, RiskGroupDTO request) {
                RiskGroup riskGroup = riskGroupRepository.findById(request.getId())
                                .orElseThrow(() -> new IllegalArgumentException("해당 RiskGroup이 존재하지 않습니다."));
                patientRepository.findByRiskGroup(riskGroup).forEach(patient -> {
                        patient.removeRiskGroup();
                        patientRepository.save(patient);
                });
                riskGroupRepository.delete(riskGroup);
        }

        public List<RiskGroupDTO> getRiskGroupsInfo(Ward ward) {
                return riskGroupRepository.findByWard(ward).stream()
                                .map(riskGroup -> RiskGroupDTO.builder()
                                                .id(riskGroup.getId())
                                                .name(riskGroup.getName())
                                                .build())
                                .collect(Collectors.toList());
        }
}
