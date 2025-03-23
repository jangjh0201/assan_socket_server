package org.asan.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asan.auth.dto.AccountDTO;
import org.asan.auth.dto.CustomUserDetails;
import org.asan.auth.service.AccountService;
import org.asan.global.common.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/profile")
@RestController
public class ProfileController {

    private final AccountService accountService;

    @PostMapping("/password")
    public ResponseEntity<RestResponse<Void>> verifyPassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Map<String, String> request) {
        log.info("비밀번호 확인 요청");

        String password = request.get("password");
        boolean isValid = accountService.verifyPassword(userDetails, password);

        if (isValid) {
            return ResponseEntity.status(HttpStatus.OK).body(RestResponse.OK());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RestResponse.UNAUTHORIZED());
        }
    }

    @GetMapping("")
    public ResponseEntity<RestResponse<AccountDTO>> getProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("프로필 조회 요청");   

        AccountDTO profile = accountService.getProfile(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.OK(profile));
    }

    @PatchMapping("/password")
    public ResponseEntity<RestResponse<Void>> updatePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Map<String, String> request) {
        log.info("비밀번호 변경 요청");

        String newPassword = request.get("password");
        accountService.updatePassword(userDetails, newPassword);

        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.OK());
    }
}
