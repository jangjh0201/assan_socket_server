package org.assansocketserver.domain.sensor.dto.request;

public record HeartRateRequestDto(
        Integer value,
        Long timestamp
) {
}
