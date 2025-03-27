package org.assansocketserver.domain.patient.repository;

import org.assansocketserver.domain.patient.entity.HighRiskGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HighRiskGroupRepository extends JpaRepository<HighRiskGroup, Long> {

}
