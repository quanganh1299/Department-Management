package com.vti.validation;

import com.vti.repository.IAccountRepository;
import com.vti.repository.IDepartmentRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

// kiểm tra dữ liệu có hợp lệ hay không
public class AccountUserNameNotExistsValidator implements ConstraintValidator<AccountUserNameNotExists, String> {
    @Autowired
    private IAccountRepository repository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return !repository.existsByUsername(username);
        // Hàm repository với JPA tự gen ra đoạn code existByName (có trong repository)
        // Tuy nhiên nó sẽ trả về kết quả là có tồn tại tên đó mà ta cần là tên Account không được trùng
        // Tức không tồn tại tên trong DB thì mới đúng
        // Vậy nên ta dùng dấu !
    }
}
