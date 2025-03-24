package org.asan.domain.sector.dto;

import lombok.*;

import java.math.BigDecimal;

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

    private Long wardId;
    private Long sectorId;
    private String sectorName;

    @JsonProperty("start_x")
    private BigDecimal startX;

    @JsonProperty("start_y")
    private BigDecimal startY;

    @JsonProperty("end_x")
    private BigDecimal endX;

    @JsonProperty("end_y")
    private BigDecimal endY;

    @JsonProperty("sector_type")
    private SectorType sectorType;
}