package org.asan.domain.ward.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.asan.domain.position.dto.PositionDTO;
import org.asan.domain.sector.entity.Sector;
import org.asan.domain.ward.dto.WardResponseDto;
import org.asan.domain.ward.dto.WardsDTO;
import org.asan.domain.ward.entity.Ward;
import org.asan.domain.ward.repository.WardRepository;
import org.asansocketserver.domain.sector.dto.SectorDTO;
import org.asansocketserver.domain.sector.repository.SectorRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@RequiredArgsConstructor
@Transactional
@Service
public class WardAppService {
    private final SectorRepository sectorRepository;
    private final WardRepository wardRepository;

    public static String UPLOAD_DIR = "C:\\Users\\Gachon\\uploads\\images\\";
    // public static String UPLOAD_DIR = "/app/uploads/images/";

    public WardResponseDto getImage(Long id) {
        Optional<Ward> ward = wardRepository.findById(id);
        String image = ward.get().getImage();
        return WardResponseDto.of(ward.get().getId(), ward.get().getName(), image);
    }

    public WardsDTO getWards() {

        List<Long> wardIdDTOs = new ArrayList<>();
        List<String> wardNameDTOs = new ArrayList<>();
        for (Ward ward : wardRepository.findAll()) {
            wardIdDTOs.add(ward.getId());
            wardNameDTOs.add(ward.getName());
        }

        WardsDTO wardsDTO = new WardsDTO();
        wardsDTO.setWardIds(wardIdDTOs);
        wardsDTO.setWardNames(wardNameDTOs);
        return wardsDTO;
    }

    public List<SectorDTO> getSectors(Long id) {
        Optional<Ward> ward = wardRepository.findById(id);
        List<Sector> sectors = sectorRepository.findAllByWard(ward.get());

        List<SectorDTO> sectorDTOs = new ArrayList<>();

        if (!sectors.isEmpty()) {
            for (Sector sector : sectors) {
                SectorDTO sectorDTO = new SectorDTO();
                sectorDTO.setWardId(sector.getWard().getId());
                sectorDTO.setSectorId(sector.getId());
                sectorDTO.setSectorName(sector.getName());
                sectorDTO.setStartX(sector.getStartX());
                sectorDTO.setStartY(sector.getStartY());
                sectorDTO.setEndX(sector.getEndX());
                sectorDTO.setEndY(sector.getEndY());
                sectorDTO.setSectorType(sector.getSectorType());
                sectorDTOs.add(sectorDTO);
            }
        }

        return sectorDTOs;
    }

    @Transactional
    public List<PositionDTO> getSectorNames() {
        List<Sector> sectors = sectorRepository.findAll();
        List<PositionDTO> positionList = new ArrayList<>();

        if (sectors.isEmpty()) {
            throw new IllegalArgumentException("해당 이미지의 위치 목록이 존재하지 않습니다");
        } else {
            for (Sector sector : sectors) {
                PositionDTO positionDTO = new PositionDTO();
                positionDTO.setWardId(sector.getWard().getId());
                positionDTO.setSectorId(sector.getId());
                positionDTO.setSectorName(sector.getName());

                positionList.add(positionDTO);
            }
        }
        return positionList;
    }
}