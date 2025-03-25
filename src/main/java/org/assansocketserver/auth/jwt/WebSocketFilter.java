package org.assansocketserver.auth.jwt;

import org.assansocketserver.auth.entity.Account;
import org.assansocketserver.auth.service.AccountService;
import org.assansocketserver.domain.ward.entity.Ward;
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
