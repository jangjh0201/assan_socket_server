package org.assansocketserver.domain.watch.repository;

import java.util.List;
import java.util.Optional;

import org.assansocketserver.domain.patient.entity.Patient;
import org.assansocketserver.domain.watch.entity.Watch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchRepository extends JpaRepository<Watch, Long> {
    List<Watch> findByPatientIsNull();

    Watch findByPatient(Patient patient);

    Optional<Watch> findByUuid(String uuid);

    boolean existsByUuid(String uuid);

    boolean existsById(Long id);

    void delete(Watch watch);

}
