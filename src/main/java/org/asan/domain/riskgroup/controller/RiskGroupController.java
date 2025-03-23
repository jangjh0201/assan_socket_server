package org.asan.domain.riskgroup.controller;

import java.util.Map;

import org.asan.domain.riskgroup.service.RiskGroupService;
import org.asan.domain.ward.service.WardService;
import org.asan.global.common.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/riskgroups")
@RestController
public class RiskGroupController {

    private final WardService wardService;
    private final RiskGroupService riskGroupService;

    @GetMapping("")
    public ResponseEntity<RestResponse<Map<String, Object>>> getRiskGroups(
            @AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> response = riskGroupService.getRiskGroups(wardService.getCurrentWard(userDetails));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(RestResponse.OK(response));
    }
}
