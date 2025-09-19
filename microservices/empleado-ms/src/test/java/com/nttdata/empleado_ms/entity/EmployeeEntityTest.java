package com.nttdata.empleado_ms.entity;


import com.nttdata.empleado_ms.model.entity.AreaEntity;
import com.nttdata.empleado_ms.model.entity.EmployeeEntity;
import com.nttdata.empleado_ms.model.entity.ProjectEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeEntityTest {

    @Test
    void testGettersAndSetters() {
        EmployeeEntity emp = new EmployeeEntity();
        emp.setId(1L);
        emp.setUserId(2L);
        emp.setCodeEmployee("EMP001");
        emp.setPosition("Developer");
        emp.setDocumentIdentity("12345678");
        emp.setPhone("987654321");
        emp.setAddress("Lima, Perú");
        emp.setActive(true);

        ProjectEntity project = new ProjectEntity(1L, "Proyecto A", "Desc", true);
        AreaEntity area = new AreaEntity(1L, "TI");
        emp.setProject(project);
        emp.setArea(area);

        assertThat(emp.getId()).isEqualTo(1L);
        assertThat(emp.getCodeEmployee()).isEqualTo("EMP001");
        assertThat(emp.getArea().getName()).isEqualTo("TI");
        assertThat(emp.getProject().getName()).isEqualTo("Proyecto A");
    }

    @Test
    void testAllArgsConstructorAndToString() {
        ProjectEntity project = new ProjectEntity(1L, "Proyecto A", "Desc", true);
        AreaEntity area = new AreaEntity(1L, "TI");
        EmployeeEntity emp = new EmployeeEntity(
                1L, 2L, "EMP001", "Dev", "12345678", "98765", "Address", true, project, area);

        assertThat(emp.getUserId()).isEqualTo(2L);
        assertThat(emp.toString()).contains("EMP001");
    }
}
