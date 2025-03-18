package org.asansocketserver.domain.image.dto;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ImageIDAndNameAndCoordinateDTO(
        Long imageId,
        String imageName,
        Long coordinateId,
        String position

) {
}


