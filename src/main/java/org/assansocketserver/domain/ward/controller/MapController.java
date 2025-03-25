package org.assansocketserver.domain.ward.controller;

import lombok.RequiredArgsConstructor;

import org.assansocketserver.domain.ward.dto.MapDTO;
import org.assansocketserver.domain.ward.service.MapService;
import org.assansocketserver.domain.ward.service.WardService;
import org.assansocketserver.global.common.RestResponse;
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
