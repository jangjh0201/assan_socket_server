package org.assansocketserver.domain.position.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionDTO {
    @JsonProperty("imageId")
    private Long wardId;
    @JsonProperty("coordinateId")
    private Long sectorId;
    @JsonProperty("position")
    private String sectorName;
}
