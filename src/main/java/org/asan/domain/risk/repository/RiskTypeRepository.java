package org.asan.domain.risk.repository;

import org.asan.domain.risk.entity.RiskType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiskTypeRepository extends JpaRepository<RiskType, Long> {

}
