package com.nttdata.empleado_ms.repository;

import com.nttdata.empleado_ms.model.entity.EmployeeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeRepositoryMockTest {

    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeEntity employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new EmployeeEntity();
        employee.setId(1L);
        employee.setCodeEmployee("EMP001");
        employee.setDocumentIdentity("12345678");
        employee.setActive(true);
    }

    @Test
    void testExistsByCodeEmployee() {
        when(employeeRepository.existsByCodeEmployee("EMP001")).thenReturn(true);

        boolean exists = employeeRepository.existsByCodeEmployee("EMP001");

        assertThat(exists).isTrue();
        verify(employeeRepository).existsByCodeEmployee("EMP001");
    }

    @Test
    void testFindByDocumentIdentity() {
        when(employeeRepository.findByDocumentIdentity("12345678")).thenReturn(Optional.of(employee));

        Optional<EmployeeEntity> result = employeeRepository.findByDocumentIdentity("12345678");

        assertThat(result).isPresent();
        assertThat(result.get().getCodeEmployee()).isEqualTo("EMP001");
        verify(employeeRepository).findByDocumentIdentity("12345678");
    }

    @Test
    void testFindAllByActiveTrue() {
        when(employeeRepository.findAllByActiveTrue()).thenReturn(List.of(employee));

        List<EmployeeEntity> list = employeeRepository.findAllByActiveTrue();

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getActive()).isTrue();
        verify(employeeRepository).findAllByActiveTrue();
    }
}