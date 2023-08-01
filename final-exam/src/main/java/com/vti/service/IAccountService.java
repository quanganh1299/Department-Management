package com.vti.service;

import com.vti.entity.Account;
import com.vti.form.AccountCreateForm;
import com.vti.form.AccountFilterForm;
import com.vti.form.AccountUpdateForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAccountService {
    Page<Account> findAll(AccountFilterForm form, Pageable pageable);

    Account findById(Integer id);

    void create(AccountCreateForm form);

    void update(AccountUpdateForm form);

    void deleteById(Integer id);

    void deleteAllById(List<Integer> ids);
}
