package org.asan.domain.watch.repository;

import java.util.List;
import java.util.Optional;

import org.asan.domain.watch.entity.Watch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.asan.domain.patient.entity.Patient;

public interface WatchRepository extends JpaRepository<Watch, Long> {
    List<Watch> findByPatientIsNull();

    Watch findByPatient(Patient patient);

    Optional<Watch> findByUuid(String uuid);

    boolean existsByUuid(String uuid);

    boolean existsById(Long id);

    void delete(Watch watch);

}
