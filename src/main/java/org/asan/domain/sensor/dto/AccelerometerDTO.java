package org.asan.domain.sensor.dto;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record AccelerometerDTO(
        Float accX,
        Float accY,
        Float accZ,
        Long timestamp) {
}
