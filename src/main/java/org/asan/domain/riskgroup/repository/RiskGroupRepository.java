package org.asan.domain.riskgroup.repository;

import java.util.List;
import java.util.Optional;

import org.asan.domain.riskgroup.entity.RiskGroup;
import org.asan.domain.ward.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiskGroupRepository extends JpaRepository<RiskGroup, Long> {
    List<RiskGroup> findByWard(Ward ward);

    Optional<RiskGroup> findByName(String name);

    boolean existsByNameAndWard(String name, Ward ward);
}
