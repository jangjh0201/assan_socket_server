package org.asan.domain.sensor.dto.request;

public record GyroscopeRequestDto(
        Float gyroX,
        Float gyroY,
        Float gyroZ,
        Long timestamp
) {
}
