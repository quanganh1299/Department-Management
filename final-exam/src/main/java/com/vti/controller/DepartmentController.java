package com.vti.controller;

import com.vti.dto.DepartmentDto;
import com.vti.entity.Department;
import com.vti.form.DepartmentCreateForm;
import com.vti.form.DepartmentFilterForm;
import com.vti.form.DepartmentUpdateForm;
import com.vti.service.IDepartmentService;
import com.vti.validation.DepartmentIdExists;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {
    @Autowired
    private IDepartmentService service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public Page<DepartmentDto> findAll(DepartmentFilterForm form, Pageable pageable){
        Page<Department> departments =  service.findAll(form, pageable);
        return departments.map(department -> mapper.map(department, DepartmentDto.class).withSelfRel());
    }

    @GetMapping("/{id}")
    public DepartmentDto findById(@PathVariable("id") @DepartmentIdExists Integer id){
        Department department = service.findById(id);
        return mapper.map(department, DepartmentDto.class).withSelfRel();
    }

    @PostMapping
    public void create(@RequestBody @Valid DepartmentCreateForm form){
        service.create(form);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") @DepartmentIdExists Integer id, @RequestBody DepartmentUpdateForm form){
        form.setId(id);
        service.update(form);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") @DepartmentIdExists Integer id){
        service.deleteById(id);
    }

    @DeleteMapping
    public void deleteAllById(@RequestBody List<Integer> ids){
        service.deleteAllById(ids);
    }
}
