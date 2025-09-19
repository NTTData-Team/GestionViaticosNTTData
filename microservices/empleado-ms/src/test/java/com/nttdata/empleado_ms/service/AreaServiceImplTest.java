package com.nttdata.empleado_ms.service;


import com.nttdata.empleado_ms.exception.ResourceNotFoundException;
import com.nttdata.empleado_ms.mapper.AreaMapper;
import com.nttdata.empleado_ms.model.dto.AreaRequestDTO;
import com.nttdata.empleado_ms.model.dto.AreaResponseDTO;
import com.nttdata.empleado_ms.model.entity.AreaEntity;
import com.nttdata.empleado_ms.repository.AreaRepository;
import com.nttdata.empleado_ms.service.impl.AreaServiceImpl;
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

class AreaServiceImplTest {

    @Mock
    private AreaRepository areaRepository;
    @Mock
    private AreaMapper areaMapper;

    @InjectMocks
    private AreaServiceImpl areaService;

    private AreaEntity areaEntity;
    private AreaRequestDTO request;
    private AreaResponseDTO response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        areaEntity = new AreaEntity(1L, "Sistemas");
        request = new AreaRequestDTO();
        request.setName("Sistemas");
        response = new AreaResponseDTO();
        response.setId(1L);
        response.setName("Sistemas");
    }

    @Test
    void testCreateSuccess() {
        when(areaRepository.existsByName("Sistemas")).thenReturn(false);
        when(areaMapper.toEntity(request)).thenReturn(areaEntity);
        when(areaRepository.save(areaEntity)).thenReturn(areaEntity);
        when(areaMapper.toResponse(areaEntity)).thenReturn(response);

        AreaResponseDTO result = areaService.create(request);

        assertThat(result.getName()).isEqualTo("Sistemas");
        verify(areaRepository).save(areaEntity);
    }

    @Test
    void testCreateThrowsExceptionWhenNameExists() {
        when(areaRepository.existsByName("Sistemas")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> areaService.create(request));
    }

    @Test
    void testFindByIdSuccess() {
        when(areaRepository.findById(1L)).thenReturn(Optional.of(areaEntity));
        when(areaMapper.toResponse(areaEntity)).thenReturn(response);

        AreaResponseDTO result = areaService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(areaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> areaService.findById(1L));
    }

    @Test
    void testFindAll() {
        when(areaRepository.findAll()).thenReturn(List.of(areaEntity));
        when(areaMapper.toResponseList(List.of(areaEntity))).thenReturn(List.of(response));

        List<AreaResponseDTO> result = areaService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Sistemas");
    }
}
