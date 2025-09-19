package com.nttdata.auth_ms.entity;

import com.nttdata.auth_ms.model.entity.RoleEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleEntityTest {

    @Test
    void testGettersSetters() {
        RoleEntity role = new RoleEntity();
        role.setId(1L);
        role.setName("ADMIN");

        assertThat(role.getId()).isEqualTo(1L);
        assertThat(role.getName()).isEqualTo("ADMIN");
    }

    @Test
    void testBuilder() {
        RoleEntity role = RoleEntity.builder()
                .id(2L)
                .name("USER")
                .build();

        assertThat(role.getId()).isEqualTo(2L);
        assertThat(role.getName()).isEqualTo("USER");
    }
}