package org.asansocketserver.domain.ward.repository;

import org.asansocketserver.domain.ward.dto.ImageIDAndNameAndCoordinateDTO;
import org.asansocketserver.domain.ward.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Ward, Long> {

    @Query("SELECT new org.asansocketserver.domain.image.dto.ImageIDAndNameAndCoordinateDTO(i.id, i.imageName, c.id,c.position) " +
            "FROM Coordinate c JOIN Image i ON i = c.image WHERE i.isWeb = true")
    List<ImageIDAndNameAndCoordinateDTO> findImagesWithCoordinates();

}
