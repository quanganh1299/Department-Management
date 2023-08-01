package com.vti.controller;

import com.vti.service.IResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/password")
public class ResetPassWordController {
    @Autowired
    private IResetPasswordService resetPassword;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            resetPassword.forgotPassword(email);
            return ResponseEntity.ok().body("Liên kết đặt lại mật khẩu đã được gửi đến " + email);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam String token, @RequestParam String password, Model model) {
        try {
            resetPassword.resetPassword(token, password);
            model.addAttribute("success", "Mật khẩu của bạn đã được cập nhật thành công!");
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "reset-password";
    }
}
