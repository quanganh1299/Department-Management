package com.vti.service;

import com.vti.entity.Department;
import com.vti.form.DepartmentCreateForm;
import com.vti.form.DepartmentFilterForm;
import com.vti.form.DepartmentUpdateForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDepartmentService {
    Page<Department> findAll(DepartmentFilterForm form, Pageable pageable);

    Department findById(Integer id);

    Department create(DepartmentCreateForm form);

    Department update(DepartmentUpdateForm form);

    void deleteById(Integer id);

    void deleteAllById(List<Integer> ids);
}
