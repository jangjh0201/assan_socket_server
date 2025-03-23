package org.asan.domain.notification.controller;

import java.util.Map;

import org.asan.auth.dto.CustomUserDetails;
import org.asan.domain.notification.service.EmergencyService;
import org.asan.domain.ward.service.WardService;
import org.asan.global.common.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/emergencies")
@RestController
public class EmergencyController {

        private final WardService wardService;
        private final EmergencyService emergencyService;

        @GetMapping("")
        public ResponseEntity<RestResponse<Map<String, Object>>> getEmergencies(
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        @RequestParam(required = false, defaultValue = "000101", value = "startdate") String startdate,
                        @RequestParam(required = false, defaultValue = "991231", value = "enddate") String enddate,
                        @RequestParam(required = false, defaultValue = "date", value = "sort") String sort,
                        @RequestParam(required = false, defaultValue = "desc", value = "sortby") String sortby,
                        @RequestParam(required = false, defaultValue = "1", value = "page") Integer page,
                        @RequestParam(required = false, value = "search") String keyword) {
                Map<String, Object> response = emergencyService.getEmergencies(wardService.getCurrentWard(userDetails),
                                startdate, enddate, sort, sortby, page, keyword);
                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(RestResponse.OK(response));
        }
}
