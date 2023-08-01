package com.vti.specification;

import com.vti.entity.Account;
import com.vti.entity.Department;
import com.vti.form.DepartmentFilterForm;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DepartmentSpecification {
    public static Specification<Department> builSpec(DepartmentFilterForm form){
        return (Specification<Department>) (root, query, builder) -> {
            if(form == null){
                return null;
            }
            List<Predicate> predicates = new ArrayList<>();
            if(form.getSearch() != null){
                String pattern = "%" + form.getSearch() + "%";
                Path<String> departmentName = root.get("name");
                Predicate predicate = builder.like(departmentName, pattern);
                predicates.add(predicate);
            }
            if(form.getMinId() != null){
                Path<Integer> id = root.get("id");
                Predicate predicate = builder.greaterThanOrEqualTo(id, form.getMinId()); // WHERE Id >= ?
                predicates.add(predicate);
            }
            if(form.getMaxId() != null){
                Path<Integer> id = root.get("id");
                Predicate predicate = builder.lessThanOrEqualTo(id, form.getMaxId()); // WHERE Id <= ?
                predicates.add(predicate);
            }
            if(form.getMinCreatedDate() != null){
                Path<LocalDateTime> createdAt  = root.get("createdAt");
                LocalDateTime input = form.getMinCreatedDate().atStartOfDay();
                Predicate predicate = builder.greaterThanOrEqualTo(createdAt, input);
                predicates.add(predicate);
            }
            if(form.getMaxCreatedDate() != null){
                Path<LocalDateTime> createdAt  = root.get("createdAt");
                LocalDateTime input = form.getMaxCreatedDate().atStartOfDay();
                Predicate predicate = builder.lessThanOrEqualTo(createdAt, input);
                predicates.add(predicate);
            }
            if(form.getType() != null){
                Path<Department.Type> type = root.get("type");
                Predicate predicate = builder.equal(type, form.getType());
                predicates.add(predicate);
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
