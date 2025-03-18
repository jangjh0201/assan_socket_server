package org.asansocketserver.domain.notification.controller;

import lombok.RequiredArgsConstructor;
import org.asansocketserver.domain.notification.dto.request.response.NotificationResponseDto;
import org.asansocketserver.domain.notification.service.NotificationService;
import org.asansocketserver.global.common.SuccessResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Tag(name = "Notification API", description = "알림 관련 API")
@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {
        private final NotificationService notificationService;

        // @Operation(summary = "모든 알림 조회", description = "페이지네이션을 적용하여 필터링된 알림 목록을 조회합니다.")
        // @GetMapping("/getAllNotifications")
        // public ResponseEntity<SuccessResponse<?>> getAllNotifications(
        //                 @Parameter(description = "페이지 번호 (1부터 시작)", example = "1") @RequestParam(defaultValue = "1") int page,
        //                 @Parameter(description = "페이지 크기", example = "20") @RequestParam(defaultValue = "20") int size,
        //                 @Parameter(description = "알림 유형 (예: 'WARNING', 'INFO')") @RequestParam(required = false) String type,
        //                 @Parameter(description = "워치 이름") @RequestParam(required = false) String watchName,
        //                 @Parameter(description = "워치 ID") @RequestParam(required = false) String watchId,
        //                 @Parameter(description = "조회 시작 날짜", example = "2024-01-01") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        //                 @Parameter(description = "조회 종료 날짜", example = "2024-01-31") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        //                 @Parameter(description = "정렬 방식 (true: 오름차순, false: 내림차순)", example = "false") @RequestParam(defaultValue = "false") boolean sortAsc) {
        //         List<NotificationResponseDto> notifications = notificationService.getNotifications(page, size, type,
        //                         watchName, watchId, startDate, endDate, sortAsc);
        //         return SuccessResponse.ok(notifications);
        // }

}
