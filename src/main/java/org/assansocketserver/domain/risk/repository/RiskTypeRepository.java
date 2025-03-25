package org.assansocketserver.domain.risk.repository;

import org.assansocketserver.domain.risk.entity.RiskType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiskTypeRepository extends JpaRepository<RiskType, Long> {

}
