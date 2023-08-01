package com.vti.validation;

import com.vti.repository.IDepartmentRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

//người kiểm tra dữ liệu có hợp lệ hay không
public class DepartmentIdExistsValidator implements ConstraintValidator<DepartmentIdExists, Integer> {
    @Autowired
    private IDepartmentRepository repository;

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext context) {
        return repository.existsById(id);
        // Hàm repository với JPA tự gen ra đoạn code existByName
        // Tuy nhiên nó sẽ trả về kết quả là có tồn tại tên đó mà ta cần là tên Department không được trùng
        // Tức không tồn tại tên trong DB thì mới đúng
        // Vậy nên ta dùng dấu !
    }
}
