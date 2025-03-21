package org.asansocketserver.domain.ward.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

import org.asansocketserver.domain.position.dto.PositionDTO;
import org.asansocketserver.domain.ward.dto.*;
import org.asansocketserver.domain.ward.enums.SectorType;
import org.asansocketserver.domain.ward.service.ImageService;
import org.asansocketserver.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/image")
@RestController
@Tag(name = "Image Controller", description = "이미지 관련 API")
public class ImageController {
        private final ImageService imageService;

        // --------------- 공통 ----------------
        // 이미지 조회 api
        @Operation(summary = "이미지 조회 API", description = "주어진 이미지 id에 해당하는 이미지를 조회합니다.")
        @GetMapping("/getImage/{id}")
        public ResponseEntity<SuccessResponse<?>> getImage(@PathVariable("id") Long id) {
                ImageResponseDto responseDto = imageService.getImage(id);
                return SuccessResponse.ok(responseDto);
        }

        // 이미지 목록 조회 api -> RequestParam 추가
        @Operation(summary = "이미지 목록 조회 API", description = "isWeb 파라미터에 따라 이미지 목록을 조회합니다.")
        @GetMapping("/getImageList")
        public ResponseEntity<SuccessResponse<?>> getImageList() {
                ImageListDTO responseDto = imageService.getImageList();
                return SuccessResponse.ok(responseDto);
        }

        @Operation(summary = "이미지 이름 변경 API", description = "이미지 id와 새로운 이름을 받아 이미지 이름을 변경합니다.")
        @PostMapping("/nameChange")
        public ResponseEntity<SuccessResponse<?>> nameChange(@RequestBody ImageIdAndNameDTO imageIdAndNameDTO) {
                Long imageId = imageService.nameChange(imageIdAndNameDTO);
                return SuccessResponse.ok(imageId);
        }

        // 이미지 삭제 api
        @Operation(summary = "이미지 삭제 API", description = "주어진 이미지 id에 해당하는 이미지를 삭제합니다.")
        @DeleteMapping("/deleteImage/{imageId}")
        public ResponseEntity<SuccessResponse<?>> deleteImage(@PathVariable("id") Long imageId) {
                imageService.deleteImage(imageId);
                return SuccessResponse.ok(null);
        }

        // 이미지내에 설정한 위치들의 이름 목록을 가져오는 api (앱에서 비콘을 모으기 위해 위치 목록을 띄울 경우 사용)
        @Operation(summary = "이미지 내 위치 목록 조회 API", description = "isWeb 파라미터에 따라 이미지 내 위치 이름 목록을 조회합니다.")
        @GetMapping("/getPositionList")
        public ResponseEntity<SuccessResponse<?>> getPositionList() {
                List<PositionDTO> positionList = imageService.getPositionList();
                return SuccessResponse.ok(positionList);
        }

        // 이미지내에 설정한 위치들과 해당 좌표 목록을 가져오는 api
        @Operation(summary = "이미지 내 위치 및 좌표 목록 조회 API", description = "주어진 이미지 id와 isWeb 파라미터에 따라 위치와 좌표 목록을 조회합니다.")
        @GetMapping("/getPositionAndCoordinateList/{id}")
        public ResponseEntity<SuccessResponse<?>> getPositionAndCoordinateList(@PathVariable("id") Long id,
                        @RequestParam("isWeb") Boolean isWeb) {
                List<CoordinateDTO> positionList = imageService.getPositionAndCoordinateList(id, isWeb);
                return SuccessResponse.ok(positionList);
        }

        // 이미지 내 위치 및 범위 생성 api
        // LabelDataDTO에 isWeb 속성 존재
        @Operation(summary = "이미지 내 위치 및 범위 생성 API", description = "LabelDataDTO를 받아 이미지 내 위치와 좌표를 저장합니다.")
        @PostMapping("/postImagePositionAndCoordinates")
        public ResponseEntity<SuccessResponse<?>> saveImagePositionAndCoordinates(
                        @RequestBody LabelDataDTO labelDataDTO) {
                imageService.saveImagePositionAndCoordinates(labelDataDTO);
                return SuccessResponse.ok(null);

        }

        // --------------- 앱 ----------------

        // 이미지 저장 api
        @Operation(summary = "이미지 저장 API (앱)", description = "MultipartFile 형태의 이미지 데이터를 받아 저장 후 이미지 id를 반환합니다.")
        @PostMapping("/saveImage")
        public ResponseEntity<SuccessResponse<?>> saveImage(@RequestParam("imageData") MultipartFile file)
                        throws IOException {
                Long imageId = imageService.saveImage(file);
                return SuccessResponse.ok(imageId);
        }

        // 이미지 내 위치 및 범위 삭제 api

        @Operation(summary = "이미지 내 위치 및 범위 삭제 API (앱)", description = "주어진 positionName에 해당하는 이미지 내 위치와 범위를 삭제합니다.")
        @DeleteMapping("/deleteImagePositionAndCoordinates/{positionName}")
        public ResponseEntity<SuccessResponse<?>> deleteImagePositionAndCoordinates(@PathVariable("positionName") String positionName) {
                imageService.deleteImagePositionAndCoordinates(positionName);
                return SuccessResponse.ok(null);
        }

}
