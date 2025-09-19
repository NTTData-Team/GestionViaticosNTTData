package com.nttdata.auth_ms.service;

import com.nttdata.auth_ms.mapper.UserMapper;
import com.nttdata.auth_ms.model.dto.UserRequestDTO;
import com.nttdata.auth_ms.model.dto.UserResponseDTO;
import com.nttdata.auth_ms.model.entity.RoleEntity;
import com.nttdata.auth_ms.model.entity.UserEntity;
import com.nttdata.auth_ms.repository.RoleRepository;
import com.nttdata.auth_ms.repository.UserRepository;
import com.nttdata.auth_ms.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequestDTO userRequest;
    private UserEntity userEntity;
    private UserResponseDTO userResponse;
    private RoleEntity role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        role = new RoleEntity(1L, "ADMIN");

        userRequest = new UserRequestDTO();
        userRequest.setEmail("user@test.com");
        userRequest.setPassword("1234");
        userRequest.setRoleId(1L);

        userEntity = new UserEntity();
        userEntity.setEmail("user@test.com");
        userEntity.setPassword("1234");
        userEntity.setRole(role);

        userResponse = new UserResponseDTO();
    }

    @Test
    void testCreateUserSuccess() {
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(userMapper.toEntity(userRequest)).thenReturn(userEntity);
        when(passwordEncoder.encode("1234")).thenReturn("encoded");
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toResponse(userEntity)).thenReturn(userResponse);

        UserResponseDTO result = userService.createUser(userRequest);

        assertThat(result).isEqualTo(userResponse);
        verify(userRepository).save(userEntity);
    }

    @Test
    void testCreateUserEmailExists() {
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(userEntity));

        assertThatThrownBy(() -> userService.createUser(userRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El email ya está registrado");
    }

    @Test
    void testCreateUserRoleNotFound() {
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.createUser(userRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Role no encontrado con id: 1");
    }

    @Test
    void testFindByIdSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userMapper.toResponse(userEntity)).thenReturn(userResponse);

        UserResponseDTO result = userService.findById(1L);

        assertThat(result).isEqualTo(userResponse);
    }

    @Test
    void testFindByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuario no encontrado con id: 1");
    }

    @Test
    void testFindByEmailSuccess() {
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(userEntity));
        when(userMapper.toResponse(userEntity)).thenReturn(userResponse);

        UserResponseDTO result = userService.findByEmail("user@test.com");

        assertThat(result).isEqualTo(userResponse);
    }

    @Test
    void testFindByEmailNotFound() {
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByEmail("user@test.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuario no encontrado con email: user@test.com");
    }

    @Test
    void testFindAll() {
        when(userRepository.findAll()).thenReturn(List.of(userEntity));
        when(userMapper.toResponseList(List.of(userEntity))).thenReturn(List.of(userResponse));

        List<UserResponseDTO> result = userService.findAll();

        assertThat(result).containsExactly(userResponse);
    }

    @Test
    void testExistsByEmailTrue() {
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(userEntity));
        assertThat(userService.existsByEmail("user@test.com")).isTrue();
    }

    @Test
    void testExistsByEmailFalse() {
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.empty());
        assertThat(userService.existsByEmail("user@test.com")).isFalse();
    }
}