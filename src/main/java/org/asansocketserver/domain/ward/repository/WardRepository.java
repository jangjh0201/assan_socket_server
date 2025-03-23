package org.asansocketserver.domain.ward.repository;

import org.asansocketserver.domain.ward.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WardRepository extends JpaRepository<Ward, Long> {

}
