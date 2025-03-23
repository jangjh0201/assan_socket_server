package org.asan.auth.service;

import java.util.List;
import java.util.Map;

import org.asan.domain.risk.dto.RiskDTO;
import org.asan.domain.risk.service.RiskService;
import org.asan.domain.riskgroup.dto.RiskGroupDTO;
import org.asan.domain.riskgroup.service.RiskGroupService;
import org.asan.domain.stat.service.StatService;
import org.asan.domain.ward.entity.Ward;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserFacade {

    private final UserService userService;
    private final StatService statService;
    private final RiskService riskService;
    private final RiskGroupService riskGroupService;

    public Map<String, Object> getUsers(Integer page, String keyword) {
        return userService.getUsers(page, keyword);
    }

    public Map<String, Object> getStats(Integer page, String keyword) {
        return statService.getStats(page, keyword);
    }

    public Map<String, Object> getStats(Ward ward) {
        return statService.getStats(ward);
    }

    public Map<String, Object> getRisksAll(Ward ward) {
        return riskService.getRisksAll(ward);
    }

    public void updateRisk(Ward ward, List<RiskDTO> request) {
        riskService.updateRisk(ward, request);
    }

    public Map<String, Object> getRiskGroups(Ward ward) {
        return riskGroupService.getRiskGroups(ward);
    }

    public RiskGroupDTO createRiskGroups(Ward ward, RiskGroupDTO request) {
        return riskGroupService.createRiskGroups(ward, request);

    }

    public void deleteRiskGroups(Ward ward, RiskGroupDTO request) {
        riskGroupService.deleteRiskGroups(ward, request);
    }
}
