package org.asan.domain.sensor.dto.response;

import org.asan.domain.sensor.entity.sensorType.Accelerometer;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record AccelerometerResponseDto(
        Float accX,
        Float accY,
        Float accZ,
        String timestamp
) {
    public static AccelerometerResponseDto of(Accelerometer accelerometer) {
        return AccelerometerResponseDto.builder()
                .accX(accelerometer.getAccX())
                .accY(accelerometer.getAccY())
                .accZ(accelerometer.getAccZ())
                .timestamp(accelerometer.getTimestamp())
                .build();
    }
}
