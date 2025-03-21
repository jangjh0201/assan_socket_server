package org.asansocketserver.domain.watch.dto.web.response;

import lombok.Builder;

import java.util.List;

@Builder
public record RestrictedAreasUpdateResponseDto(
        Long watchId,
        List<Long> restrictedAreasIds) {

    public static RestrictedAreasUpdateResponseDto of(Long watchId, List<Long> restrictedAreasIds) {
        return RestrictedAreasUpdateResponseDto.builder()
                .watchId(watchId)
                .restrictedAreasIds(restrictedAreasIds).build();
    }
}
