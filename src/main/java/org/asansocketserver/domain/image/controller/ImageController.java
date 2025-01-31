package org.asansocketserver.domain.image.controller;

import lombok.RequiredArgsConstructor;
import org.asansocketserver.domain.image.dto.*;
import org.asansocketserver.domain.image.service.ImageService;
import org.asansocketserver.domain.position.dto.PositionDTO;
import org.asansocketserver.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.io.IOException;
import java.util.List;

@Tag(name = "Image API", description = "이미지 관련 API")
@RequiredArgsConstructor
@RequestMapping("/api/image")
@RestController
public class ImageController {
    private final ImageService imageService;

    // --------------- 공통 ----------------
    @Operation(summary = "이미지 조회", description = "특정 ID에 해당하는 이미지를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 조회 성공"),
            @ApiResponse(responseCode = "404", description = "이미지가 존재하지 않음")
    })
    @GetMapping("/getImage/{id}")
    public ResponseEntity<SuccessResponse<?>> getImage(
            @Parameter(description = "조회할 이미지 ID", example = "1") @PathVariable Long id) {
        ImageResponseDto responseDto = imageService.getImage(id);
        return SuccessResponse.ok(responseDto);
    }

    @Operation(summary = "이미지 목록 조회", description = "웹 또는 앱에서 사용할 이미지 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 목록 조회 성공")
    })
    @GetMapping("/getImageList")
    public ResponseEntity<SuccessResponse<?>> getImageList(
            @Parameter(description = "웹 여부", example = "true") @RequestParam("isWeb") Boolean isWeb) {
        ImageListDTO responseDto = imageService.getImageList(isWeb);
        return SuccessResponse.ok(responseDto);
    }

    @Operation(summary = "웹에서 사용할 이미지 목록 조회", description = "웹에서 사용할 이미지 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웹용 이미지 목록 조회 성공")
    })
    @GetMapping("/getImageListForWeb")
    public ResponseEntity<SuccessResponse<?>> getImageListForWeb(
            @Parameter(description = "웹 여부", example = "true") @RequestParam("isWeb") Boolean isWeb) {
        ImageListForWebDto responseDto = imageService.getImageListForWeb(isWeb);
        return SuccessResponse.ok(responseDto);
    }

    @Operation(summary = "이미지 이름 변경", description = "이미지의 이름을 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이름 변경 성공"),
            @ApiResponse(responseCode = "404", description = "해당 이미지가 존재하지 않음")
    })
    @PostMapping("/nameChange")
    public ResponseEntity<SuccessResponse<?>> nameChange(@RequestBody ImageIdAndNameDTO imageIdAndNameDTO) {
        Long imageId = imageService.nameChange(imageIdAndNameDTO);
        return SuccessResponse.ok(imageId);
    }

    @Operation(summary = "이미지 삭제", description = "특정 이미지 ID에 해당하는 이미지를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 이미지가 존재하지 않음")
    })
    @DeleteMapping("/deleteImage/{imageId}")
    public ResponseEntity<SuccessResponse<?>> deleteImage(
            @Parameter(description = "삭제할 이미지 ID", example = "1") @PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "이미지 내 위치 목록 조회", description = "이미지에 설정된 위치 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위치 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 이미지의 위치 목록이 존재하지 않음")
    })
    @GetMapping("/getPositionList")
    public ResponseEntity<SuccessResponse<?>> getPositionList(
            @Parameter(description = "웹 여부", example = "true") @RequestParam("isWeb") Boolean isWeb) {
        List<PositionDTO> positionList = imageService.getPositionList(isWeb);
        return SuccessResponse.ok(positionList);
    }

    @Operation(summary = "이미지 내 위치 및 좌표 조회", description = "특정 이미지 ID에 해당하는 위치 및 좌표 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위치 및 좌표 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 이미지가 존재하지 않음")
    })
    @GetMapping("/getPositionAndCoordinateList/{id}")
    public ResponseEntity<SuccessResponse<?>> getPositionAndCoordinateList(
            @Parameter(description = "이미지 ID", example = "1") @PathVariable Long id,
            @Parameter(description = "웹 여부", example = "true") @RequestParam("isWeb") Boolean isWeb) {
        List<CoordinateDTO> positionList = imageService.getPositionAndCoordinateList(id, isWeb);
        return SuccessResponse.ok(positionList);
    }

    @Operation(summary = "이미지 내 위치 및 범위 저장", description = "이미지 내 특정 위치 및 범위를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위치 및 범위 저장 성공")
    })
    @PostMapping("/postImagePositionAndCoordinates")
    public ResponseEntity<SuccessResponse<?>> saveImagePositionAndCoordinates(@RequestBody LabelDataDTO labelDataDTO) {
        imageService.saveImagePositionAndCoordinates(labelDataDTO);
        return SuccessResponse.ok(null);
    }

    // --------------- 앱 ----------------
    @Operation(summary = "이미지 저장", description = "이미지를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 저장 성공")
    })
    @PostMapping("/saveImage")
    public ResponseEntity<SuccessResponse<?>> saveImage(
            @Parameter(description = "이미지 데이터") @RequestParam("imageData") MultipartFile file) throws IOException {
        Long imageId = imageService.saveImage(file);
        return SuccessResponse.ok(imageId);
    }

    @Operation(summary = "이미지 내 위치 및 범위 삭제", description = "특정 위치 이름에 해당하는 이미지 내 위치 및 범위를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위치 삭제 성공")
    })
    @DeleteMapping("/deleteImagePositionAndCoordinates/{positionName}")
    public ResponseEntity<SuccessResponse<?>> deleteImagePositionAndCoordinates(
            @Parameter(description = "삭제할 위치 이름", example = "회의실") @PathVariable String positionName) {
        imageService.deleteImagePositionAndCoordinates(positionName);
        return SuccessResponse.ok(null);
    }

    // --------------- 웹 ----------------
    @Operation(summary = "웹에서 이미지 저장", description = "Base64 형식의 이미지 데이터를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 저장 성공")
    })
    @PostMapping("/web/saveImage")
    public ResponseEntity<SuccessResponse<?>> saveImageForWeb(@RequestBody ImageBase64DTO imageBase64Dto)
            throws IOException {
        Long imageId = imageService.saveImageForWeb(imageBase64Dto.getImageData());
        return SuccessResponse.ok(imageId);
    }

    @Operation(summary = "웹에서 좌표 삭제", description = "웹에서 특정 좌표 ID에 해당하는 좌표를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좌표 삭제 성공")
    })
    @DeleteMapping("/web/deleteImagePositionAndCoordinates/{coorId}")
    public ResponseEntity<SuccessResponse<?>> deleteImagePositionAndCoordinatesForWeb(
            @Parameter(description = "삭제할 좌표 ID", example = "100") @PathVariable Long coorId) {
        imageService.deleteImagePositionAndCoordinatesForWeb(coorId);
        return SuccessResponse.ok(null);
    }
}
