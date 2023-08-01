package com.vti.repository;

import com.vti.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITokenRepository extends JpaRepository<Token,Integer>{
    Token findByPasswordResetToken(String token);
}
