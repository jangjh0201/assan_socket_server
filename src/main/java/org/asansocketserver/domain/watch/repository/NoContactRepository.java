package org.asansocketserver.domain.watch.repository;

import org.asansocketserver.domain.patient.entity.NoContact;
import org.asansocketserver.domain.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoContactRepository extends JpaRepository<NoContact, Long> {

    void deleteByPatient(Patient patient);

    void deleteByNoContact(Patient noContact);

    List<NoContact> findByPatient(Patient patient);

    List<NoContact> findByNoContact(Patient noContact);

    void deleteAllByPatient(Patient patient);

    void deleteAllByNoContact(Patient patient);
}
