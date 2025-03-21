package org.asansocketserver.domain.ward.dto;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ImageIDAndPositionAndCoordinateDTO(
        Long imageId,
        Long coordinateId,
        String position

) {
    public static ImageIDAndPositionAndCoordinateDTO of(Long imageId, Long coordinateId, String position) {
        return ImageIDAndPositionAndCoordinateDTO.builder()
                .imageId(imageId)
                .coordinateId(coordinateId)
                .position(position)
                .build();
    }
}


