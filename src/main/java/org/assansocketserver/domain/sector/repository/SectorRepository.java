package org.assansocketserver.domain.sector.repository;

import org.assansocketserver.domain.sector.entity.Sector;
import org.assansocketserver.domain.ward.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SectorRepository extends JpaRepository<Sector, Long> {
    List<Sector> findByWard(Ward ward); // 특정 병동(Ward)에 속한 Sector 목록 조회

    Optional<Sector> findByName(String name);

    List<Sector> findAllByWard(Ward ward);

    @Query("SELECT s FROM Sector s WHERE s.ward.id = :wardId AND s.name = :name")
    Optional<Sector> findByWardIdAndName(@Param("wardId") Long wardId, @Param("name") String name);

}
