package com.vti.configuration.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtHandler {
    private final String secretKey;
    private final long expirationInMs;

    public JwtHandler(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.expiration-in-ms}") long expirationInMs
    ) {
        this.secretKey = secretKey;
        this.expirationInMs = expirationInMs;
    }

    public String generateToken(String username) {
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username) //truyền vào payload
                .setIssuer("com.vti") //optional: thông tin liên quan
                .setIssuedAt(new Date(currentTimeMillis)) //generate lúc nào
                .setExpiration(new Date(currentTimeMillis + expirationInMs)) //ngày hết hạn
                .signWith(SignatureAlgorithm.HS512, secretKey) //phương thức mã hóa, khóa bí mật
                .compact(); //build Token lên
    }

    public String parseUsername(String token) { //đầu vào token, đầu ra username -> giải mã token
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
