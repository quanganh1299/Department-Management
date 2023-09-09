package com.vti.controller;

import com.vti.service.IResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/password")
public class ResetPassWordController {
    @Autowired
    private IResetPasswordService resetPassword;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String,String> body) {
        try {
            String email = body.get("email");
            resetPassword.forgotPassword(email);
            return ResponseEntity.ok().body("Liên kết đặt lại mật khẩu đã được gửi đến " + email);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestBody Map<String,String> body, Model model) {
        try {
            String token = body.get("token");
            String password = body.get("password");
            resetPassword.resetPassword(token, password);
            model.addAttribute("success", "Mật khẩu của bạn đã được cập nhật thành công!");
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "reset-password";
    }
}
