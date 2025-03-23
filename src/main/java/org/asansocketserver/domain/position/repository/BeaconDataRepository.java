package org.asansocketserver.domain.position.repository;

import org.asansocketserver.domain.position.entity.BeaconData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BeaconDataRepository extends JpaRepository<BeaconData, Integer> {
    List<BeaconData> findAllBySectorName(String sectorName);

    @Query("SELECT s.name, COUNT(b) FROM Sector s LEFT JOIN BeaconData b ON b.sectorName = s.name GROUP BY s.name")
    List<Object[]> findAllBeaconCount();

    public void deleteAllByWardId(Long wardId);

    public void deleteAllBySectorName(String sectorName);
}