package org.asansocketserver.domain.ward.dto;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record SectorIDAndNameDTO(
        Long sectorId,
        String sectorName

) {
    public static SectorIDAndNameDTO of(Long sectorId, String sectorName) {
        return SectorIDAndNameDTO.builder()
                .sectorId(sectorId)
                .sectorName(sectorName)
                .build();
    }

}
