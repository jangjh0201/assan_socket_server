package org.asan.domain.sensor.dto;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record LightDTO(
                Integer value,
                Long timestamp) {
}
