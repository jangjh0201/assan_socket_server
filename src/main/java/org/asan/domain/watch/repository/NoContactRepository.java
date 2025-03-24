package org.asan.domain.watch.repository;

import org.asan.domain.patient.entity.NoContact;
import org.asan.domain.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoContactRepository extends JpaRepository<NoContact, Long> {

    void deleteByPatient(Patient patient);

    void deleteByNoContact(Patient noContact);

    List<org.asan.domain.patient.entity.NoContact> findByPatient(Patient patient);

    List<NoContact> findByNoContact(Patient noContact);

    void deleteAllByPatient(Patient patient);

    void deleteAllByNoContact(Patient patient);
}
