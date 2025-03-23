package org.asan.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.asan.auth.dto.UserInfo;
import org.asan.auth.entity.Account;
import org.asan.domain.risk.service.RiskService;
import org.asan.domain.riskgroup.service.RiskGroupService;
import org.asan.domain.ward.entity.Ward;
import org.asan.domain.ward.service.MapService;
import org.asan.domain.ward.service.WardService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

        private final AccountService accountService;
        private final WardService wardService;
        private final RiskService riskService;
        private final RiskGroupService riskGroupService;
        private final MapService mapService;

        public Map<String, Object> getUsers(Integer pageNo, String keyword) {

                Pageable pageable = PageRequest.of(pageNo - 1, 10, Sort.by("name").ascending());

                // 검색 조건에 따른 Account 조회
                Page<Account> accountsPage;
                if (keyword == null || keyword.trim().isEmpty()) {
                        accountsPage = accountService.getAccounts(pageable);
                } else {
                        accountsPage = accountService.getAccountsByName(keyword, pageable);
                }

                List<UserInfo> users = new ArrayList<>();

                for (Account account : accountsPage.getContent()) {
                        Ward ward = wardService.getWardByAccount(account);

                        UserInfo userInfo = UserInfo.builder()
                                        .accountId(account.getId())
                                        .username(account.getUsername())
                                        .wardId(ward.getId())
                                        .wardName(account.getWardName())
                                        .risks(riskService.getRisksInfo(ward))
                                        .riskgroups(riskGroupService.getRiskGroupsInfo(ward))
                                        .map(mapService.getMapInfo(ward))
                                        .build();

                        users.add(userInfo);
                }

                return Map.of(
                                "total_count", accountsPage.getTotalElements(),
                                "total_pages", accountsPage.getTotalPages(),
                                "current_page", pageNo,
                                "per_page", pageable.getPageSize(),
                                "users", users);
        }
}
