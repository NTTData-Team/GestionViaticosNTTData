package com.nttdata.auth_ms.repository;

import com.nttdata.auth_ms.model.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserRepositoryMockTest {

    @Mock
    private UserRepository userRepository;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserEntity();
        user.setId(1L);
        user.setEmail("user@test.com");
    }

    @Test
    void testFindByEmail() {
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        Optional<UserEntity> result = userRepository.findByEmail("user@test.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("user@test.com");
        verify(userRepository).findByEmail("user@test.com");
    }
}
