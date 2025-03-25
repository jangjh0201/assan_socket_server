package org.assansocketserver.auth.repository;

import org.assansocketserver.auth.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
    Boolean existsByUsername(String username);

    Account findByUsername(String username);

}
