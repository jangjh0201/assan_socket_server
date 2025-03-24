package org.asan.domain.sensor.entity.sensorType;

import org.asan.domain.sensor.dto.request.AccelerometerRequestDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Accelerometer {
    private Float accX;
    private Float accY;
    private Float accZ;
    private String timestamp;

    public static Accelerometer createAccelerometer(AccelerometerRequestDto requestDto) {
        return Accelerometer.builder()
                .accX(requestDto.accX())
                .accY(requestDto.accY())
                .accZ(requestDto.accZ())
                .timestamp(requestDto.timestamp().toString())
                .build();
    }
}
