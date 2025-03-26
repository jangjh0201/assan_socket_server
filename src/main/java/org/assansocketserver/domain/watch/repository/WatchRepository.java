package org.assansocketserver.domain.watch.repository;

import java.util.List;
import java.util.Optional;

import org.assansocketserver.domain.patient.entity.Patient;
import org.assansocketserver.domain.watch.entity.Watch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WatchRepository extends JpaRepository<Watch, Long> {
    List<Watch> findByPatientIsNull();

    Watch findByPatient(Patient patient);

    Optional<Watch> findByUuid(String uuid);

    boolean existsByUuid(String uuid);

    boolean existsById(Long id);

    void delete(Watch watch);

    @Query("""
            SELECT w FROM Watch w
            JOIN FETCH w.patient p
            JOIN FETCH p.ward ward
            LEFT JOIN FETCH ward.risks r
            LEFT JOIN FETCH r.riskType
            WHERE w.id = :watchId
            """)
    Optional<Watch> findWithPatientWardAndRisksById(@Param("watchId") Long watchId);

}
