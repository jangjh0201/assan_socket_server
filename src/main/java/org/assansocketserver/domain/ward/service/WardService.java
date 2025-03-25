package org.assansocketserver.domain.ward.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.assansocketserver.auth.dto.AccountDTO;
import org.assansocketserver.auth.entity.Account;
import org.assansocketserver.auth.repository.AccountRepository;
import org.assansocketserver.domain.risk.service.RiskService;
import org.assansocketserver.domain.ward.dto.WardDTO;
import org.assansocketserver.domain.ward.entity.Ward;
import org.assansocketserver.domain.ward.repository.WardRepository;
import org.assansocketserver.global.exception.UnauthenticatedUserException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WardService {

    private final AccountRepository accountRepository;
    private final WardRepository wardRepository;
    private final RiskService riskService;

    public void create(AccountDTO accountDTO) {
        Ward ward = Ward.builder()
                .name(accountDTO.getWardName())
                .account(accountRepository.findByUsername(accountDTO.getUsername()))
                .build();

        wardRepository.save(ward);

        riskService.create(ward, accountDTO.getRiskList());
    }

    public Ward getWardByAccount(Account account) {
        return wardRepository.findByAccount(account);
    }

    public Map<String, Object> getWards() {
        List<WardDTO> wardDTOs = wardRepository.findAll().stream()
                .map(ward -> WardDTO.builder()
                        .id(ward.getId())
                        .name(ward.getName())
                        .build())
                .collect(Collectors.toList());

        return Map.of(
                "total_count", wardDTOs.size(),
                "wards", wardDTOs);
    }

    /**
     * 현재 로그인한 사용자의 병동 반환
     */
    public Ward getCurrentWard(UserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthenticatedUserException("인증되지 않은 사용자입니다.");
        }
        return wardRepository.findByAccount(accountRepository.findByUsername(userDetails.getUsername()));
    }

    /**
     * 병동 ID로 병동 정보 가져오기
     */
    public Ward getWard(Long wardId) {
        return wardRepository.findById(wardId).orElseThrow(
                () -> new IllegalArgumentException("해당 병동을 찾을 수 없습니다."));
    }

}
