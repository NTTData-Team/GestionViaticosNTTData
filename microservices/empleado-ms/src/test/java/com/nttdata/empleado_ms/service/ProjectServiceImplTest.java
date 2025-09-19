package com.nttdata.empleado_ms.service;


import com.nttdata.empleado_ms.exception.ResourceNotFoundException;
import com.nttdata.empleado_ms.mapper.ProjectMapper;
import com.nttdata.empleado_ms.model.dto.ProjectRequestDTO;
import com.nttdata.empleado_ms.model.dto.ProjectResponseDTO;
import com.nttdata.empleado_ms.model.entity.ProjectEntity;
import com.nttdata.empleado_ms.repository.ProjectRepository;
import com.nttdata.empleado_ms.service.impl.ProjectServiceImpl;
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

class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private ProjectEntity entity;
    private ProjectRequestDTO request;
    private ProjectResponseDTO response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = new ProjectEntity(1L, "Proyecto X", "Desc", true);
        request = new ProjectRequestDTO();
        request.setName("Proyecto X");
        request.setDescription("Desc");
        response = new ProjectResponseDTO();
        response.setId(1L);
        response.setName("Proyecto X");
    }

    @Test
    void testCreateSuccess() {
        when(projectRepository.existsByName("Proyecto X")).thenReturn(false);
        when(projectMapper.toEntity(request)).thenReturn(entity);
        when(projectRepository.save(entity)).thenReturn(entity);
        when(projectMapper.toResponse(entity)).thenReturn(response);

        ProjectResponseDTO result = projectService.create(request);

        assertThat(result.getName()).isEqualTo("Proyecto X");
        verify(projectRepository).save(entity);
    }

    @Test
    void testCreateThrowsExceptionWhenNameExists() {
        when(projectRepository.existsByName("Proyecto X")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> projectService.create(request));
    }

    @Test
    void testFindByIdSuccess() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(projectMapper.toResponse(entity)).thenReturn(response);

        ProjectResponseDTO result = projectService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> projectService.findById(1L));
    }

    @Test
    void testFindAll() {
        when(projectRepository.findAll()).thenReturn(List.of(entity));
        when(projectMapper.toResponseList(List.of(entity))).thenReturn(List.of(response));

        List<ProjectResponseDTO> result = projectService.findAll();

        assertThat(result).hasSize(1);
    }
}
