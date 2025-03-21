package org.asansocketserver.domain.ward.dto;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record CoordinateIDAndPositionDTO(
        Long coordinateId,
        String position

) {
    public static CoordinateIDAndPositionDTO of(Long coordinateId, String position) {
        return CoordinateIDAndPositionDTO.builder()
                .coordinateId(coordinateId)
                .position(position)
                .build();
    }

}
