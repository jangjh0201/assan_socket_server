package org.asansocketserver.domain.ward.dto;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record ImageAndCoordinateDTO(
        Long imageId,
        String imageName,
        List<CoordinateIDAndPositionDTO> positionList

) {
    public static ImageAndCoordinateDTO of(Long imageId, String imageName, List<CoordinateIDAndPositionDTO> positionList) {
        return ImageAndCoordinateDTO.builder()
                .imageId(imageId)
                .imageName(imageName)
                .positionList(positionList)
                .build();
    }

}
