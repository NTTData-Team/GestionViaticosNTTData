package com.nttdata.auth_ms.service;

import com.nttdata.auth_ms.model.dto.UserRequestDTO;
import com.nttdata.auth_ms.model.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO dto);
    UserResponseDTO findById(Long id);
    UserResponseDTO findByEmail(String email);
    List<UserResponseDTO> findAll();
    boolean existsByEmail(String email);
}
