package org.asansocketserver.domain.image.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.asansocketserver.domain.image.dto.*;
import org.asansocketserver.domain.image.entity.Coordinate;
import org.asansocketserver.domain.image.entity.Image;
import org.asansocketserver.domain.image.enums.CoordinateSetting;
import org.asansocketserver.domain.image.repository.CoordinateRepository;
import org.asansocketserver.domain.image.repository.ImageRepository;
import org.asansocketserver.domain.position.dto.PositionDTO;
import org.asansocketserver.domain.position.repository.BeaconDataRepository;
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

//    public static String UPLOAD_DIR = "/Users/parkjaeseok/Desktop/image/";

    public static String UPLOAD_DIR = "/app/uploads/images/";


    public ImageResponseDto getImage(Long id)  {
        Optional<Image> image = imageRepository.findById(id);
        String imageUrl = image.get().getImageUrl();
        return  ImageResponseDto.of(image.get().getId(),image.get().getImageName(),imageUrl);
    }



    public ImageListDTO getImageList(Boolean isWeb) {

        List<Long>  imageIdDtoArrayList = new ArrayList<>();
        List<String>  imageNameDtoArrayList = new ArrayList<>();
        List<Image> images = null;

        if(!isWeb){
            images = imageRepository.findAllByIsWebFalse();
            for (Image image : images) {
                imageIdDtoArrayList.add(image.getId());
                imageNameDtoArrayList.add(image.getImageName());
            }
        }
        else {
            images = imageRepository.findAllByIsWebTrue();
            for (Image image : images) {
                imageIdDtoArrayList.add(image.getId());
                imageNameDtoArrayList.add(image.getImageName());
            }
        }

        ImageListDTO imageListDTO = new ImageListDTO();
        imageListDTO.setImageIds(imageIdDtoArrayList);
        imageListDTO.setImageNames(imageNameDtoArrayList);
        return imageListDTO;
    }

    public ImageListForWebDto getImageListForWeb(Boolean isWeb) {

        List<Long>  imageIdDtoArrayList = new ArrayList<>();
        List<String>  imageNameDtoArrayList = new ArrayList<>();
        List<String>  imageUrlDtoArrayList = new ArrayList<>();
        List<Image> images = null;


        images = imageRepository.findAllByIsWebTrue();
        for (Image image : images) {
            imageIdDtoArrayList.add(image.getId());
            imageNameDtoArrayList.add(image.getImageName());
            imageUrlDtoArrayList.add(image.getImageUrl());
        }

        return  ImageListForWebDto.of(imageIdDtoArrayList,imageNameDtoArrayList,imageUrlDtoArrayList);
    }


    public Long saveImage(MultipartFile file) throws IOException {

        byte[] bytes = file.getBytes();
        Path path = Paths.get(UPLOAD_DIR   + file.getOriginalFilename());
        Files.write(path, bytes);

        Image image = Image.builder().imageUrl("/images/" + file.getOriginalFilename()).imageName("지정되지 않음").isWeb(false).build();
        Image saveImage = imageRepository.save(image);
        return saveImage.getId();
    }


    public Long nameChange(ImageIdAndNameDTO imageIdAndNameDTO) {

        Image image = (imageRepository.findById(imageIdAndNameDTO.getImageId()).orElseThrow(() ->
                new IllegalArgumentException("해당 이미지가 존재하지 않습니다 :" + imageIdAndNameDTO.getImageId())));


        image.updateName(imageIdAndNameDTO.getImageName());

        return image.getId();
    }


    public void deleteImage(Long imageId) {

        Image image = (imageRepository.findById(imageId).orElseThrow(() ->
                new IllegalArgumentException("해당 이미지가 존재하지 않습니다 :" + imageId)));

        beaconDataRepository.deleteAllByImageId(image.getId());
        imageRepository.delete(image);

    }

    public void saveImagePositionAndCoordinates(LabelDataDTO labelDataDTO) {

        Image image = (imageRepository.findById(labelDataDTO.getImageId()).orElseThrow(() ->
                new IllegalArgumentException("해당 이미지가 존재하지 않습니다")));


//        Coordinate existingCoordinate = coordinateRepository.findByImageAndPosition(image, labelDataDTO.getPosition());
//
//        if (existingCoordinate != null) {
//            throw new IllegalArgumentException("해당 이미지의 위치가 이미 존재합니다.");
//        }

//        boolean isDuplicateName = coordinateRepository.findByPosition(labelDataDTO.getPosition());
//        if (isDuplicateName) {
//            throw new IllegalArgumentException("중복 이름의 위치가 존재합니다.");
//        }

        try {
            Coordinate coordinate = Coordinate.builder()
                    .image(image)
                    .position(labelDataDTO.getPosition())
                    .latitude(labelDataDTO.getLatitude())
                    .longitude(labelDataDTO.getLongitude())
                    .startX(labelDataDTO.getStartX())
                    .startY(labelDataDTO.getStartY())
                    .endX(labelDataDTO.getEndX())
                    .endY(labelDataDTO.getEndY())
                    .isWeb(labelDataDTO.getIsWeb()).build();

            coordinateRepository.save(coordinate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteImagePositionAndCoordinates(String positionName) {
        Optional<Coordinate> coordinate = coordinateRepository.findByPosition(positionName);
        beaconDataRepository.deleteAllByPosition(positionName);
        coordinateRepository.delete(coordinate.get());
    }



    public List<CoordinateDTO> getPositionAndCoordinateList(Long id , Boolean isWeb) {
        Optional<Image> image = imageRepository.findById(id);
        List<Coordinate> coordinateList = null;

        if(image.isPresent()){
            if(isWeb){
                coordinateList  = coordinateRepository.findAllByImageAndIsWebTrue(image.get());
            }
            else{
                coordinateList  = coordinateRepository.findAllByImageAndIsWebFalse(image.get());
            }
        }

        List<CoordinateDTO> coordinateDTOList = new ArrayList<>();

        if (!coordinateList.isEmpty()) {
            for (Coordinate coordinate : coordinateList) {
                CoordinateDTO coordinateDTO = new CoordinateDTO();
                coordinateDTO.setImageId(coordinate.getImage().getId());
                coordinateDTO.setCoordinateId(coordinate.getId());
                coordinateDTO.setLatitude(coordinate.getLatitude());
                coordinateDTO.setLongitude(coordinate.getLongitude());
                coordinateDTO.setPosition(coordinate.getPosition());
                coordinateDTO.setStartX(coordinate.getStartX());
                coordinateDTO.setStartY(coordinate.getStartY());
                coordinateDTO.setEndX(coordinate.getEndX());
                coordinateDTO.setEndY(coordinate.getEndY());
                coordinateDTO.setSetting(String.valueOf(coordinate.getSetting()));
                coordinateDTOList.add(coordinateDTO);
            }
        }

        return coordinateDTOList;
    }

    @Transactional
    public List<PositionDTO> getPositionList(Boolean isWeb) {
        List<Coordinate> coordinateList = null;

        if(isWeb){
            coordinateList = coordinateRepository.findAllByIsWebTrue();
        }else{
            coordinateList = coordinateRepository.findAllByIsWebFalse();
        }

        List<PositionDTO> positionList = new ArrayList<>();

        if (coordinateList.isEmpty()) {
            throw new IllegalArgumentException("해당 이미지의 위치 목록이 존재하지 않습니다");
        } else {
            for (Coordinate coordinate : coordinateList) {
                PositionDTO positionDTO = new PositionDTO();
                positionDTO.setImageId(coordinate.getImage().getId());
                positionDTO.setCoordinateId(coordinate.getId());
                positionDTO.setPosition(coordinate.getPosition());

                positionList.add(positionDTO);
            }
        }
        return positionList;
    }

    public Long saveImageForWeb(String base64Image) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        Path path = Paths.get(UPLOAD_DIR + fileName);
        Files.write(path, imageBytes);

        Image image = Image.builder().imageUrl("/images/" + fileName).imageName("지정되지 않음").isWeb(true).build();
        Image savedImage = imageRepository.save(image);
        return savedImage.getId();
    }

    public void deleteImagePositionAndCoordinatesForWeb(Long coorId) {
        Optional<Coordinate> optionalCoordinate = coordinateRepository.findById(coorId);
        if (optionalCoordinate.isPresent()) {
            Coordinate coordinate = optionalCoordinate.get();
            coordinateRepository.delete(coordinate);
        } else {
            // 처리할 오류 또는 예외 던지기
            throw new IllegalArgumentException("해당 좌표가 존재하지 않습니다.");
        }
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

            CoordinateIDAndPositionDTO coordinateDTO = CoordinateIDAndPositionDTO.of(dto.coordinateId(),dto.position());

            positionList = imageMap.get(dto.imageId()).positionList();
            positionList.add(coordinateDTO);
            imageWithCoordinates = ImageAndCoordinateDTO.of(dto.imageId(), dto.imageName(),positionList);
            imageMap.put(dto.imageId(), imageWithCoordinates);

        }


        return new ArrayList<>(imageMap.values());
    }

    public CoodinateSettingDto setCoordinateSetting(CoodinateSettingDto coordinateSettingDto) {
        Optional<Coordinate> optionalCoordinate = coordinateRepository.findById(coordinateSettingDto.coordinateId());
  
        if (optionalCoordinate.isPresent()) {

            Coordinate coordinate = optionalCoordinate.get();
            String setting = coordinateSettingDto.setting();

            CoordinateSetting coordinateSetting = mapSettingToEnum(setting);
            coordinate.updateSetting(coordinateSetting);

        }

        return coordinateSettingDto;
    }

    private CoordinateSetting mapSettingToEnum(String setting) {
        return switch (setting) {
            case "MAN" -> CoordinateSetting.MAN;
            case "FEMALE" -> CoordinateSetting.FEMALE;
            case "PROHIBITION" -> CoordinateSetting.PROHIBITION;
            default -> null;
        };
    }
}



