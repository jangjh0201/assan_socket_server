package org.asan.domain.sensor.entity.sensorType;

import org.asan.domain.sensor.dto.request.BarometerRequestDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Barometer {
    private Float value;
    private String timestamp;

    public static Barometer createBarometer(BarometerRequestDto requestDto) {
        return Barometer.builder()
                .value(requestDto.value())
                .timestamp(requestDto.timestamp().toString())
                .build();
    }
}
