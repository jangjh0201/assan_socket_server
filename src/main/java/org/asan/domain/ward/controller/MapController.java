package org.asan.domain.ward.controller;

import lombok.RequiredArgsConstructor;

import org.asan.domain.ward.dto.MapDTO;
import org.asan.domain.ward.service.MapService;
import org.asan.domain.ward.service.WardService;
import org.asan.global.common.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/map")
@RestController
public class MapController {

    private final WardService wardService;
    private final MapService mapService;

    @GetMapping("")
    public ResponseEntity<RestResponse<MapDTO>> getMap(
            @AuthenticationPrincipal UserDetails userDetails) {
        MapDTO mapDTO = mapService.getMap(wardService.getCurrentWard(userDetails));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(RestResponse.OK(mapDTO));
    }
}
