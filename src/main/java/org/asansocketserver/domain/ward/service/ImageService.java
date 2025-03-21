package org.asansocketserver.domain.ward.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.asansocketserver.domain.position.dto.PositionDTO;
import org.asansocketserver.domain.position.repository.BeaconDataRepository;
import org.asansocketserver.domain.ward.dto.*;
import org.asansocketserver.domain.ward.entity.Sector;
import org.asansocketserver.domain.ward.entity.Ward;
import org.asansocketserver.domain.ward.enums.SectorType;
import org.asansocketserver.domain.ward.repository.CoordinateRepository;
import org.asansocketserver.domain.ward.repository.ImageRepository;
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
public class ImageService {
    private final CoordinateRepository coordinateRepository;
    private final ImageRepository imageRepository;
    private final BeaconDataRepository beaconDataRepository;

    public static String UPLOAD_DIR = "C:\\Users\\Gachon\\uploads\\images\\";
    // public static String UPLOAD_DIR = "/app/uploads/images/";

    public ImageResponseDto getImage(Long id) {
        Optional<Ward> ward = imageRepository.findById(id);
        String imageUrl = ward.get().getImageUrl();
        return ImageResponseDto.of(ward.get().getId(), ward.get().getName(), imageUrl);
    }

    public ImageListDTO getImageList() {

        List<Long> wardIdDTOs = new ArrayList<>();
        List<String> wardNameDTOs = new ArrayList<>();
        // List<Image> images = null;

        // if (!isWeb) {
        // images = imageRepository.findAllByIsWebFalse();
        // for (Image ward : images) {
        // wardIdDTOs.add(ward.getId());
        // wardNameDTOs.add(ward.getImageName());
        // }
        // } else {
        // images = imageRepository.findAllByIsWebTrue();
        // for (Image ward : images) {
        // wardIdDTOs.add(ward.getId());
        // wardNameDTOs.add(ward.getImageName());
        // }
        // }
        for (Ward ward : imageRepository.findAll()) {
            wardIdDTOs.add(ward.getId());
            wardNameDTOs.add(ward.getName());
        }

        ImageListDTO imageListDTO = new ImageListDTO();
        imageListDTO.setImageIds(wardIdDTOs);
        imageListDTO.setImageNames(wardNameDTOs);
        return imageListDTO;
    }

    public Long saveImage(MultipartFile file) throws IOException {

        byte[] bytes = file.getBytes();
        Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
        Files.write(path, bytes);

        Ward ward = Ward.builder().imageUrl("/images/" + file.getOriginalFilename()).name("지정되지 않음").build();
        Ward saveImage = imageRepository.save(ward);
        return saveImage.getId();
    }

    public void deleteImage(Long imageId) {

        Ward ward = (imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지가 존재하지 않습니다 :" + imageId)));

        beaconDataRepository.deleteAllByImageId(ward.getId());
        imageRepository.delete(ward);

    }

    public void saveImagePositionAndCoordinates(LabelDataDTO labelDataDTO) {

        Ward ward = (imageRepository.findById(labelDataDTO.getImageId())
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지가 존재하지 않습니다")));

        // Coordinate existingCoordinate =
        // coordinateRepository.findByImageAndPosition(ward,
        // labelDataDTO.getPosition());
        //
        // if (existingCoordinate != null) {
        // throw new IllegalArgumentException("해당 이미지의 위치가 이미 존재합니다.");
        // }

        // boolean isDuplicateName =
        // coordinateRepository.findByPosition(labelDataDTO.getPosition());
        // if (isDuplicateName) {
        // throw new IllegalArgumentException("중복 이름의 위치가 존재합니다.");
        // }

        try {
            Sector sector = Sector.builder()
                    .ward(ward)
                    .position(labelDataDTO.getPosition())
                    .startX(labelDataDTO.getStartX())
                    .startY(labelDataDTO.getStartY())
                    .endX(labelDataDTO.getEndX())
                    .endY(labelDataDTO.getEndY()).build();

            coordinateRepository.save(sector);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteImagePositionAndCoordinates(String positionName) {
        Optional<Sector> sector = coordinateRepository.findByPosition(positionName);
        beaconDataRepository.deleteAllByPosition(positionName);
        coordinateRepository.delete(sector.get());
    }

    public List<CoordinateDTO> getPositionAndCoordinateList(Long id, Boolean isWeb) {
        Optional<Ward> ward = imageRepository.findById(id);
        List<Sector> coordinateList = null;

        // if (ward.isPresent()) {
        //     if (isWeb) {
        //         coordinateList = coordinateRepository.findAllByImageAndIsWebTrue(ward.get());
        //     } else {
        //         coordinateList = coordinateRepository.findAllByImageAndIsWebFalse(ward.get());
        //     }
        // }

        coordinateList = coordinateRepository.findAllByImage(ward.get());

        List<CoordinateDTO> coordinateDTOList = new ArrayList<>();

        if (!coordinateList.isEmpty()) {
            for (Sector sector : coordinateList) {
                CoordinateDTO coordinateDTO = new CoordinateDTO();
                coordinateDTO.setImageId(sector.getWard().getId());
                coordinateDTO.setCoordinateId(sector.getId());
                coordinateDTO.setPosition(sector.getPosition());
                coordinateDTO.setStartX(sector.getStartX());
                coordinateDTO.setStartY(sector.getStartY());
                coordinateDTO.setEndX(sector.getEndX());
                coordinateDTO.setEndY(sector.getEndY());
                coordinateDTO.setSetting(String.valueOf(sector.getSetting()));
                coordinateDTOList.add(coordinateDTO);
            }
        }

        return coordinateDTOList;
    }

    @Transactional
    public List<PositionDTO> getPositionList() {
        List<Sector> coordinateList = null;
        coordinateList = coordinateRepository.findAll();

        // if (isWeb) {
        // coordinateList = coordinateRepository.findAllByIsWebTrue();
        // } else {
        // coordinateList = coordinateRepository.findAllByIsWebFalse();
        // }

        List<PositionDTO> positionList = new ArrayList<>();

        if (coordinateList.isEmpty()) {
            throw new IllegalArgumentException("해당 이미지의 위치 목록이 존재하지 않습니다");
        } else {
            for (Sector sector : coordinateList) {
                PositionDTO positionDTO = new PositionDTO();
                positionDTO.setImageId(sector.getImage().getId());
                positionDTO.setCoordinateId(sector.getId());
                positionDTO.setPosition(sector.getPosition());

                positionList.add(positionDTO);
            }
        }
        return positionList;
    }

    public List<ImageAndCoordinateDTO> getImageAndPositionNameList() {
        List<ImageIDAndNameAndCoordinateDTO> imageAndCoordinateDTOList = imageRepository.findImagesWithCoordinates();

        Map<Long, ImageAndCoordinateDTO> imageMap = new HashMap<>();
        List<CoordinateIDAndPositionDTO> positionList = new ArrayList<>();

        ImageAndCoordinateDTO imageWithCoordinates;

        for (ImageIDAndNameAndCoordinateDTO dto : imageAndCoordinateDTOList) {
            Long imageId = dto.imageId();
            ImageAndCoordinateDTO imageWithCoordinate = imageMap.get(imageId);

            if (imageWithCoordinate == null) {
                imageWithCoordinates = ImageAndCoordinateDTO.of(dto.imageId(), dto.imageName(), new ArrayList<>());
                imageMap.put(dto.imageId(), imageWithCoordinates);
            }

            CoordinateIDAndPositionDTO coordinateDTO = CoordinateIDAndPositionDTO.of(dto.coordinateId(),
                    dto.position());

            positionList = imageMap.get(dto.imageId()).positionList();
            positionList.add(coordinateDTO);
            imageWithCoordinates = ImageAndCoordinateDTO.of(dto.imageId(), dto.imageName(), positionList);
            imageMap.put(dto.imageId(), imageWithCoordinates);

        }

        return new ArrayList<>(imageMap.values());
    }
}
