package org.assansocketserver.domain.ward.dto;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import org.assansocketserver.domain.sector.dto.SectorDTO;
import org.assansocketserver.domain.sector.enums.SectorType;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MapDTO {

    @JsonProperty("image_path")
    private String imagePath; // 이미지 경로

    @JsonProperty("image_size")
    private List<Integer> imageSize; // 이미지 크기 [width, height]

    @JsonProperty("sector_types")
    private List<SectorType> sectorTypes; // 구역 유형 목록

    @JsonProperty("sectors")
    private List<SectorDTO> sectors; // 구역 정보 목록
}