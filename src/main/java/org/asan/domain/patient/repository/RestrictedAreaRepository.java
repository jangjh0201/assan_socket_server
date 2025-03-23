package org.asan.domain.patient.repository;

import org.asan.domain.patient.entity.RestrictedArea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestrictedAreaRepository extends JpaRepository<RestrictedArea, Long> {

}