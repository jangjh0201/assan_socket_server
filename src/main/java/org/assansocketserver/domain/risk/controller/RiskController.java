package org.assansocketserver.domain.risk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.assansocketserver.domain.risk.dto.RiskDTO;
import org.assansocketserver.domain.risk.service.RiskService;
import org.assansocketserver.domain.ward.service.WardService;
import org.assansocketserver.global.common.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/risks")
@RestController
public class RiskController {

    private final WardService wardService;
    private final RiskService riskService;

    // GET /risks - 특정 Ward의 위험 목록 조회
    @GetMapping("")
    public ResponseEntity<RestResponse<Map<String, Object>>> getRisks(
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("위험 목록 조회 요청");
        Map<String, Object> response = riskService.getRisks(wardService.getCurrentWard(userDetails));
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.OK(response));
    }

    // PATCH /risks - 특정 Ward의 위험 데이터 수정 (오직 severity만 변경)
    @PatchMapping("")
    public ResponseEntity<RestResponse<Void>> updateRiskSeverity(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody List<RiskDTO> request) {
        log.info("위험 데이터 업데이트 요청: {}", request);
        riskService.updateSeverity(wardService.getCurrentWard(userDetails), request);
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.OK());
    }

    @GetMapping("/types")
    public ResponseEntity<RestResponse<Map<String, Object>>> getRiskTypes() {
        log.info("위험 유형 목록 조회 요청");
        Map<String, Object> response = riskService.getRiskTypes();
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.OK(response));
    }
}
