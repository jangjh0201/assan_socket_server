package org.asan.domain.sensor.dto.response;

import org.asan.domain.sensor.entity.sensorType.Barometer;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record BarometerResponseDto(
        Float value,
        String timestamp
) {
    public static BarometerResponseDto of(Barometer barometer) {
        return BarometerResponseDto.builder()
                .value(barometer.getValue())
                .timestamp(barometer.getTimestamp())
                .build();
    }
}
