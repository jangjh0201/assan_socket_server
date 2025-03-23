package org.asansocketserver.domain.ward.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.asansocketserver.domain.position.dto.PositionDTO;
import org.asansocketserver.domain.sector.dto.SectorDTO;
import org.asansocketserver.domain.ward.dto.*;
import org.asansocketserver.domain.ward.service.WardService;
import org.asansocketserver.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/image")
@RestController
@Tag(name = "Image Controller", description = "이미지 관련 API")
public class WardController {
        private final WardService wardService;

        // --------------- 공통 ----------------
        // 이미지 조회 api
        @Operation(summary = "이미지 조회 API", description = "주어진 이미지 id에 해당하는 이미지를 조회합니다.")
        @GetMapping("/getImage/{id}")
        public ResponseEntity<SuccessResponse<?>> getImage(@PathVariable("id") Long id) {
                WardResponseDto responseDto = wardService.getImage(id);
                return SuccessResponse.ok(responseDto);
        }

        // 이미지 목록 조회 api -> RequestParam 추가
        @Operation(summary = "이미지 목록 조회 API", description = "병동 목록을 조회합니다.")
        @GetMapping("/getImageList")
        public ResponseEntity<SuccessResponse<?>> getWards() {
                WardsDTO responseDto = wardService.getWards();
                return SuccessResponse.ok(responseDto);
        }

        // 이미지내에 설정한 위치들의 이름 목록을 가져오는 api (앱에서 비콘을 모으기 위해 위치 목록을 띄울 경우 사용)
        @Operation(summary = "이미지 내 위치 목록 조회 API", description = "병동 내 위치 이름 목록을 조회합니다.")
        @GetMapping("/getPositionList")
        public ResponseEntity<SuccessResponse<?>> getSectorNames() {
                List<PositionDTO> positionList = wardService.getSectorNames();
                return SuccessResponse.ok(positionList);
        }

        // 이미지내에 설정한 위치들과 해당 좌표 목록을 가져오는 api
        @Operation(summary = "이미지 내 위치 및 좌표 목록 조회 API", description = "위치와 좌표 목록을 조회합니다.")
        @GetMapping("/getPositionAndCoordinateList/{id}")
        public ResponseEntity<SuccessResponse<?>> getSectors(@PathVariable("id") Long id) {
                List<SectorDTO> positionList = wardService.getSectors(id);
                return SuccessResponse.ok(positionList);
        }

}
