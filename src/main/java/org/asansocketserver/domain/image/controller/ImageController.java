package org.asansocketserver.domain.image.controller;

import lombok.RequiredArgsConstructor;
import org.asansocketserver.domain.image.dto.*;
import org.asansocketserver.domain.image.enums.CoordinateSetting;
import org.asansocketserver.domain.image.service.ImageService;
import org.asansocketserver.domain.position.dto.PositionDTO;
import org.asansocketserver.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Image API", description = "이미지 관련 API")
@RequiredArgsConstructor
@RequestMapping("/api/image")
@RestController
public class ImageController {
    private final ImageService imageService;

    @Operation(summary = "이미지 조회", description = "특정 ID에 해당하는 이미지를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 이미지가 존재하지 않음")
    })
    @GetMapping("/getImage/{id}")
    public ResponseEntity<SuccessResponse<?>> getImage(@PathVariable Long id) {
        ImageResponseDto responseDto = imageService.getImage(id);
        return SuccessResponse.ok(responseDto);
    }

    @Operation(summary = "이미지 목록 조회", description = "웹 또는 앱에서 사용할 이미지 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "이미지 목록 조회 성공")
    @GetMapping("/getImageList")
    public ResponseEntity<SuccessResponse<?>> getImageList(@RequestParam("isWeb") Boolean isWeb) {
        ImageListDTO responseDto = imageService.getImageList(isWeb);
        return SuccessResponse.ok(responseDto);
    }

    @Operation(summary = "이미지 이름 변경", description = "이미지의 이름을 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이름 변경 성공"),
            @ApiResponse(responseCode = "400", description = "이미지 ID가 존재하지 않음")
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
    public ResponseEntity<SuccessResponse<?>> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "이미지 저장", description = "이미지를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 저장 성공"),
            @ApiResponse(responseCode = "500", description = "파일 저장 오류")
    })
    @PostMapping("/saveImage")
    public ResponseEntity<SuccessResponse<?>> saveImage(@RequestParam("imageData") MultipartFile file)
            throws IOException {
        Long imageId = imageService.saveImage(file);
        return SuccessResponse.ok(imageId);
    }

    @Operation(summary = "이미지 내 위치 및 범위 저장", description = "이미지 내 특정 위치 및 범위를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위치 및 범위 저장 성공"),
            @ApiResponse(responseCode = "400", description = "중복된 위치명 존재"),
            @ApiResponse(responseCode = "500", description = "DB 저장 오류")
    })
    @PostMapping("/postImagePositionAndCoordinates")
    public ResponseEntity<SuccessResponse<?>> saveImagePositionAndCoordinates(@RequestBody LabelDataDTO labelDataDTO) {
        imageService.saveImagePositionAndCoordinates(labelDataDTO);
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "이미지 내 위치 및 범위 삭제", description = "특정 위치 이름에 해당하는 이미지 내 위치 및 범위를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 위치 존재하지 않음")
    })
    @DeleteMapping("/deleteImagePositionAndCoordinates/{positionName}")
    public ResponseEntity<SuccessResponse<?>> deleteImagePositionAndCoordinates(@PathVariable String positionName) {
        imageService.deleteImagePositionAndCoordinates(positionName);
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "이미지 내 위치 및 좌표 조회", description = "특정 이미지 ID에 해당하는 위치 및 좌표 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 이미지가 존재하지 않음")
    })
    @GetMapping("/getPositionAndCoordinateList/{id}")
    public ResponseEntity<SuccessResponse<?>> getPositionAndCoordinateList(@PathVariable Long id,
            @RequestParam("isWeb") Boolean isWeb) {
        List<CoordinateDTO> positionList = imageService.getPositionAndCoordinateList(id, isWeb);
        return SuccessResponse.ok(positionList);
    }

    @Operation(summary = "이미지 좌표 상태 설정", description = "특정 이미지의 좌표 상태를 설정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "설정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 설정 값"),
            @ApiResponse(responseCode = "404", description = "해당 좌표가 존재하지 않음")
    })
    @PostMapping("/web/setCoordinateSetting")
    public ResponseEntity<SuccessResponse<?>> setCoordinateSetting(
            @RequestBody CoodinateSettingDto coordinateSettingDto) {
        CoodinateSettingDto responseDto = imageService.setCoordinateSetting(coordinateSettingDto);
        return SuccessResponse.ok(responseDto);
    }

    @Operation(summary = "웹에서 이미지 저장", description = "Base64 형식의 이미지 데이터를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 저장 성공"),
            @ApiResponse(responseCode = "500", description = "파일 저장 오류")
    })
    @PostMapping("/web/saveImage")
    public ResponseEntity<SuccessResponse<?>> saveImageForWeb(@RequestBody ImageBase64DTO imageBase64Dto)
            throws IOException {
        Long imageId = imageService.saveImageForWeb(imageBase64Dto.getImageData());
        return SuccessResponse.ok(imageId);
    }

    @Operation(summary = "웹에서 좌표 삭제", description = "웹에서 특정 좌표 ID에 해당하는 좌표를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 좌표가 존재하지 않음")
    })
    @DeleteMapping("/web/deleteImagePositionAndCoordinates/{coorId}")
    public ResponseEntity<SuccessResponse<?>> deleteImagePositionAndCoordinatesForWeb(@PathVariable Long coorId) {
        imageService.deleteImagePositionAndCoordinatesForWeb(coorId);
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "이미지와 위치 목록 조회", description = "이미지별로 지정된 위치 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/web/getImageWithPositionNameList")
    public ResponseEntity<SuccessResponse<?>> getImageWithPositionNameList() {
        List<ImageAndCoordinateDTO> responseDto = imageService.getImageAndPositionNameList();
        return SuccessResponse.ok(responseDto);
    }
}
