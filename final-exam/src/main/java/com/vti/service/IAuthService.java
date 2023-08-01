package com.vti.service;

import com.vti.form.AuthRegisterForm;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IAuthService extends UserDetailsService {
    void create(AuthRegisterForm form);
}
