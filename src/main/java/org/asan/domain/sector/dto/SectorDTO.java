package org.asan.domain.sector.dto;

import lombok.*;
import org.asan.domain.sector.enums.SectorType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SectorDTO {
    @JsonProperty("sector_id")
    private Long id;

    @JsonProperty("sector_name")
    private String name;

    @JsonProperty("start_x")
    private Double startX;

    @JsonProperty("start_y")
    private Double startY;

    @JsonProperty("end_x")
    private Double endX;

    @JsonProperty("end_y")
    private Double endY;

    @JsonProperty("sector_type")
    private SectorType sectorType;
}