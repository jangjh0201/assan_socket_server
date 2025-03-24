package org.asan.domain.position.repository;

import org.asan.domain.position.entity.entity.Beacon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BeaconRepository extends JpaRepository<Beacon, Integer> {
    List<Beacon> findAllBySectorName(String sectorName);

    @Query("SELECT s.name, COUNT(b) FROM Sector s LEFT JOIN Beacon b ON b.sectorName = s.name GROUP BY s.name")
    List<Object[]> findAllBeaconCount();

    public void deleteAllByWardId(Long wardId);

    public void deleteAllBySectorName(String sectorName);
}