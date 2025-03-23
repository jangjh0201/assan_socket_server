package org.asan.auth.controller;

import java.util.List;
import java.util.Map;

import org.asan.auth.dto.AccountDTO;
import org.asan.auth.dto.CustomUserDetails;
import org.asan.auth.service.UserFacade;
import org.asan.auth.service.AccountService;
import org.asan.domain.risk.dto.RiskDTO;
import org.asan.domain.riskgroup.dto.RiskGroupDTO;
import org.asan.domain.ward.service.WardService;
import org.asan.global.common.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

        private final AccountService accountService;
        private final WardService wardService;
        private final UserFacade userFacade;

        @PostMapping("")
        public ResponseEntity<RestResponse<Void>> create(
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        @RequestBody AccountDTO accountDTO) {
                // 현재 인증된 사용자 정보 조회
                // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                // boolean isSuper = auth.getAuthorities().stream()
                // .anyMatch(a -> a.getAuthority().equals("ROLE_SUPER"));

                if (!userDetails.getRole().equals("ROLE_SUPER")) {
                        throw new AccessDeniedException("총괄 관리자만 계정 생성이 가능합니다.");
                } else {
                        accountService.create(accountDTO);
                        return ResponseEntity
                                        .status(HttpStatus.CREATED)
                                        .body(RestResponse.CREATED());
                }
        }

        @GetMapping("")
        public ResponseEntity<RestResponse<Map<String, Object>>> getUsers(
                        @RequestParam(required = false, defaultValue = "", value = "hospital") Long hospitalId,
                        @RequestParam(required = false, defaultValue = "1", value = "page") Integer page,
                        @RequestParam(required = false, defaultValue = "", value = "search") String keyword) {
                Map<String, Object> response = userFacade.getUsers(page, keyword);

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(RestResponse.OK(response));
        }

        @GetMapping("/stats")
        public ResponseEntity<RestResponse<Map<String, Object>>> getStats(
                        @RequestParam(required = false, defaultValue = "", value = "hospital") Long hospitalId,
                        @RequestParam(required = false, defaultValue = "1", value = "page") Integer page,
                        @RequestParam(required = false, defaultValue = "", value = "search") String keyword) {
                Map<String, Object> response = userFacade.getStats(page, keyword);

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(RestResponse.OK(response));
        }

        @GetMapping("/{id}/stats")
        public ResponseEntity<RestResponse<Map<String, Object>>> getStatsByWardId(
                        @PathVariable("id") Long id) {
                Map<String, Object> response = userFacade.getStats(wardService.getWard(id));

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(RestResponse.OK(response));
        }

        @GetMapping("/{id}/risks")
        public ResponseEntity<RestResponse<Map<String, Object>>> getRisksByWardId(@PathVariable("id") Long id) {
                Map<String, Object> response = userFacade.getRisksAll(wardService.getWard(id));

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(RestResponse.OK(response));
        }

        @PatchMapping("/{id}/risks")
        public ResponseEntity<RestResponse<Void>> updateAvailabilityByWardId(@PathVariable("id") Long id,
                        @RequestBody List<RiskDTO> request) {
                userFacade.updateRisk(wardService.getWard(id), request);

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(RestResponse.OK());
        }

        @GetMapping("/{id}/riskgroups")
        public ResponseEntity<RestResponse<Map<String, Object>>> getRiskGroupsByWardId(@PathVariable("id") Long id) {
                Map<String, Object> response = userFacade.getRiskGroups(wardService.getWard(id));

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(RestResponse.OK(response));
        }

        @PostMapping("/{id}/riskgroups")
        public ResponseEntity<RestResponse<RiskGroupDTO>> createRiskGroupsByWardId(@PathVariable("id") Long id,
                        @RequestBody RiskGroupDTO request) {
                RiskGroupDTO response = userFacade.createRiskGroups(wardService.getWard(id), request);
                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(RestResponse.CREATED(response));
        }

        @DeleteMapping("/{id}/riskgroups")
        public ResponseEntity<RestResponse<Void>> deleteRiskGroupsByWardId(@PathVariable("id") Long id,
                        @RequestBody RiskGroupDTO request) {
                userFacade.deleteRiskGroups(wardService.getWard(id), request);

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(RestResponse.OK());
        }

}