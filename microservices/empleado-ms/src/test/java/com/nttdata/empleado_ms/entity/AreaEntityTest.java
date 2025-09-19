package com.nttdata.empleado_ms.entity;

import com.nttdata.empleado_ms.model.entity.AreaEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AreaEntityTest {

    @Test
    void testGettersAndSetters() {
        AreaEntity area = new AreaEntity();
        area.setId(1L);
        area.setName("Recursos Humanos");

        assertThat(area.getId()).isEqualTo(1L);
        assertThat(area.getName()).isEqualTo("Recursos Humanos");
    }

    @Test
    void testAllArgsConstructorAndToString() {
        AreaEntity area = new AreaEntity(2L, "Finanzas");
        assertThat(area.getId()).isEqualTo(2L);
        assertThat(area.getName()).isEqualTo("Finanzas");

        String str = area.toString();
        assertThat(str).contains("Finanzas");
    }
}