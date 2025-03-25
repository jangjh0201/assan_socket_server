package org.asan.domain.ward.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.asan.domain.position.dto.PositionDTO;
import org.asan.domain.ward.dto.WardResponseDto;
import org.asan.domain.ward.dto.WardsDTO;
import org.asan.domain.ward.service.WardAppService;
import org.asan.global.common.SuccessResponse;
import org.asan.domain.sector.dto.CoordinateDTO;
import org.asan.domain.sector.dto.SectorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/image")
@RestController
@Tag(name = "Image Controller", description = "이미지 관련 API")
public class WardAppController {
        private final WardAppService wardAppService;

        // --------------- 앱 ----------------
        // 이미지 조회 api
        @Operation(summary = "이미지 조회 API", description = "주어진 이미지 id에 해당하는 이미지를 조회합니다.")
        @GetMapping("/getImage/{id}")
        public ResponseEntity<SuccessResponse<?>> getImage(@PathVariable("id") Long id) {
                WardResponseDto responseDto = wardAppService.getImage(id);
                return SuccessResponse.ok(responseDto);
        }

        // 이미지 목록 조회 api -> RequestParam 추가
        @Operation(summary = "이미지 목록 조회 API", description = "병동 목록을 조회합니다.")
        @GetMapping("/getImageList")
        public ResponseEntity<SuccessResponse<?>> getWards() {
                WardsDTO responseDto = wardAppService.getWards();
                return SuccessResponse.ok(responseDto);
        }

        // 이미지내에 설정한 위치들의 이름 목록을 가져오는 api (앱에서 비콘을 모으기 위해 위치 목록을 띄울 경우 사용)
        @Operation(summary = "이미지 내 위치 목록 조회 API", description = "병동 내 위치 이름 목록을 조회합니다.")
        @GetMapping("/getPositionList")
        public ResponseEntity<SuccessResponse<?>> getSectorNames() {
                List<PositionDTO> positionList = wardAppService.getSectorNames();
                return SuccessResponse.ok(positionList);
        }

        // // 이미지내에 설정한 위치들과 해당 좌표 목록을 가져오는 api
        // @Operation(summary = "이미지 내 위치 및 좌표 목록 조회 API", description = "위치와 좌표 목록을
        // 조회합니다.")
        // @GetMapping("/getPositionAndCoordinateList/{id}")
        // public ResponseEntity<SuccessResponse<?>> getCoordinates(@PathVariable("id")
        // Long id) {
        // List<CoordinateDTO> positionList = wardAppService.getCoordinates(id);
        // return SuccessResponse.ok(positionList);
        // }

}
