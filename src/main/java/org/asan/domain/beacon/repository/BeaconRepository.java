package org.asan.domain.beacon.repository;

import org.asan.domain.beacon.entity.Beacon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeaconRepository extends JpaRepository<Beacon, Long> {
    
}
