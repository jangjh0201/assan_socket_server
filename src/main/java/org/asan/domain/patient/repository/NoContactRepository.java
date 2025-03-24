package org.asan.domain.patient.repository;

import org.asan.domain.patient.entity.NoContact;
import org.asan.domain.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoContactRepository extends JpaRepository<NoContact, Long> {

    void deleteByPatient(Patient patient);

    void deleteByNoContactPatient(Patient patient);

    List<NoContact> findByPatient(Patient patient);

    List<NoContact> findByNoContactPatient(Patient patient);

    void deleteAllByPatient(Patient patient);

    void deleteAllByNoContactPatient(Patient patient);
}
