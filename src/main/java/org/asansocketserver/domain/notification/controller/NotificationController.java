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

        @Operation(summary = "모든 알림 조회", description = "페이지네이션을 적용하여 필터링된 알림 목록을 조회합니다.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "알림 조회 성공"),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
                        @ApiResponse(responseCode = "500", description = "서버 오류")
        })
        @GetMapping("/getAllNotifications")
        public ResponseEntity<SuccessResponse<?>> getAllNotifications(
                        @Parameter(description = "페이지 번호 (1부터 시작)", example = "1") @RequestParam(defaultValue = "1") int page,
                        @Parameter(description = "페이지 크기", example = "20") @RequestParam(defaultValue = "20") int size,
                        @Parameter(description = "알림 유형 (예: 'WARNING', 'INFO')") @RequestParam(required = false) String type,
                        @Parameter(description = "워치 이름") @RequestParam(required = false) String watchName,
                        @Parameter(description = "워치 ID") @RequestParam(required = false) String watchId,
                        @Parameter(description = "조회 시작 날짜", example = "2024-01-01") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                        @Parameter(description = "조회 종료 날짜", example = "2024-01-31") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                        @Parameter(description = "정렬 방식 (true: 오름차순, false: 내림차순)", example = "false") @RequestParam(defaultValue = "false") boolean sortAsc) {
                List<NotificationResponseDto> notifications = notificationService.getNotifications(page, size, type,
                                watchName, watchId, startDate, endDate, sortAsc);
                return SuccessResponse.ok(notifications);
        }

        @Operation(summary = "알림 데이터 다운로드", description = "필터링된 알림 데이터를 ZIP 파일로 다운로드합니다.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "파일 다운로드 성공"),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
                        @ApiResponse(responseCode = "500", description = "파일 생성 오류")
        })
        @PostMapping("/downloadNotifications")
        public ResponseEntity<byte[]> downloadNotifications(
                        @Parameter(description = "알림 유형") @RequestParam(required = false) String type,
                        @Parameter(description = "워치 이름") @RequestParam(required = false) String watchName,
                        @Parameter(description = "워치 ID") @RequestParam(required = false) String watchId,
                        @Parameter(description = "조회 시작 날짜", example = "2024-01-01") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                        @Parameter(description = "조회 종료 날짜", example = "2024-01-31") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                        @Parameter(description = "정렬 방식 (true: 오름차순, false: 내림차순)", example = "false") @RequestParam(defaultValue = "false") boolean sortAsc,
                        @Parameter(description = "파일 분할 크기", example = "10000") @RequestParam(defaultValue = "10000") int chunkSize)
                        throws IOException {
                List<NotificationResponseDto> notifications = notificationService.getNotificationsByDateForDownload(
                                type, watchName, watchId, startDate, endDate, sortAsc);
                byte[] zipBytes = notificationService.makeZipFile(notifications, chunkSize);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment",
                                "notifications-" + startDate + "-" + endDate + ".zip");

                return ResponseEntity.ok()
                                .headers(headers)
                                .body(zipBytes);
        }

        @Operation(summary = "CSV 포맷의 알림 ZIP 다운로드", description = "지정된 날짜의 알림 데이터를 CSV 파일로 ZIP 압축하여 다운로드합니다.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "파일 다운로드 성공"),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
                        @ApiResponse(responseCode = "500", description = "파일 생성 오류")
        })
        @GetMapping("/downloadNotificationsAsCsvZip")
        public ResponseEntity<byte[]> downloadNotificationsAsCsvZip(
                        @Parameter(description = "다운로드할 날짜", example = "2024-01-01") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                        @Parameter(description = "파일 분할 크기", example = "1000") @RequestParam(defaultValue = "1000") int chunkSize)
                        throws IOException {
                List<NotificationResponseDto> notifications = notificationService.getNotificationsByDate(date);
                byte[] zipBytes = notificationService.makeZipFile(notifications, chunkSize);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", "notifications.zip");

                return ResponseEntity.ok()
                                .headers(headers)
                                .body(zipBytes);
        }
}
