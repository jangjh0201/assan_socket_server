package org.asansocketserver.domain.position.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record PositionResponseDto(
        Long watchId,
        String watchName,
        Long wardId,
        String sectorName,
        LocalDateTime currentTime
        ) {
    public static PositionResponseDto of(Long watchId, String watchName, Long wardId, String sectorName) {
        return PositionResponseDto.builder()
                .watchId(watchId)
                .watchName(watchName)
                .wardId(wardId)
                .sectorName(sectorName)
                .currentTime(LocalDateTime.now())
                .build();
    }
}
