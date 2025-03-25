package org.assansocketserver.domain.patient.repository;

import org.assansocketserver.domain.patient.entity.RestrictedArea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestrictedAreaRepository extends JpaRepository<RestrictedArea, Long> {

}