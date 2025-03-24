package org.asan.domain.sensor.entity.sensorType;

import org.asan.domain.sensor.dto.request.LightRequestDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Light {
    private Integer value;
    private String timeStamp;

    public static Light createLight(LightRequestDto requestDto) {
        return Light.builder()
                .value(requestDto.value())
                .timeStamp(requestDto.timestamp().toString())
                .build();
    }
}
