package org.assansocketserver.domain.position.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class SectorNameDTO {
    @JsonProperty("position")
    private String sectorName;
}
