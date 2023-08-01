package com.vti.controller;

import com.vti.dto.AccountDto;
import com.vti.entity.Account;
import com.vti.form.AccountCreateForm;
import com.vti.form.AccountFilterForm;
import com.vti.form.AccountUpdateForm;
import com.vti.service.IAccountService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    @Autowired
    private IAccountService service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public Page<AccountDto> findAll(AccountFilterForm form, Pageable pageable) {
        // Với Page thì nó có thể tự map
        Page<Account> accounts = service.findAll(form, pageable);
        return accounts.map(account -> mapper.map(account, AccountDto.class).withSelfRel());
    }

    @GetMapping("/{id}")
    public AccountDto findById(@PathVariable("id") Integer id){
        Account account = service.findById(id);
        // map account sang kiểu dữ liệu AccountDto
        return mapper.map(account, AccountDto.class)
                                .withSelfRel();
        // thay vì gán vào account dto thì gán vào link cho chính nó
    }

    @PostMapping
    public void create(@RequestBody AccountCreateForm form){
        service.create(form);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") Integer id, @RequestBody AccountUpdateForm form){
        form.setId(id);
        service.update(form);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer id){
        service.deleteById(id);
    }

    @DeleteMapping
    public void deleteAllById(@RequestBody List<Integer> ids){
        service.deleteAllById(ids);
    }
}
