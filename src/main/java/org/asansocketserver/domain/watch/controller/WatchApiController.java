package org.asansocketserver.domain.watch.controller;

import lombok.RequiredArgsConstructor;
import org.asansocketserver.domain.watch.dto.web.request.WatchNoContactedRequestDto;
import org.asansocketserver.domain.watch.dto.request.WatchRequestDto;
import org.asansocketserver.domain.watch.dto.request.WatchUpdateRequestDto;
import org.asansocketserver.domain.watch.dto.response.WatchAllResponseDto;
import org.asansocketserver.domain.watch.dto.response.WatchResponseDto;
import org.asansocketserver.domain.watch.dto.web.request.WatchProhibitedCoordinatesUpdateRequestDto;
import org.asansocketserver.domain.watch.dto.web.request.WatchTransferDto;
import org.asansocketserver.domain.watch.dto.web.request.WatchUpdateRequestForWebDto;
import org.asansocketserver.domain.watch.dto.web.response.*;
import org.asansocketserver.domain.watch.service.WatchService;
import org.asansocketserver.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@Tag(name = "Watch API", description = "스마트워치 관리 API")
@RequiredArgsConstructor
@RequestMapping("/api/watch")
@RestController
public class WatchApiController {
    private final WatchService watchService;

    @Operation(summary = "모든 스마트워치 조회", description = "등록된 모든 스마트워치 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<SuccessResponse<?>> findAllWatch() {
        final WatchAllResponseDto responseDto = watchService.findAllWatch();
        return SuccessResponse.ok(responseDto);
    }

    @Operation(summary = "스마트워치 삭제", description = "특정 ID에 해당하는 스마트워치를 삭제합니다.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SuccessResponse<?>> deleteWatch(
            @Parameter(description = "삭제할 스마트워치 ID", example = "1") @PathVariable("id") final Long id) {
        return SuccessResponse.ok(watchService.deleteWatch(id));
    }

    @Operation(summary = "스마트워치 조회", description = "UUID를 이용하여 특정 스마트워치를 조회합니다.")
    @GetMapping("/{uuid}")
    public ResponseEntity<SuccessResponse<?>> findWatch(
            @Parameter(description = "조회할 스마트워치 UUID") @PathVariable final String uuid) {
        final WatchResponseDto responseDto = watchService.findWatch(uuid);
        return SuccessResponse.ok(responseDto);
    }

    @Operation(summary = "스마트워치 등록", description = "새로운 스마트워치를 등록합니다.")
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createWatch(@RequestBody final WatchRequestDto requestDto) {
        final WatchResponseDto responseDto = watchService.createWatch(requestDto);
        return SuccessResponse.created(responseDto);
    }

    @Operation(summary = "스마트워치 정보 수정", description = "특정 스마트워치의 정보를 수정합니다.")
    @PostMapping("/{id}")
    public ResponseEntity<SuccessResponse<?>> updateWatchInfo(
            @Parameter(description = "수정할 스마트워치 ID", example = "1") @PathVariable("id") final Long id,
            @RequestBody final WatchUpdateRequestDto requestDto) {
        final WatchResponseDto responseDto = watchService.updateWatchInfo(id, requestDto);
        return SuccessResponse.created(responseDto);
    }

    // @Operation(summary = "웹에서 스마트워치 정보 수정", description = "웹을 통해 특정 스마트워치 정보를
    // 수정합니다.")
    // @PostMapping("/web/updateWatchInfoForWeb")
    // public ResponseEntity<SuccessResponse<?>> updateWatchInfoForWeb(
    // @RequestBody final WatchUpdateRequestForWebDto requestDto) {
    // final WatchResponseForWebDto responseDto =
    // watchService.updateWatchInfoForWeb(requestDto);
    // return SuccessResponse.created(responseDto);
    // }

    // @Operation(summary = "웹에서 스마트워치 목록 조회", description = "웹을 통해 등록된 모든 스마트워치 목록을
    // 조회합니다.")
    // @GetMapping("/web")
    // public ResponseEntity<SuccessResponse<?>> findAllWatchForWeb() {
    // final WatchAllResponseForWebDto responseDto =
    // watchService.findAllWatchForWeb();
    // return SuccessResponse.ok(responseDto);
    // }

    // @Operation(summary = "웹에서 스마트워치 조회 (ID)", description = "스마트워치 ID를 이용하여 특정
    // 스마트워치를 조회합니다.")
    // @GetMapping("/web/getWatch/{watchId}")
    // public ResponseEntity<SuccessResponse<?>> findWatchByIdForWeb(
    // @Parameter(description = "조회할 스마트워치 ID", example = "1") @PathVariable Long
    // watchId) {
    // final WatchResponseForWebDto responseDto =
    // watchService.findWatchByIdForWeb(watchId);
    // return SuccessResponse.ok(responseDto);
    // }

    // @Operation(summary = "웹에서 스마트워치 조회 (UUID)", description = "스마트워치 UUID를 이용하여
    // 특정 스마트워치를 조회합니다.")
    // @GetMapping("/web/{uuid}")
    // public ResponseEntity<SuccessResponse<?>> findWatchForWeb(
    // @Parameter(description = "조회할 스마트워치 UUID") @PathVariable final String uuid) {
    // final WatchResponseForWebDto responseDto =
    // watchService.findWatchForWeb(uuid);
    // return SuccessResponse.ok(responseDto);
    // }

    // @Operation(summary = "접촉 금지 대상 스마트워치 목록 조회", description = "접촉 금지 대상 스마트워치
    // 목록을 조회합니다.")
    // @GetMapping("/web/getWatchForNoContact")
    // public ResponseEntity<SuccessResponse<?>> getWatchForNoContact() {
    // List<WatchIdAndNameDto> responseDto = watchService.getWatchForNoContact();
    // return SuccessResponse.ok(responseDto);
    // }

    // @Operation(summary = "접촉 금지 및 금지된 위치 조회", description = "특정 스마트워치의 접촉 금지 대상 및
    // 금지된 위치 정보를 조회합니다.")
    // @GetMapping("/web/getNoContactAndProhibitedIdWithName/{watchId}")
    // public ResponseEntity<SuccessResponse<?>>
    // getNoContactAndProhibitedIdWithName(
    // @Parameter(description = "조회할 스마트워치 ID", example = "1") @PathVariable Long
    // watchId) {
    // NoContactAndProhibitedIdWithNameDto responseDto =
    // watchService.getNoContactAndProhibitedIdWithName(watchId);
    // return SuccessResponse.ok(responseDto);
    // }

    // @Operation(summary = "스마트워치 정보 이관", description = "스마트워치 정보를 다른 스마트워치로
    // 이관합니다.")
    // @PostMapping("/web/transferWatchInfo")
    // public ResponseEntity<SuccessResponse<?>> transferWatchInfo(@RequestBody
    // final WatchTransferDto requestDto) {
    // final WatchResponseForWebDto responseDto =
    // watchService.transferWatchInfo(requestDto);
    // return SuccessResponse.ok(responseDto);
    // }
}
