package org.assansocketserver.domain.position.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record PositionResponseDto(
        Long watchId,
        String watchName,
        Long imageId,
        String position,
        LocalDateTime currentTime) {
    public static PositionResponseDto of(Long watchId, String watchName, Long wardId, String sectorName) {
        return PositionResponseDto.builder()
                .watchId(watchId)
                .watchName(watchName)
                .imageId(wardId)
                .position(sectorName)
                .currentTime(LocalDateTime.now())
                .build();
    }
}
