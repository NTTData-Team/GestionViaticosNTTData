package com.nttdata.auth_ms.controller;

import com.nttdata.auth_ms.model.dto.LoginRequestDTO;
import com.nttdata.auth_ms.model.dto.LoginResponseDTO;
import com.nttdata.auth_ms.security.jwt.TokenProvider;
import com.nttdata.auth_ms.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO loginRequest) {
        return authService.login(loginRequest);
    }
}