package org.asan.domain.sensor.dto;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record GyroscopeDTO(
                Float gyroX,
                Float gyroY,
                Float gyroZ,
                Long timestamp) {
}
