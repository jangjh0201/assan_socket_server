package org.asan.domain.ward.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.asan.domain.position.dto.PositionDTO;
import org.asan.domain.sector.entity.Sector;
import org.asan.domain.ward.dto.WardResponseDto;
import org.asan.domain.ward.dto.WardsDTO;
import org.asan.domain.ward.entity.Ward;
import org.asan.domain.ward.repository.WardRepository;
import org.asan.domain.sector.dto.CoordinateDTO;
import org.asan.domain.sector.dto.SectorDTO;
import org.asan.domain.sector.repository.SectorRepository;
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

    // // sector -> coorditateDTO 치환 (데이터 일관성을 위해 추후 앱 수정 필요)
    // public List<CoordinateDTO> getCoordinates(Long id) {
    //     Optional<Ward> ward = wardRepository.findById(id);
    //     List<Sector> sectors = sectorRepository.findAllByWard(ward.get());

    //     List<CoordinateDTO> coordinateDTOs = new ArrayList<>();

    //     if (!sectors.isEmpty()) {
    //         for (Sector sector : sectors) {
    //             CoordinateDTO coordinateDTO = CoordinateDTO.builder()
    //                     .imageId(id)
    //                     .coordinateId(sector.getId())
    //                     .latitude(null)
    //                     .longitude(null)
    //                     .position(sector.getName())
    //                     .startX(sector.getStartX())
    //                     .startY(sector.getStartY())
    //                     .endX(sector.getEndX())
    //                     .endY(sector.getEndY())
    //                     .setting(sector.getSectorType().toString())
    //                     .build();
    //             coordinateDTOs.add(coordinateDTO);
    //         }
    //     }

    //     return coordinateDTOs;
    // }

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