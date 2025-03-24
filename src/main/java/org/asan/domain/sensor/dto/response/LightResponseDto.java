package org.asan.domain.sensor.dto.response;

import org.asan.domain.sensor.entity.sensorType.Light;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record LightResponseDto(
        Integer value,
        String timestamp
) {
    public static LightResponseDto of(Light light) {
        return LightResponseDto.builder()
                .value(light.getValue())
                .timestamp(light.getTimeStamp())
                .build();
    }
}
