package org.asansocketserver.domain.sensor.controller;

import lombok.RequiredArgsConstructor;
import org.asansocketserver.domain.position.dto.request.StateDTO;
import org.asansocketserver.domain.sensor.dto.request.*;
import org.asansocketserver.domain.sensor.dto.response.*;
import org.asansocketserver.domain.sensor.service.SensorService;
import org.asansocketserver.global.common.SuccessResponse;
import org.asansocketserver.socket.dto.MessageType;
import org.asansocketserver.socket.dto.SocketBaseResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.io.IOException;
import java.util.Map;

@Tag(name = "Sensor API", description = "센서 데이터 처리 API")
@RequiredArgsConstructor
@RestController
public class SensorApiController {
    private final SensorService sensorService;

    @Operation(summary = "센서 상태 전송", description = "워치의 센서 상태를 Redis에 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상태 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/api/sensor/sendState")
    public ResponseEntity<SuccessResponse<?>> insertState(@RequestBody StateRequestDto stateDTO) {
        sensorService.sensorSendState(stateDTO);
        return SuccessResponse.ok("success");
    }

    @Operation(summary = "센서 데이터 다운로드 (CSV ZIP)", description = "워치의 센서 데이터를 CSV 파일로 변환하여 ZIP 압축 후 다운로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "파일 다운로드 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "500", description = "파일 생성 오류")
    })
    @PostMapping("/api/sensor/downloadSensorDataAsCsvZip")
    public ResponseEntity<byte[]> downloadSensorDataAsCsvZip(
            @RequestBody DownloadRequestDto downloadRequestDto,
            @Parameter(description = "파일 분할 크기", example = "100000") @RequestParam(defaultValue = "100000") int chunkSize)
            throws IOException {
        byte[] zipBytes = sensorService.downloadSensorDataAsCsvZip(downloadRequestDto, chunkSize);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "sensorData.zip");

        return ResponseEntity.ok()
                .headers(headers)
                .body(zipBytes);
    }

    @Operation(summary = "가속도계 데이터 전송", description = "워치에서 가속도계 데이터를 WebSocket을 통해 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 전송 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @MessageMapping("/accelerometer")
    public void sendAccelerometer(
            @Parameter(description = "WebSocket 세션 속성", hidden = true) @Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes,
            @Parameter(description = "가속도계 데이터") @Payload final AccelerometerRequestDto request) {
        sensorService.sendAccelerometer(simpSessionAttributes, request);
    }

    @Operation(summary = "기압 데이터 전송", description = "워치에서 기압 데이터를 WebSocket을 통해 전송합니다.")
    @MessageMapping("/barometer")
    public void sendBarometer(
            @Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes,
            @Payload final BarometerRequestDto request) {
        sensorService.sendBarometer(simpSessionAttributes, request);
    }

    @Operation(summary = "자이로스코프 데이터 전송", description = "워치에서 자이로스코프 데이터를 WebSocket을 통해 전송합니다.")
    @MessageMapping("/gyroscope")
    public void sendGyroscope(
            @Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes,
            @Payload final GyroscopeRequestDto request) {
        sensorService.sendGyroscope(simpSessionAttributes, request);
    }

    @Operation(summary = "심박수 데이터 전송", description = "워치에서 심박수 데이터를 WebSocket을 통해 전송합니다.")
    @MessageMapping("/heart-rate")
    public void sendHeartRate(
            @Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes,
            @Payload final HeartRateRequestDto request) {
        sensorService.sendHeartRate(simpSessionAttributes, request);
    }

    @Operation(summary = "조도 데이터 전송", description = "워치에서 조도 데이터를 WebSocket을 통해 전송합니다.")
    @MessageMapping("/light")
    public void sendLight(
            @Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes,
            @Payload final LightRequestDto request) {
        sensorService.sendLight(simpSessionAttributes, request);
    }
}
