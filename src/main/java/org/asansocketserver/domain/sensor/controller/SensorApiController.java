package org.asansocketserver.domain.sensor.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.asansocketserver.domain.sensor.dto.request.*;
import org.asansocketserver.domain.sensor.service.SensorService;
import org.asansocketserver.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;

@Slf4j
@Tag(name = "Sensor API", description = "센서 데이터 처리 API")
@RequiredArgsConstructor
@RestController
public class SensorApiController {
    private final SensorService sensorService;

    @Operation(summary = "센서 상태 전송", description = "워치의 센서 상태를 Redis에 저장합니다.")
    @PostMapping("/api/sensor/sendState")
    public ResponseEntity<SuccessResponse<?>> insertState(@RequestBody StateRequestDto stateDTO) {
        sensorService.sensorSendState(stateDTO);
        return SuccessResponse.ok("success");
    }

    @Operation(summary = "가속도계 데이터 전송", description = "워치에서 가속도계 데이터를 WebSocket을 통해 전송합니다.")
    @MessageMapping("/accelerometer")
    public void sendAccelerometer(
            @Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes,
            @Payload final AccelerometerRequestDto request) {
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
