package com.nttdata.empleado_ms.service;

import com.nttdata.empleado_ms.client.AuthClient;
import com.nttdata.empleado_ms.exception.ResourceNotFoundException;
import com.nttdata.empleado_ms.mapper.EmployeeMapper;
import com.nttdata.empleado_ms.model.dto.*;
import com.nttdata.empleado_ms.model.entity.AreaEntity;
import com.nttdata.empleado_ms.model.entity.EmployeeEntity;
import com.nttdata.empleado_ms.model.entity.ProjectEntity;
import com.nttdata.empleado_ms.repository.AreaRepository;
import com.nttdata.empleado_ms.repository.EmployeeRepository;
import com.nttdata.empleado_ms.repository.ProjectRepository;
import com.nttdata.empleado_ms.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private AreaRepository areaRepository;
    @Mock private AuthClient authClient;
    @Mock private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeEntity entity;
    private EmployeeRequestDTO request;
    private EmployeeResponseDTO response;
    private ProjectEntity project;
    private AreaEntity area;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        project = new ProjectEntity(1L, "Proyecto Z", "Desc", true);
        area = new AreaEntity(1L, "Logística");

        entity = new EmployeeEntity();
        entity.setId(1L);
        entity.setCodeEmployee("EMP001");
        entity.setDocumentIdentity("12345678");

        request = new EmployeeRequestDTO();
        request.setCodeEmployee("EMP001");
        request.setDocumentIdentity("12345678");
        request.setFirstName("Juan");
        request.setLastName("Pérez");
        request.setEmail("jp@example.com");
        request.setPassword("123");
        request.setRoleId(2L);
        request.setProjectId(1L);
        request.setAreaId(1L);

        response = new EmployeeResponseDTO();
        response.setId(1L);
        response.setCodeEmployee("EMP001");
    }

    @Test
    void testCreateSuccess() {
        when(employeeRepository.existsByCodeEmployee("EMP001")).thenReturn(false);
        when(employeeRepository.existsByDocumentIdentity("12345678")).thenReturn(false);

        UserResponseDTO userResp = new UserResponseDTO();
        userResp.setId(99L);

        ApiResponse<UserResponseDTO> apiResp =
                new ApiResponse<>(200, "success", userResp);


        when(authClient.createUser(any())).thenReturn(apiResp);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(areaRepository.findById(1L)).thenReturn(Optional.of(area));
        when(employeeMapper.toEntity(request)).thenReturn(entity);
        when(employeeRepository.save(entity)).thenReturn(entity);
        when(employeeMapper.toResponse(entity)).thenReturn(response);

        EmployeeResponseDTO result = employeeService.create(request);

        assertThat(result.getCodeEmployee()).isEqualTo("EMP001");
        verify(employeeRepository).save(entity);
    }

    @Test
    void testFindByIdSuccess() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(employeeMapper.toResponse(entity)).thenReturn(response);

        EmployeeResponseDTO result = employeeService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> employeeService.findById(1L));
    }

    @Test
    void testFindAll() {
        when(employeeRepository.findAll()).thenReturn(List.of(entity));
        when(employeeMapper.toResponse(entity)).thenReturn(response);

        List<EmployeeResponseDTO> result = employeeService.findAll();

        assertThat(result).hasSize(1);
    }

    @Test
    void testUpdateSuccess() {
        EmployeeUpdateDTO update = new EmployeeUpdateDTO();
        update.setCodeEmployee("EMP001");
        update.setDocumentIdentity("12345678");
        update.setProjectId(1L);
        update.setAreaId(1L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(areaRepository.findById(1L)).thenReturn(Optional.of(area));
        doNothing().when(employeeMapper).updateEntityFromDto(update, entity);
        when(employeeRepository.save(entity)).thenReturn(entity);
        when(employeeMapper.toResponse(entity)).thenReturn(response);

        EmployeeResponseDTO result = employeeService.update(1L, update);

        assertThat(result.getCodeEmployee()).isEqualTo("EMP001");
    }

    @Test
    void testDelete() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(employeeRepository.save(entity)).thenReturn(entity);

        employeeService.delete(1L);

        assertThat(entity.getActive()).isFalse();
        assertThat(entity.getDeletedAt()).isNotNull();
    }

    @Test
    void testExistsByDocumentIdentity() {
        when(employeeRepository.existsByDocumentIdentity("12345678")).thenReturn(true);
        assertThat(employeeService.existsByDocumentIdentity("12345678")).isTrue();
    }

    @Test
    void testExistsByCodeEmployee() {
        when(employeeRepository.existsByCodeEmployee("EMP001")).thenReturn(true);
        assertThat(employeeService.existsByCodeEmployee("EMP001")).isTrue();
    }
}
