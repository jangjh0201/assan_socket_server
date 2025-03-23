package org.asansocketserver.domain.ward.dto;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record WardAndSectorDTO(
        Long wardId,
        String wardName,
        List<SectorIDAndNameDTO> sectorIDAndPositionDTOs

) {
    public static WardAndSectorDTO of(Long wardId, String wardName, List<SectorIDAndNameDTO> sectorIDAndPositionDTOs) {
        return WardAndSectorDTO.builder()
                .wardId(wardId)
                .wardName(wardName)
                .sectorIDAndPositionDTOs(sectorIDAndPositionDTOs)
                .build();
    }

}
