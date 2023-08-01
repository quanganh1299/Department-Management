package com.vti.service;

import com.vti.entity.Account;
import com.vti.entity.Department;
import com.vti.form.DepartmentCreateForm;
import com.vti.form.DepartmentFilterForm;
import com.vti.form.DepartmentUpdateForm;
import com.vti.repository.IDepartmentRepository;
import com.vti.specification.DepartmentSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentService implements IDepartmentService {
    @Autowired
    private IDepartmentRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public Page<Department> findAll(DepartmentFilterForm form, Pageable pageable){
        Specification<Department> spec = DepartmentSpecification.builSpec(form);
        return repository.findAll(spec, pageable);
    }

    @Override
    public Department findById(Integer id){
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Department create(DepartmentCreateForm form){
        Department department = mapper.map(form, Department.class);
        department.setTotalMembers(0);
        List<Account> accounts = department.getAccounts();
        //Account lấy từ entity vì map là đang map tới Department
        if(accounts != null){
            accounts.forEach(account -> {
                account.setDepartment(department);
            });
        }
        return repository.save(department);
    }

    @Override
    public Department update(DepartmentUpdateForm form){
        Department department = mapper.map(form, Department.class);
        return repository.save(department);
    }

    @Override
    public void deleteById(Integer id){
        repository.deleteById(id);
    }

    @Override
    public void deleteAllById(List<Integer> ids){
        repository.deleteAllById(ids);
    }
}
