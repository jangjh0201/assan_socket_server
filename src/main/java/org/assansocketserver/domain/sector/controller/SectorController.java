package org.assansocketserver.domain.sector.controller;

import java.util.List;
import java.util.Map;

import org.assansocketserver.domain.sector.dto.SectorDTO;
import org.assansocketserver.domain.sector.service.SectorService;
import org.assansocketserver.domain.ward.service.WardService;
import org.assansocketserver.global.common.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/sectors")
@RestController
public class SectorController {
    private final WardService wardService;
    private final SectorService sectorService;

    @GetMapping("")
    public ResponseEntity<RestResponse<Map<String, Object>>> getSectors(
            @AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> response = sectorService.getSectors(wardService.getCurrentWard(userDetails));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(RestResponse.OK(response));
    }

    @PatchMapping("")
    public ResponseEntity<RestResponse<Void>> updateSector(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody List<SectorDTO> request) {
        sectorService.updateSector(wardService.getCurrentWard(userDetails), request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(RestResponse.OK());
    }
}
