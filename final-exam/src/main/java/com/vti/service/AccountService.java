package com.vti.service;

import com.vti.entity.Account;
import com.vti.form.AccountCreateForm;
import com.vti.form.AccountFilterForm;
import com.vti.form.AccountUpdateForm;
import com.vti.repository.IAccountRepository;
import com.vti.repository.IDepartmentRepository;
import com.vti.specification.AccountSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AccountService implements IAccountService {

    private IDepartmentRepository departmentRepository;
    private IAccountRepository repository;
    private ModelMapper mapper;
    private PasswordEncoder encoder;

    @Value("${app.baseUrl}")
    private String appBaseUrl;

    @Autowired
    public AccountService(IDepartmentRepository departmentRepository, IAccountRepository repository,
                          ModelMapper mapper, PasswordEncoder encoder) {
        this.departmentRepository = departmentRepository;
        this.repository = repository;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    @Override
    public Page<Account> findAll(AccountFilterForm form, Pageable pageable) {
        Specification<Account> spec = AccountSpecification.buildSpec(form);
        return repository.findAll(spec, pageable);
    }

    @Override
    public Account findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void create(AccountCreateForm form) {
        Account account = mapper.map(form, Account.class);
        String encodedPassword = encoder.encode(account.getPassword());
        account.setPassword(encodedPassword);
        repository.save(account);
        departmentRepository.updateTotalMembers();
    }

    @Override
    public void update(AccountUpdateForm form) {
        Account account = mapper.map(form, Account.class);
        Integer departmentId = account.getDepartment().getId();
        String encodedPassword = encoder.encode(account.getPassword());
        account.setPassword(encodedPassword);
        repository.save(account);
        departmentRepository.updateTotalMembers();
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
        departmentRepository.updateTotalMembers();
    }

    @Override
    public void deleteAllById(List<Integer> ids) {
        repository.deleteAllById(ids);
        departmentRepository.updateTotalMembers();
    }
}
