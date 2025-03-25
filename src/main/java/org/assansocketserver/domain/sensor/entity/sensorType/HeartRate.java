package org.assansocketserver.domain.sensor.entity.sensorType;

import org.assansocketserver.domain.sensor.dto.request.HeartRateRequestDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class HeartRate {
    private Integer value;
    private String timestamp;

    public static HeartRate createHeartRate(HeartRateRequestDto requestDto) {
        return HeartRate.builder()
                .value(requestDto.value())
                .timestamp(requestDto.timestamp().toString())
                .build();
    }
}
