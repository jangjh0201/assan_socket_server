package org.assansocketserver.domain.sensor.dto.request;

public record BarometerRequestDto(
        Float value,
        Long timestamp
) {
}
