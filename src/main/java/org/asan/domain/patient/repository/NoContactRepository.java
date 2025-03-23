package org.asan.domain.patient.repository;

import org.asan.domain.patient.entity.NoContact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoContactRepository extends JpaRepository<NoContact, Long> {

}
