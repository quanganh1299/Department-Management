package com.vti.controller;

import com.vti.form.AuthRegisterForm;
import com.vti.service.IAccountService;
import com.vti.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final IAuthService service;

    @Autowired
    private IAccountService accountService;

    @Autowired
    public AuthController(IAuthService service) {this.service = service;}

    @PostMapping("/register")
    public void create(@RequestBody AuthRegisterForm form) {service.create(form);}

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            accountService.forgotPassword(email);
            return ResponseEntity.ok().body("Liên kết đặt lại mật khẩu đã được gửi đến " + email);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam String token, @RequestParam String password, Model model) {
        try {
            accountService.resetPassword(token, password);
            model.addAttribute("success", "Mật khẩu của bạn đã được cập nhật thành công!");
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "reset-password";
    }
}
