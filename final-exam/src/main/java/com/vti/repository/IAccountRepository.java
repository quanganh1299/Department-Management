package com.vti.repository;

import com.vti.entity.Account;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IAccountRepository extends JpaRepository<Account,Integer>, JpaSpecificationExecutor<Account> {
    boolean existsByUsername(String username);
    Account findByEmail(String email);

}
