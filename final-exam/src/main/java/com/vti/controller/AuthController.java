package com.vti.controller;

import com.vti.form.AuthRegisterForm;
import com.vti.service.IAuthService;
import com.vti.service.IResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final IAuthService service;

    @Autowired
    public AuthController(IAuthService service) {this.service = service;}

    @PostMapping("/register")
    public void create(@RequestBody AuthRegisterForm form) {service.create(form);}
}
