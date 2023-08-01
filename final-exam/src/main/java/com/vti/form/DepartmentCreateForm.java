package com.vti.form;

import com.vti.validation.DepartmentNameNotExists;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
public class DepartmentCreateForm {
    @NotBlank(message = "{DepartmentForm.name.NotBlank}")
    @Length(max = 50, message = "{DepartmentForm.name.Length}")
    @DepartmentNameNotExists
    private String name;

    @Pattern(
            regexp = "DEVELOPER|TESTER|SCRUM_MASTER|PROJECT_MANAGER",
            message = "Loại phòng ban không hợp lệ"
    )
    private String type;
    private List<@Valid AccountCreateForm> accounts;

    @Getter
    @Setter
    public static class AccountCreateForm {
        @NotBlank(message = "Tài khoản không được để trống")
        @Length(max = 50, message = "Tài khoản có tối đa 50 kí tự")
        private String username;
        private String password;
        private String firstName;
        private String lastName;
        private String role;
    }
}
