package org.asansocketserver.domain.ward.dto;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record WardIDAndSectorDTO(
        Long wardId,
        Long sectorId,
        String sectorName

) {
    public static WardIDAndSectorDTO of(Long wardId, Long sectorId, String sectorName) {
        return WardIDAndSectorDTO.builder()
                .wardId(wardId)
                .sectorId(sectorId)
                .sectorName(sectorName)
                .build();
    }
}


