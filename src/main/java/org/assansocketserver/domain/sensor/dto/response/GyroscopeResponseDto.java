package org.assansocketserver.domain.sensor.dto.response;

import org.assansocketserver.domain.sensor.entity.sensorType.Gyroscope;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record GyroscopeResponseDto(
        Float gyroX,
        Float gyroY,
        Float gyroZ,
        String timestamp
) {
    public static GyroscopeResponseDto of(Gyroscope gyroscope) {
        return GyroscopeResponseDto.builder()
                .gyroX(gyroscope.getGyroX())
                .gyroY(gyroscope.getGyroY())
                .gyroZ(gyroscope.getGyroZ())
                .timestamp(gyroscope.getTimestamp())
                .build();
    }
}
