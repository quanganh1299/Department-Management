package com.vti.form;

import com.vti.entity.Account;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountUpdateForm {
    private Integer id;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Account.Role role;
    private Integer departmentId;
}
