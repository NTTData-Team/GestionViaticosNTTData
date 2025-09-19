package com.nttdata.empleado_ms.repository;

import com.nttdata.empleado_ms.model.entity.ProjectEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProjectRepositoryMockTest {

    @Mock
    private ProjectRepository projectRepository;

    private ProjectEntity project;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        project = new ProjectEntity();
        project.setId(1L);
        project.setName("Proyecto A");
        project.setDescription("Desc");
        project.setActive(true);
    }

    @Test
    void testExistsByName() {
        when(projectRepository.existsByName("Proyecto A")).thenReturn(true);

        boolean exists = projectRepository.existsByName("Proyecto A");

        assertThat(exists).isTrue();
        verify(projectRepository).existsByName("Proyecto A");
    }

    @Test
    void testFindById() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Optional<ProjectEntity> result = projectRepository.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Proyecto A");
        verify(projectRepository).findById(1L);
    }

    @Test
    void testFindAll() {
        when(projectRepository.findAll()).thenReturn(List.of(project));

        List<ProjectEntity> list = projectRepository.findAll();

        assertThat(list).isNotNull();
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getName()).isEqualTo("Proyecto A");
        verify(projectRepository).findAll();
    }
}
