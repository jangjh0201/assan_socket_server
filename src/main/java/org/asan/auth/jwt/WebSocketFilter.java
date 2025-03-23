package org.asan.auth.jwt;

import org.asan.auth.entity.Account;
import org.asan.auth.service.AccountService;
import org.asan.domain.ward.entity.Ward;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class WebSocketFilter {

    private final JWTUtil jwtUtil;
    private final AccountService accountService;

    public Ward getWardInfo(String token){
        String username = jwtUtil.getUsername(token);
        Account account = accountService.getAccount(username);
        return account.getWard();
    }
}
