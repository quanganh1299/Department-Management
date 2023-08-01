package com.vti.specification;

import com.vti.entity.Account;
import com.vti.form.AccountFilterForm;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountSpecification {

    //dùng để buil một mệnh đề where
    public static Specification<Account> buildSpec(AccountFilterForm form){
        return (root, query, builder) -> {
            if(form == null){ // người dùng không filter dữ liệu
                return null;
            }
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasText(form.getSearch())){ //có chữ thì vẫn search
                // có filter dữ liệu nhưng kiểm tra phần filter trống hay không
                String pattern = "%" + form.getSearch().trim() + "%"; // username LIKE "%search%"
                Path<String> username = root.get("username"); // get giá trị username trong Account ra
                Predicate hasUserNameLike = builder.like(username, pattern);
                Path<String> departmentName = root.get("department").get("name");
                // lấy giá trị name trong department
                Predicate hasDepartmentNameLike = builder.like(departmentName, pattern);
                // Tạo câu lệnh lấy department có name giống name truyền vào
                predicates.add(builder.or(hasUserNameLike, hasDepartmentNameLike));
                // Dùng builder để tạo điều kiện or
            }
            // another conditions
            // ...
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
            if(form.getRole() != null){
                Path<Account.Role> role = root.get("role");
                Predicate predicate = builder.equal(role, form.getRole());
                predicates.add(predicate);
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
