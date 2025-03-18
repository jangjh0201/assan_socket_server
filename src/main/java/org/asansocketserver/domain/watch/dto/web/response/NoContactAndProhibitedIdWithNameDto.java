package org.asansocketserver.domain.watch.dto.web.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.asansocketserver.domain.image.dto.ImageIDAndPositionAndCoordinateDTO;

import java.util.List;


@Builder(access = AccessLevel.PRIVATE)
public record NoContactAndProhibitedIdWithNameDto(
        List<WatchIdAndNameDto> noContactWatchIdAndNameDtos,
        List<ImageIDAndPositionAndCoordinateDTO> prohibitedIdWithNameDtos
) {
    public static NoContactAndProhibitedIdWithNameDto of(List<WatchIdAndNameDto> watchIdAndNameDtos, List<ImageIDAndPositionAndCoordinateDTO> noContactAndProhibitedIdWithNameDtos) {
        return NoContactAndProhibitedIdWithNameDto.builder()
                .noContactWatchIdAndNameDtos(watchIdAndNameDtos)
                .prohibitedIdWithNameDtos(noContactAndProhibitedIdWithNameDtos)
                .build();
    }
}
