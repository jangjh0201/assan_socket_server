package org.asan.domain.sensor.dto.request;

public record BarometerRequestDto(
        Float value,
        Long timestamp
) {
}
