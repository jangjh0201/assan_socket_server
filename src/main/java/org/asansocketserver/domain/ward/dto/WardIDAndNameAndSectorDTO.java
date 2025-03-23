package org.asansocketserver.domain.ward.dto;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record WardIDAndNameAndSectorDTO(
        Long wardId,
        String wardName,
        Long sectorId,
        String sectorName

) {
}


