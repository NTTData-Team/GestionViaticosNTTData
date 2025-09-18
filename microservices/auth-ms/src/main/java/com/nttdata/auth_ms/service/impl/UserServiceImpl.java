package com.nttdata.auth_ms.service.impl;

import com.nttdata.auth_ms.mapper.UserMapper;
import com.nttdata.auth_ms.model.dto.UserRequestDTO;
import com.nttdata.auth_ms.model.dto.UserResponseDTO;
import com.nttdata.auth_ms.model.entity.RoleEntity;
import com.nttdata.auth_ms.model.entity.UserEntity;
import com.nttdata.auth_ms.repository.RoleRepository;
import com.nttdata.auth_ms.repository.UserRepository;
import com.nttdata.auth_ms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(UserRequestDTO dto) {
        if(userRepository.findByEmail(dto.getEmail()).isPresent())
            throw new IllegalArgumentException("El email ya está registrado");
        RoleEntity role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Role no encontrado con id: " + dto.getRoleId()));

        UserEntity user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        UserEntity savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    public UserResponseDTO findById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + id));
        return userMapper.toResponse(user);
    }

    public UserResponseDTO findByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con email: " + email));
        return userMapper.toResponse(user);
    }

    public List<UserResponseDTO> findAll() {
        return userMapper.toResponseList(userRepository.findAll());
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
