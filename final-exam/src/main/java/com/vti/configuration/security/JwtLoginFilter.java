package com.vti.configuration.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {
    private final JwtHandler jwtHandler;

    @Autowired
    public JwtLoginFilter(AuthenticationManager authenticationManager, JwtHandler jwtHandler){
        super(new AntPathRequestMatcher("/api/v1/auth/login", "POST"), authenticationManager);
        this.jwtHandler = jwtHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        // trả về đối tượng chưa được xác thực
        return getAuthenticationManager().authenticate(authentication);
        // trả về đối tượng authen đã được xác thực
    }

    @Override //trả về token đăng nhập thành công
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authResult
    )
            throws IOException, ServletException {
        String token = jwtHandler.generateToken(authResult.getName());
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
