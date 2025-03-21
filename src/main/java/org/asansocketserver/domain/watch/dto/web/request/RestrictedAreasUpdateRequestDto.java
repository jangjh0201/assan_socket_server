package org.asansocketserver.domain.watch.dto.web.request;

import lombok.Builder;

import java.util.List;

@Builder
public record RestrictedAreasUpdateRequestDto(
        Long watchId,
        List<Long> restrictedAreasIds) {
}
