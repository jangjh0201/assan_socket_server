package org.assansocketserver.domain.sensor.dto.request;

public record LightRequestDto(
        Integer value,
        Long timestamp
) {
}
