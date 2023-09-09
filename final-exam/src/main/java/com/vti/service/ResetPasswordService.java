package com.vti.service;

import com.vti.entity.Account;
import com.vti.entity.Token;
import com.vti.repository.IAccountRepository;
import com.vti.repository.ITokenRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class ResetPasswordService implements IResetPasswordService{
    private ITokenRepository repository;
    private IAccountRepository accountRepository;
    private JavaMailSender javaMailSender;

    @Autowired
    public ResetPasswordService(ITokenRepository repository, IAccountRepository accountRepository,
                                JavaMailSender javaMailSender) {
        this.repository = repository;
        this.accountRepository = accountRepository;
        this.javaMailSender = javaMailSender;
    }

    public void forgotPassword(String email) {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new RuntimeException("Email không tồn tại.");
        }
        String token = generatePasswordResetToken();
        if (account.getToken() == null) {
            account.setToken(new Token());
        }
        account.getToken().setPasswordResetToken(token);
        accountRepository.save(account);
        sendPasswordResetEmail(account.getEmail(), token);
    }


    private void sendPasswordResetEmail(String recipientEmail, String token) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("quanganhdoan55@gmail.com");
            helper.setTo(recipientEmail);
            helper.setTo(recipientEmail);
            helper.setSubject("Liên kết đặt lại mật khẩu");
            helper.setText("Đây là mã số bảo mật để thiết lập lại mật khẩu của bạn: " + token +
                    "\n\nVui lòng không cung cấp mã số bảo mật này cho bất cứ ai nhằm đảm bảo an toàn cho tài khoản của bạn");

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Có lỗi xảy ra khi gửi email đặt lại mật khẩu", e);
        }
    }

    private String generatePasswordResetToken() {
        String characters = "0123456789";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder tokenBuilder = new StringBuilder();
        for(int i = 0; i < 8; i++){
            int randomIndex = secureRandom.nextInt(characters.length());
            tokenBuilder.append(characters.charAt(randomIndex));
        }
        return tokenBuilder.toString();
    }

    public void resetPassword(String token, String newPassword) {
        Account account = repository.findByPasswordResetToken(token).getAccount();
        if (account == null) {
            throw new RuntimeException("Mã số bảo mật không hợp lệ hoặc đã hết hạn.");
        }

        account.setPassword(encodePassword(newPassword)); // Mã hóa mật khẩu mới
        account.getToken().setPasswordResetToken(null);
        accountRepository.save(account);
    }

    private String encodePassword(String rawPassword) {
        return new BCryptPasswordEncoder().encode(rawPassword);
    }
}
