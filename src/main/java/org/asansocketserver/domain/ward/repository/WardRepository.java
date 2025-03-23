package org.asansocketserver.domain.ward.repository;

import org.asansocketserver.domain.ward.dto.WardIDAndNameAndSectorDTO;
import org.asansocketserver.domain.ward.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WardRepository extends JpaRepository<Ward, Long> {

    @Query("SELECT new org.asansocketserver.domain.ward.dto.WardIDAndNameAndSectorDTO(w.id, w.name, s.id,s.name) "
            +
            "FROM Sector s JOIN Ward w ON w = s.ward")
    List<WardIDAndNameAndSectorDTO> findWardsWithSectors();

}
