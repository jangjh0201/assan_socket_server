package org.asan.domain.sensor.dto.request;

public record AccelerometerRequestDto(
        Float accX,
        Float accY,
        Float accZ,
        Long timestamp
) {
}
