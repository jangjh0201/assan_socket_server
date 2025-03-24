package org.asan.domain.sensor.entity.sensorType;

import org.asan.domain.sensor.dto.request.GyroscopeRequestDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Gyroscope {
    private Float gyroX;
    private Float gyroY;
    private Float gyroZ;
    private String timestamp;

    public static Gyroscope createGyroscope(GyroscopeRequestDto requestDto) {
        return Gyroscope.builder()
                .gyroX(requestDto.gyroX())
                .gyroY(requestDto.gyroY())
                .gyroZ(requestDto.gyroZ())
                .timestamp(requestDto.timestamp().toString())
                .build();
    }
}
