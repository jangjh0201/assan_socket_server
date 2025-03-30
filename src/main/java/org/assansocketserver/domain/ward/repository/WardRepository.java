package org.assansocketserver.domain.ward.repository;

import java.util.Optional;

import org.assansocketserver.auth.entity.Account;
import org.assansocketserver.domain.ward.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WardRepository extends JpaRepository<Ward, Long>, JpaSpecificationExecutor<Ward> {

    Ward findByAccount(Account account);

    Optional<Ward> findById(Long id);
}
