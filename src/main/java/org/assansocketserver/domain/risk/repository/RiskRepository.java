package org.assansocketserver.domain.risk.repository;

import org.assansocketserver.domain.risk.entity.Risk;
import org.assansocketserver.domain.ward.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskRepository extends JpaRepository<Risk, Long> {
    List<Risk> findByWard(Ward ward);
}
