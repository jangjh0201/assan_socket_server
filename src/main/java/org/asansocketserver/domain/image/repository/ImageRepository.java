package org.asansocketserver.domain.image.repository;

import org.asansocketserver.domain.image.dto.ImageIDAndNameAndCoordinateDTO;
import org.asansocketserver.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByIsWebTrue();

    List<Image> findAllByIsWebFalse();

    @Query("SELECT new org.asansocketserver.domain.image.dto.ImageIDAndNameAndCoordinateDTO(i.id, i.imageName, c.id,c.position) " +
            "FROM Coordinate c JOIN Image i ON i = c.image WHERE i.isWeb = true")
    List<ImageIDAndNameAndCoordinateDTO> findImagesWithCoordinates();

}
