package org.assansocketserver.domain.watch.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.assansocketserver.domain.watch.dto.request.WatchRequestDto;
import org.assansocketserver.domain.watch.dto.response.WatchAllResponseDto;
import org.assansocketserver.domain.watch.dto.response.WatchResponseDto;
import org.assansocketserver.domain.watch.service.WatchAppService;
import org.assansocketserver.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@Tag(name = "Watch API", description = "스마트워치 관리 API")
@RequiredArgsConstructor
@RequestMapping("/api/watch")
@RestController
public class WatchAppController {
    private final WatchAppService watchService;

    @Operation(summary = "모든 스마트워치 조회", description = "등록된 모든 스마트워치 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<SuccessResponse<?>> findAllWatch() {
        final WatchAllResponseDto responseDto = watchService.findAllWatch();
        return SuccessResponse.ok(responseDto);
    }

    @Operation(summary = "스마트워치 삭제", description = "특정 ID에 해당하는 스마트워치를 삭제합니다.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SuccessResponse<?>> deleteWatch(@PathVariable("id") final Long id) {
        return SuccessResponse.ok(watchService.deleteWatch(id));
    }

    @Operation(summary = "스마트워치 조회", description = "UUID를 이용하여 특정 스마트워치를 조회합니다.")
    @GetMapping("/{uuid}")
    public ResponseEntity<SuccessResponse<?>> findWatch(@PathVariable("uuid") final String uuid) {
        final WatchResponseDto responseDto = watchService.findWatch(uuid);
        return SuccessResponse.ok(responseDto);
    }

    @Operation(summary = "스마트워치 등록", description = "새로운 스마트워치를 등록합니다.")
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createWatch(@RequestBody final WatchRequestDto requestDto) {
        final WatchResponseDto responseDto = watchService.createWatch(requestDto);
        return SuccessResponse.created(responseDto);
    }

}
