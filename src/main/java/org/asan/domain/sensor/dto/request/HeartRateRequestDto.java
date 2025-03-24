package org.asan.domain.sensor.dto.request;

public record HeartRateRequestDto(
        Integer value,
        Long timestamp
) {
}
