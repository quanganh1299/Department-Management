package com.vti.form;

import com.vti.entity.Account;
import com.vti.validation.AccountUserNameNotExists;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCreateForm {
    @AccountUserNameNotExists
    @NotBlank(message = "Không để trống")
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Account.Role role;
    private Integer departmentId;
}
