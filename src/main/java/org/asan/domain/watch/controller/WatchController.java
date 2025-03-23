package org.asan.domain.watch.controller;

import org.asan.domain.watch.service.WatchService;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.asan.global.common.RestResponse;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@RequestMapping("/watches")
@RestController
public class WatchController {

    private final WatchService watchService;

    @GetMapping("")
    public ResponseEntity<RestResponse<Map<String, Object>>> getWatch(
            @RequestParam(required = false, value = "patient", defaultValue = "") Long id) {
        Map<String, Object> response = watchService.getWatches(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(RestResponse.OK(response));
    }
}
