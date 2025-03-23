package org.asan.domain.risk.repository;

import org.asan.domain.risk.entity.Risk;
import org.asan.domain.ward.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskRepository extends JpaRepository<Risk, Long> {
    List<Risk> findByWard(Ward ward);
}
