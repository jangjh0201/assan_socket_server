package org.asan.domain.position.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;

import org.asan.domain.position.dto.request.SectorNameDTO;
import org.asan.domain.position.dto.request.StateDTO;
import org.asan.domain.position.service.PositionService;
import org.asan.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Position API", description = "위치 및 비콘 관련 API")
@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class PositionController {
    private final PositionService positionService;

    @Operation(summary = "비콘 개수 조회", description = "등록된 비콘의 개수를 조회합니다.")
    @GetMapping("/countBeacon")
    public ResponseEntity<SuccessResponse<?>> countBeacon() {
        return SuccessResponse.ok(positionService.countBeacon());
    }

    @Operation(summary = "CSV 파일 생성", description = "비콘 데이터를 CSV 파일로 변환하여 저장합니다.")
    @PostMapping("/createCsv")
    public ResponseEntity<SuccessResponse<?>> createCsv() throws JsonProcessingException {
        positionService.createCsv();
        return SuccessResponse.ok("success");
    }

    @Operation(summary = "비콘 데이터 삭제", description = "특정 위치의 모든 비콘 데이터를 삭제합니다.")
    @DeleteMapping("/deleteBeacon")
    public ResponseEntity<SuccessResponse<?>> deleteBeacon(@RequestBody SectorNameDTO sectorNameDTO) {
        positionService.deleteBeacon(sectorNameDTO.getSectorName());
        return SuccessResponse.ok("success");
    }

    @Operation(summary = "상태 추가", description = "워치의 특정 위치 상태를 추가합니다.")
    @PostMapping("/insertState")
    public ResponseEntity<SuccessResponse<?>> insertState(@RequestBody StateDTO stateDTO) {
        positionService.insertState(stateDTO);
        return SuccessResponse.ok("success");
    }

    @Operation(summary = "상태 삭제", description = "워치의 특정 위치 상태를 삭제합니다.")
    @DeleteMapping("/deleteState")
    public ResponseEntity<SuccessResponse<?>> deleteState(@RequestBody StateDTO stateDTO) {
        positionService.deleteState(stateDTO);
        return SuccessResponse.ok("success");
    }

    @Operation(summary = "수집 상태 조회", description = "특정 워치 ID에 대한 수집 상태를 조회합니다.")
    @GetMapping("/getCollectionStatus/{id}")
    public ResponseEntity<SuccessResponse<?>> getCollectionStatus(@PathVariable("id") Long id) {
        return SuccessResponse.ok(positionService.getCollectionState(id));
    }
}
