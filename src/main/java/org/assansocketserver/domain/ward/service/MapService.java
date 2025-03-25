package org.assansocketserver.domain.ward.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.assansocketserver.domain.sector.dto.SectorDTO;
import org.assansocketserver.domain.sector.entity.Sector;
import org.assansocketserver.domain.sector.enums.SectorType;
import org.assansocketserver.domain.sector.repository.SectorRepository;
import org.assansocketserver.domain.ward.dto.MapDTO;
import org.assansocketserver.domain.ward.entity.Ward;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapService {

        private final SectorRepository sectorRepository;

        public MapDTO getMap(Ward ward) {
                // 해당 Ward의 Sector 리스트 조회
                List<Sector> sectors = sectorRepository.findByWard(ward);

                // Sector 리스트를 DTO로 변환
                List<SectorDTO> sectorDTOs = sectors.stream()
                                .map(sector -> SectorDTO.builder()
                                                .id(sector.getId())
                                                .name(sector.getName())
                                                .startX(sector.getStartX())
                                                .startY(sector.getStartY())
                                                .endX(sector.getEndX())
                                                .endY(sector.getEndY())
                                                .sectorType(sector.getSectorType()) // Enum 그대로 사용
                                                .build())
                                .collect(Collectors.toList());

                // sectorTypes 리스트 (모든 값 포함)
                List<SectorType> sectorTypes = List.of(SectorType.values());

                // 최종 MapDTO 생성
                return MapDTO.builder()
                                .sectorTypes(List.copyOf(sectorTypes))
                                .sectors(sectorDTOs)
                                .build();
        }

        public MapDTO getMapInfo(Ward ward) {
                // 해당 Ward의 Sector 리스트 조회
                List<Sector> sectors = sectorRepository.findByWard(ward);

                // Sector 리스트를 DTO로 변환
                List<SectorDTO> sectorDTOs = sectors.stream()
                                .map(sector -> SectorDTO.builder()
                                                .name(sector.getName())
                                                .startX(sector.getStartX())
                                                .startY(sector.getStartY())
                                                .endX(sector.getEndX())
                                                .endY(sector.getEndY())
                                                .build())
                                .collect(Collectors.toList());

                // 최종 MapDTO 생성
                return MapDTO.builder()
                                .sectors(sectorDTOs)
                                .build();
        }
}
