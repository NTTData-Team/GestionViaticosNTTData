package com.nttdata.auth_ms.service;

import com.nttdata.auth_ms.model.dto.LoginRequestDTO;
import com.nttdata.auth_ms.model.dto.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO loginRequest);

}
