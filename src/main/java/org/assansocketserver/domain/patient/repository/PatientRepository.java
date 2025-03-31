package org.assansocketserver.domain.patient.repository;

import java.util.List;

import org.assansocketserver.domain.patient.entity.Patient;
import org.assansocketserver.domain.ward.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository

public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {

    Integer countByWard(Ward ward);

    Integer countByWardAndWatchIsNotNull(Ward ward);

    List<Patient> findAllByWard(Ward ward);

    List<Patient> findAllByWardAndNameContaining(Ward ward, String name);

    Boolean existsByNumber(String name);

}
