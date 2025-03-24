package org.asan.domain.sector.repository;

import org.asan.domain.sector.entity.Sector;
import org.asan.domain.ward.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SectorRepository extends JpaRepository<Sector, Long> {
    List<Sector> findByWard(Ward ward); // 특정 병동(Ward)에 속한 Sector 목록 조회

    Optional<Sector> findByName(String name);

    List<Sector> findAllByWard(Ward ward);

    Sector findByWardAndName(Ward ward, String name);

    
}
