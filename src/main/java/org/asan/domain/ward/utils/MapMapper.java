package org.asan.domain.ward.utils;

import org.asan.domain.ward.dto.MapDTO;
import org.asan.domain.sector.dto.SectorDTO;
import org.asan.domain.sector.entity.Sector;
import org.asan.domain.sector.enums.SectorType;

import java.util.List;
import java.util.stream.Collectors;

public class MapMapper {

    public static MapDTO toMapDto(List<SectorType> sectorTypes,
            List<Sector> sectors) {
        return MapDTO.builder()
                .sectorTypes(sectorTypes)
                .sectors(sectors.stream().map(MapMapper::toSectorDTO).collect(Collectors.toList()))
                .build();
    }

    private static SectorDTO toSectorDTO(Sector sector) {
        return SectorDTO.builder()
                .id(sector.getId())
                .name(sector.getName())
                .startX(sector.getStartX())
                .startY(sector.getStartY())
                .endX(sector.getEndX())
                .endY(sector.getEndY())
                .sectorType(sector.getSectorType())
                .build();
    }
}