package org.assansocketserver.domain.sensor.dto.response;

import org.assansocketserver.domain.sensor.entity.sensorType.HeartRate;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record HeartRateResponseDto(
        int value,
        String timeStamp
) {
    public static HeartRateResponseDto of(HeartRate heartRate) {
        return HeartRateResponseDto.builder()
                .value(heartRate.getValue())
                .timeStamp(heartRate.getTimestamp())
                .build();
    }
}
