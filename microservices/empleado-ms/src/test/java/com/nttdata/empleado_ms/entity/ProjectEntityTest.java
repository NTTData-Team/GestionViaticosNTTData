package com.nttdata.empleado_ms.entity;

import com.nttdata.empleado_ms.model.entity.ProjectEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectEntityTest {

    @Test
    void testGettersAndSetters() {
        ProjectEntity project = new ProjectEntity();
        project.setId(1L);
        project.setName("Proyecto Alpha");
        project.setDescription("Descripción");
        project.setActive(true);

        assertThat(project.getId()).isEqualTo(1L);
        assertThat(project.getName()).isEqualTo("Proyecto Alpha");
        assertThat(project.getDescription()).isEqualTo("Descripción");
        assertThat(project.getActive()).isTrue();
    }

    @Test
    void testAllArgsConstructorAndToString() {
        ProjectEntity project = new ProjectEntity(2L, "Proyecto Beta", "Otro", false);

        assertThat(project.getName()).isEqualTo("Proyecto Beta");
        assertThat(project.toString()).contains("Proyecto Beta");
    }
}
