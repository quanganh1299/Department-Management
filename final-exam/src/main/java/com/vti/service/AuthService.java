package com.vti.service;

import com.vti.entity.Account;
import com.vti.form.AuthRegisterForm;
import com.vti.repository.IAuthRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService{
    private final IAuthRepository repository;
    private final ModelMapper mapper;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthService(IAuthRepository repository, ModelMapper mapper, PasswordEncoder encoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    @Override
    public void create(AuthRegisterForm form) {
        Account account = mapper.map(form, Account.class);
        String encodedPassword = encoder.encode(account.getPassword()); //mã hóa password
        account.setPassword(encodedPassword);
        repository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = repository.findByUsername(username);
        if(account == null){
            throw new UsernameNotFoundException(username);
        }
        return User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .authorities(account.getRole().toString())
                .build();
    }
}
