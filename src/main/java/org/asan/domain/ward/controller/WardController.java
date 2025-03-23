package org.asan.domain.ward.controller;

import java.util.Map;

import org.asan.domain.ward.service.WardService;
import org.asan.global.common.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.*;

@RequiredArgsConstructor
@RequestMapping("/wards")
@RestController
public class WardController {
    private final WardService wardService;

    @GetMapping("")
    public ResponseEntity<RestResponse<Map<String, Object>>> getWards() {
        Map<String, Object> response = wardService.getWards();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(RestResponse.OK(response));
    }
}
