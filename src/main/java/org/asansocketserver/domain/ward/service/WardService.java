package org.asansocketserver.domain.ward.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.asansocketserver.domain.position.dto.PositionDTO;
import org.asansocketserver.domain.position.repository.BeaconDataRepository;
import org.asansocketserver.domain.ward.dto.*;
import org.asansocketserver.domain.ward.entity.Sector;
import org.asansocketserver.domain.ward.entity.Ward;
import org.asansocketserver.domain.ward.enums.SectorType;
import org.asansocketserver.domain.ward.repository.SectorRepository;
import org.asansocketserver.domain.ward.repository.WardRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RequiredArgsConstructor
@Transactional
@Service
public class WardService {
    private final SectorRepository sectorRepository;
    private final WardRepository wardRepository;
    private final BeaconDataRepository beaconDataRepository;

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
        // List<Image> images = null;

        // if (!isWeb) {
        // images = wardRepository.findAllByIsWebFalse();
        // for (Image ward : images) {
        // wardIdDTOs.add(ward.getId());
        // wardNameDTOs.add(ward.getImageName());
        // }
        // } else {
        // images = wardRepository.findAllByIsWebTrue();
        // for (Image ward : images) {
        // wardIdDTOs.add(ward.getId());
        // wardNameDTOs.add(ward.getImageName());
        // }
        // }
        for (Ward ward : wardRepository.findAll()) {
            wardIdDTOs.add(ward.getId());
            wardNameDTOs.add(ward.getName());
        }

        WardsDTO wardsDTO = new WardsDTO();
        wardsDTO.setImageIds(wardIdDTOs);
        wardsDTO.setImageNames(wardNameDTOs);
        return wardsDTO;
    }

    public List<SectorDTO> getSectors(Long id) {
        Optional<Ward> ward = wardRepository.findById(id);

        // if (ward.isPresent()) {
        // if (isWeb) {
        // sectors = sectorRepository.findAllByImageAndIsWebTrue(ward.get());
        // } else {
        // sectors = sectorRepository.findAllByImageAndIsWebFalse(ward.get());
        // }
        // }

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

        // if (isWeb) {
        // sectors = sectorRepository.findAllByIsWebTrue();
        // } else {
        // sectors = sectorRepository.findAllByIsWebFalse();
        // }

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

    public List<WardAndSectorDTO> getWardAndSectorNameList() {
        List<WardIDAndNameAndSectorDTO> wardIDAndNameAndSectorDTOs = wardRepository.findWardsWithSectors();

        Map<Long, WardAndSectorDTO> wardMap = new HashMap<>();
        List<SectorIDAndNameDTO> positionList = new ArrayList<>();

        WardAndSectorDTO wardAndSectors;

        for (WardIDAndNameAndSectorDTO dto : wardIDAndNameAndSectorDTOs) {
            Long wardId = dto.wardId();

            if (wardMap.get(wardId) == null) {
                wardAndSectors = WardAndSectorDTO.of(dto.wardId(), dto.wardName(), new ArrayList<>());
                wardMap.put(dto.wardId(), wardAndSectors);
            }

            SectorIDAndNameDTO sectorDTO = SectorIDAndNameDTO.of(dto.sectorId(),
                    dto.sectorName());

            positionList = wardMap.get(dto.wardId()).sectorIDAndPositionDTOs();
            positionList.add(sectorDTO);
            wardAndSectors = WardAndSectorDTO.of(dto.wardId(), dto.wardName(), positionList);
            wardMap.put(dto.wardId(), wardAndSectors);

        }

        return new ArrayList<>(wardMap.values());
    }
}
