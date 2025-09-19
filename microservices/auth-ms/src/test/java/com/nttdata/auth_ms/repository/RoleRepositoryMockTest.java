package com.nttdata.auth_ms.repository;

import com.nttdata.auth_ms.model.entity.RoleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RoleRepositoryMockTest {

    @Mock
    private RoleRepository roleRepository;

    private RoleEntity role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        role = new RoleEntity(1L, "ADMIN");
    }

    @Test
    void testFindById() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        Optional<RoleEntity> result = roleRepository.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("ADMIN");
        verify(roleRepository).findById(1L);
    }
}