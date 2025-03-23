package org.asan.auth.controller;

import lombok.RequiredArgsConstructor;
import org.asan.auth.dto.*;
import org.asan.auth.jwt.JWTUtil;
import org.asan.global.common.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final JWTUtil jwtUtil;

        @PostMapping("/user")
        public ResponseEntity<RestResponse<LoginResponse>> loginUser(@RequestBody LoginRequest request) {
                // 1) 인증
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(request.getUsername(),
                                                request.getPassword()));
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

                // 2) JWT 토큰 생성
                String token = jwtUtil.createJwt(
                                userDetails.getUsername(),
                                userDetails.getRole()); // "ROLE_USER" 등

                // 3) 병동명
                String accountName = userDetails.getName();

                // 4) JSON 응답에 담을 DTO 생성
                LoginResponse data = new LoginResponse("Bearer " + token, accountName, null);

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(RestResponse.OK(data));
        }

        @PostMapping("/admin")
        public ResponseEntity<RestResponse<LoginResponse>> loginAdmin(@RequestBody LoginRequest request) {
                // 1) 인증
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(request.getUsername(),
                                                request.getPassword()));
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

                // 관리자 권한 체크 (ROLE_ADMIN / ROLE_SUPER 등)
                String role = userDetails.getAuthorities().stream()
                                .map(auth -> auth.getAuthority())
                                .filter(r -> r.equals("ROLE_ADMIN") || r.equals("ROLE_SUPER"))
                                .findFirst()
                                .orElseThrow(() -> new AccessDeniedException("관리자 접근 권한이 필요합니다."));

                // 2) JWT 토큰 생성
                String token = jwtUtil.createJwt(
                                userDetails.getUsername(),
                                role);

                // 3) 병원명
                String accountName = userDetails.getName();

                // 4) JSON 응답에 담을 DTO 생성
                LoginResponse data = new LoginResponse("Bearer " + token, accountName, role);

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(RestResponse.OK(data));
        }
}
