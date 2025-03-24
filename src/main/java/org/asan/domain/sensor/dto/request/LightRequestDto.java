package org.asan.domain.sensor.dto.request;

public record LightRequestDto(
        Integer value,
        Long timestamp
) {
}
