package org.asansocketserver.domain.ward.repository;


import org.asansocketserver.domain.ward.entity.Sector;
import org.asansocketserver.domain.ward.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CoordinateRepository extends JpaRepository<Sector, Long> {
    Sector findByImageAndPosition(Ward image, String position);

    Optional<Sector> findByPosition(String position);

    List<Sector> findAllByImageAndIsWebFalse(Ward image);

    List<Sector> findAllByImageAndIsWebTrue(Ward image);

    List<Sector> findAllByIsWebTrue();

    List<Sector> findAllByIsWebFalse();

    Optional<Sector> findByPositionAndIsWebTrue(String prediction);
}
