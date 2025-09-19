package com.nttdata.auth_ms.entity;


import com.nttdata.auth_ms.model.entity.RoleEntity;
import com.nttdata.auth_ms.model.entity.UserEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {

    @Test
    void testGettersSettersAndAuthorities() {
        RoleEntity role = new RoleEntity(1L, "ADMIN");
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setFirstName("Juan");
        user.setLastName("Pérez");
        user.setEmail("jp@example.com");
        user.setPassword("123");
        user.setRole(role);

        assertThat(user.getUsername()).isEqualTo("jp@example.com");
        assertThat(user.getAuthorities()).extracting("authority").containsExactly("ROLE_ADMIN");
        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isAccountNonLocked()).isTrue();
    }
}
