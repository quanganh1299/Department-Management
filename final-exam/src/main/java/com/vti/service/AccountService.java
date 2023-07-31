package com.vti.service;

import com.vti.entity.Account;
import com.vti.entity.Department;
import com.vti.form.AccountCreateForm;
import com.vti.form.AccountFilterForm;
import com.vti.form.AccountUpdateForm;
import com.vti.repository.IAccountRepository;
import com.vti.repository.IDepartmentRepository;
import com.vti.specification.AccountSpecification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService implements IAccountService {

    private IDepartmentRepository departmentRepository;
    private IAccountRepository repository;
    private ModelMapper mapper;
    private PasswordEncoder encoder;
    private JavaMailSender javaMailSender;
    @Value("${app.baseUrl}")
    private String appBaseUrl;

    @Autowired
    public AccountService(IDepartmentRepository departmentRepository, IAccountRepository repository,
                          ModelMapper mapper, PasswordEncoder encoder, JavaMailSender javaMailSender) {
        this.departmentRepository = departmentRepository;
        this.repository = repository;
        this.mapper = mapper;
        this.encoder = encoder;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public Page<Account> findAll(AccountFilterForm form, Pageable pageable) {
        Specification<Account> spec = AccountSpecification.buildSpec(form);
        return repository.findAll(spec, pageable);
    }

    @Override
    public Account findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void create(AccountCreateForm form) {
        Account account = mapper.map(form, Account.class);
        String encodedPassword = encoder.encode(account.getPassword());
        account.setPassword(encodedPassword);
        repository.save(account);
        departmentRepository.updateTotalMembers();
    }

    @Override
    public void update(AccountUpdateForm form) {
        Account account = mapper.map(form, Account.class);
        Integer departmentId = account.getDepartment().getId();
        String encodedPassword = encoder.encode(account.getPassword());
        account.setPassword(encodedPassword);
        repository.save(account);
        departmentRepository.updateTotalMembers();
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
        departmentRepository.updateTotalMembers();
    }

    @Override
    public void deleteAllById(List<Integer> ids) {
        repository.deleteAllById(ids);
        departmentRepository.updateTotalMembers();
    }

    @Override
    public Account findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public void forgotPassword(String email) {
        Account account = findByEmail(email);
        if (account == null) {
            throw new RuntimeException("Email không tồn tại.");
        }
        String token = generatePasswordResetToken();
        account.setPasswordResetToken(token);
        repository.save(account);
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
        Account account = repository.findByPasswordResetToken(token);

        if (account == null) {
            throw new RuntimeException("Mã số bảo mật không hợp lệ hoặc đã hết hạn.");
        }

        account.setPassword(encodePassword(newPassword)); // Mã hóa mật khẩu mới
        account.setPasswordResetToken(null);
        repository.save(account);
    }

    private String encodePassword(String rawPassword) {
        return new BCryptPasswordEncoder().encode(rawPassword);
    }

}
