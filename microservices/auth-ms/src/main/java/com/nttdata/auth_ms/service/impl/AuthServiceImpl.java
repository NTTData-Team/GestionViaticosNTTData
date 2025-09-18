package com.nttdata.auth_ms.service.impl;

import com.nttdata.auth_ms.model.dto.LoginRequestDTO;
import com.nttdata.auth_ms.model.dto.LoginResponseDTO;
import com.nttdata.auth_ms.security.jwt.TokenProvider;
import com.nttdata.auth_ms.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            String token = tokenProvider.generateToken(authentication);
            return new LoginResponseDTO(token);
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
    }
}