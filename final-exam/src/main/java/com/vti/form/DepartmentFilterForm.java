package com.vti.form;

import com.vti.entity.Department;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class DepartmentFilterForm {
    private String search;
    private Integer minId;
    private Integer maxId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) //định dạng ngày
    private LocalDate minCreatedDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) //định dạng ngày
    private LocalDate maxCreatedDate;
    private Department.Type type;
}
