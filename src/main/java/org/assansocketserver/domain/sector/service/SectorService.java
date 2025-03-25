package org.assansocketserver.domain.sector.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.assansocketserver.domain.sector.dto.SectorDTO;
import org.assansocketserver.domain.sector.entity.Sector;
import org.assansocketserver.domain.sector.enums.SectorType;
import org.assansocketserver.domain.sector.repository.SectorRepository;
import org.assansocketserver.domain.ward.entity.Ward;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SectorService {
    private final SectorRepository sectorRepository;

    public Map<String, Object> getSectors(Ward ward) {
        List<SectorDTO> sectorDTOs = sectorRepository.findByWard(ward).stream()
                .map(sector -> SectorDTO.builder()
                        .id(sector.getId())
                        .name(sector.getName())
                        .build())
                .collect(Collectors.toList());

        return Map.of(
                "total_count", sectorDTOs.size(),
                "sectors", sectorDTOs);
    }

    /**
     * Sector 데이터 수정 (SectorType 수정)
     * 
     * @param request { id: number, sectorType: SectorType }[]
     */
    public void updateSector(Ward ward, List<SectorDTO> request) {
        List<Sector> sectors = sectorRepository.findByWard(ward);

        for (SectorDTO sectorDTO : request) {
            Long sectorId = sectorDTO.getId();
            SectorType newSectorType = SectorType.fromName(sectorDTO.getSectorType().getName());

            sectors.stream()
                    .filter(sector -> sector.getId().equals(sectorId))
                    .findFirst()
                    .ifPresent(sector -> {
                        sector.changeSectorType(newSectorType);
                    });
        }

        sectorRepository.saveAll(sectors);
    }
}
