package com.nttdata.empleado_ms.repository;

import com.nttdata.empleado_ms.model.entity.AreaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AreaRepositoryMockTest {

    @Mock
    private AreaRepository areaRepository;

    private AreaEntity area;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        area = new AreaEntity();
        area.setId(1L);
        area.setName("TI");
    }

    @Test
    void testExistsByName() {
        when(areaRepository.existsByName("TI")).thenReturn(true);

        boolean exists = areaRepository.existsByName("TI");

        assertThat(exists).isTrue();
        verify(areaRepository).existsByName("TI");
    }

    @Test
    void testFindById() {
        when(areaRepository.findById(1L)).thenReturn(Optional.of(area));

        Optional<AreaEntity> result = areaRepository.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("TI");
        verify(areaRepository).findById(1L);
    }

    @Test
    void testFindAll() {
        when(areaRepository.findAll()).thenReturn(List.of(area));

        List<AreaEntity> list = areaRepository.findAll();

        assertThat(list).isNotNull();
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getName()).isEqualTo("TI");
        verify(areaRepository).findAll();
    }
}