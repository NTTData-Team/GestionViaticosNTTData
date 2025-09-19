package com.nttdata.aprobacion_ms.service.impl;

import com.nttdata.aprobacion_ms.client.ViaticoClient;
import com.nttdata.aprobacion_ms.client.dto.ViaticoEstadoUpdateDTO;
import com.nttdata.aprobacion_ms.exception.AprobacionNotFoundException;
import com.nttdata.aprobacion_ms.exception.DuplicateApprovalException;
import com.nttdata.aprobacion_ms.exception.InvalidDecisionException;
import com.nttdata.aprobacion_ms.model.dto.*;
import com.nttdata.aprobacion_ms.model.entity.AprobacionEntity;
import com.nttdata.aprobacion_ms.model.mapper.AprobacionMapper;
import com.nttdata.aprobacion_ms.repository.AprobacionRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AprobacionServiceImplTest {

    @Mock
    private AprobacionRepository repo;

    @Mock
    private ViaticoClient viaticoClient;

    private AprobacionServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new AprobacionServiceImpl(repo, viaticoClient);
    }

    private AprobacionResponseDTO buildResponse(Long id, Long viaticoId, String estado, Long aprobadorId, String comentario) {
        return new AprobacionResponseDTO(
                id,
                viaticoId,
                estado,
                1,
                aprobadorId,
                comentario,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
    }

    @Test
    void create_ok() {
        var dto = new AprobacionCreateDTO(10L, 1);
        var entity = new AprobacionEntity();
        var saved = new AprobacionEntity();
        saved.setId(100L);

        var response = buildResponse(100L, 10L, "PENDIENTE", null, null);

        when(repo.existsByViaticoIdAndNivel(10L, 1)).thenReturn(false);

        try (MockedStatic<AprobacionMapper> ms = mockStatic(AprobacionMapper.class)) {
            ms.when(() -> AprobacionMapper.toEntity(eq(dto), any(AprobacionEntity.class))).thenReturn(entity);
            when(repo.save(entity)).thenReturn(saved);
            ms.when(() -> AprobacionMapper.toDto(saved)).thenReturn(response);

            var result = service.create(dto);

            assertEquals(100L, result.id());
            verify(repo).save(entity);
        }
    }

    @Test
    void create_throws_whenDuplicate() {
        var dto = new AprobacionCreateDTO(10L, 1);
        when(repo.existsByViaticoIdAndNivel(10L, 1)).thenReturn(true);
        assertThrows(DuplicateApprovalException.class, () -> service.create(dto));
    }

    @Test
    void get_ok() {
        var entity = new AprobacionEntity();
        var response = buildResponse(5L, 20L, "PENDIENTE", null, null);

        when(repo.findById(5L)).thenReturn(Optional.of(entity));

        try (MockedStatic<AprobacionMapper> ms = mockStatic(AprobacionMapper.class)) {
            ms.when(() -> AprobacionMapper.toDto(entity)).thenReturn(response);

            var result = service.get(5L);

            assertEquals(5L, result.id());
        }
    }

    @Test
    void get_notFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(AprobacionNotFoundException.class, () -> service.get(1L));
    }

    @Test
    void listByViatico_ok() {
        var entity = new AprobacionEntity();
        var response = buildResponse(1L, 50L, "PENDIENTE", null, null);

        when(repo.findByViaticoId(50L)).thenReturn(List.of(entity));

        try (MockedStatic<AprobacionMapper> ms = mockStatic(AprobacionMapper.class)) {
            ms.when(() -> AprobacionMapper.toDto(entity)).thenReturn(response);

            var result = service.listByViatico(50L);

            assertEquals(1, result.size());
            assertEquals(50L, result.get(0).viaticoId());
        }
    }

    @Test
    void approve_ok() {
        var entity = new AprobacionEntity();
        entity.setId(10L);
        entity.setViaticoId(60L);
        entity.setEstado(AprobacionEntity.Estado.PENDIENTE);

        var decision = new AprobacionDecisionDTO(99L, "ok");
        var response = buildResponse(10L, 60L, "APROBADO", 99L, "ok");

        when(repo.findById(10L)).thenReturn(Optional.of(entity));
        when(repo.save(any())).thenReturn(entity);

        try (MockedStatic<AprobacionMapper> ms = mockStatic(AprobacionMapper.class)) {
            ms.when(() -> AprobacionMapper.toDto(any())).thenReturn(response);

            var result = service.approve(10L, decision);

            assertEquals("APROBADO", result.estado());
            verify(viaticoClient).updateEstado(eq(60L), any(ViaticoEstadoUpdateDTO.class));
        }
    }

    @Test
    void approve_throws_ifNotPending() {
        var entity = new AprobacionEntity();
        entity.setEstado(AprobacionEntity.Estado.APROBADO);
        when(repo.findById(1L)).thenReturn(Optional.of(entity));

        assertThrows(InvalidDecisionException.class, () -> service.approve(1L, new AprobacionDecisionDTO(5L, "x")));
    }

    @Test
    void reject_ok() {
        var entity = new AprobacionEntity();
        entity.setId(11L);
        entity.setViaticoId(70L);
        entity.setEstado(AprobacionEntity.Estado.PENDIENTE);

        var decision = new AprobacionDecisionDTO(77L, "rechazado");
        var response = buildResponse(11L, 70L, "RECHAZADO", 77L, "rechazado");

        when(repo.findById(11L)).thenReturn(Optional.of(entity));
        when(repo.save(any())).thenReturn(entity);

        try (MockedStatic<AprobacionMapper> ms = mockStatic(AprobacionMapper.class)) {
            ms.when(() -> AprobacionMapper.toDto(any())).thenReturn(response);

            var result = service.reject(11L, decision);

            assertEquals("RECHAZADO", result.estado());
            verify(viaticoClient).updateEstado(eq(70L), any(ViaticoEstadoUpdateDTO.class));
        }
    }

    @Test
    void reject_throws_ifNotPending() {
        var entity = new AprobacionEntity();
        entity.setEstado(AprobacionEntity.Estado.APROBADO);
        when(repo.findById(2L)).thenReturn(Optional.of(entity));

        assertThrows(InvalidDecisionException.class, () -> service.reject(2L, new AprobacionDecisionDTO(1L, "x")));
    }
}
