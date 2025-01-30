package org.asansocketserver.domain.position.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.asansocketserver.domain.position.dto.PositionDTO;
import org.asansocketserver.domain.position.dto.request.GetStateDTO;
import org.asansocketserver.domain.position.dto.request.PositionNameDTO;
import org.asansocketserver.domain.position.dto.request.StateDTO;
import org.asansocketserver.domain.position.service.PositionService;
import org.asansocketserver.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Position API", description = "위치 및 비콘 관련 API")
@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class PositionController {
    private final PositionService positionService;

    @Operation(summary = "비콘 개수 조회", description = "등록된 비콘의 개수를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비콘 개수 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/countBeacon")
    public ResponseEntity<SuccessResponse<?>> countBeacon() {
        return SuccessResponse.ok(positionService.countBeacon());
    }

    @Operation(summary = "CSV 파일 생성", description = "비콘 데이터를 CSV 파일로 변환하여 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CSV 파일 생성 성공"),
            @ApiResponse(responseCode = "500", description = "파일 생성 오류")
    })
    @PostMapping("/createCsv")
    public ResponseEntity<SuccessResponse<?>> createCsv() throws JsonProcessingException {
        positionService.createCsv();
        return SuccessResponse.ok("success");
    }

    @Operation(summary = "비콘 데이터 삭제", description = "특정 위치의 모든 비콘 데이터를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비콘 데이터 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 위치의 비콘 데이터 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/deleteBeacon")
    public ResponseEntity<SuccessResponse<?>> deleteBeacon(@RequestBody PositionNameDTO positionNameDTO) {
        positionService.deleteBeacon(positionNameDTO.getPosition());
        return SuccessResponse.ok("success");
    }

    @Operation(summary = "상태 추가", description = "워치의 특정 위치 상태를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상태 추가 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/insertState")
    public ResponseEntity<SuccessResponse<?>> insertState(@RequestBody StateDTO stateDTO) {
        positionService.insertState(stateDTO);
        return SuccessResponse.ok("success");
    }

    @Operation(summary = "상태 삭제", description = "워치의 특정 위치 상태를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상태 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 상태가 존재하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/deleteState")
    public ResponseEntity<SuccessResponse<?>> deleteState(@RequestBody StateDTO stateDTO) {
        positionService.deleteState(stateDTO);
        return SuccessResponse.ok("success");
    }

    @Operation(summary = "수집 상태 조회", description = "특정 워치 ID에 대한 수집 상태를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수집 상태 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 수집 상태 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/getCollectionStatus/{id}")
    public ResponseEntity<SuccessResponse<?>> getCollectionStatus(
            @Parameter(description = "조회할 워치 ID", example = "1") @PathVariable Long id) {
        return SuccessResponse.ok(positionService.getCollectionState(id));
    }
}
