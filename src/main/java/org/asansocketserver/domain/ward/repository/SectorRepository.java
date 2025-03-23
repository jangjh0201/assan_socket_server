package org.asansocketserver.domain.ward.repository;

import org.asansocketserver.domain.ward.entity.Sector;
import org.asansocketserver.domain.ward.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SectorRepository extends JpaRepository<Sector, Long> {
    List<Sector> findAllByWard(Ward ward);

    Sector findByWardAndName(Ward ward, String name);

    Optional<Sector> findByName(String sectorName);
}
