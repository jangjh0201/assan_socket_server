package org.assansocketserver.domain.ward.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.assansocketserver.domain.sector.dto.SectorDTO;
import org.assansocketserver.domain.sector.entity.Sector;
import org.assansocketserver.domain.sector.enums.SectorType;
import org.assansocketserver.domain.ward.dto.MapDTO;

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