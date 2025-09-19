package com.nttdata.viatico_ms.service.impl;

import java.time.OffsetDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import com.nttdata.viatico_ms.client.AprobacionClient;
import com.nttdata.viatico_ms.client.EmpleadoClient;
import com.nttdata.viatico_ms.client.GastoClient;
import com.nttdata.viatico_ms.client.dto.*;
import com.nttdata.viatico_ms.exception.ReferenceNotFoundException;
import com.nttdata.viatico_ms.exception.ViaticoNotFoundException;
import com.nttdata.viatico_ms.model.dto.*;
import com.nttdata.viatico_ms.model.entity.ViaticoEntity;
import com.nttdata.viatico_ms.repository.ViaticoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@org.junit.jupiter.api.extension.ExtendWith(MockitoExtension.class)
class ViaticoServiceImplTest {

    @Mock private ViaticoRepository repo;
    @Mock private EmpleadoClient empleadoClient;
    @Mock private AprobacionClient aprobacionClient;
    @Mock private GastoClient gastoClient;

    @InjectMocks
    private ViaticoServiceImpl service;

    private static ApiResponse<EmployeeResponseDTO> empleado(Long id) {
        return new ApiResponse<>(
                200, "ok",
                new EmployeeResponseDTO(id, 5000L, "EMP-001","Consultor","DNI-12345678",
                        "999888777","Av. Siempre Viva", true, 10L,"Consultoría", 777L,"Proyecto Zeus")
        );
    }
    private static ApiResponse<ProjectResponseDTO> proyecto(Long id) {
        return new ApiResponse<>(
                200, "ok",
                new ProjectResponseDTO(id, "Proyecto Zeus", "Implantación", true)
        );
    }

    @BeforeEach
    void setup() {
    }

    @Test
    void create_sinGastos_iniciaAprobacion_y_queda_EN_APROBACION() {
        when(empleadoClient.getEmployee(5001L)).thenReturn(empleado(5001L));
        when(empleadoClient.getProject(777L)).thenReturn(proyecto(777L));
        when(repo.save(any(ViaticoEntity.class)))
                .thenAnswer(inv -> { // asigna id si viene null
                    ViaticoEntity e = inv.getArgument(0);
                    if (e.getId() == null) e.setId(123L);
                    return e;
                });

        var dto = new ViaticoCreateWithGastosDTO(
                5001L, 777L,
                LocalDate.of(2025,10,1), LocalDate.of(2025,10,5),
                "AREQUIPA", new BigDecimal("2000.00"), "PEN", "Kickoff",
                null
        );

        // act
        ViaticoResponseDTO out = service.create(dto);

        // assert
        verify(aprobacionClient).create(new AprobacionCreateDTO(123L, 1));
        verifyNoInteractions(gastoClient);
        assertThat(out.id()).isEqualTo(123L);
        assertThat(out.estado()).isEqualTo("EN_APROBACION");
    }

    @Test
    void create_conGastos_haceBatch_en_gasto_ms() {
        when(empleadoClient.getEmployee(5001L)).thenReturn(empleado(5001L));
        when(empleadoClient.getProject(777L)).thenReturn(proyecto(777L));
        when(repo.save(any(ViaticoEntity.class)))
                .thenAnswer(inv -> { ViaticoEntity e = inv.getArgument(0); if (e.getId()==null) e.setId(123L); return e; });

        var dto = new ViaticoCreateWithGastosDTO(
                5001L, 777L,
                LocalDate.of(2025,10,1), LocalDate.of(2025,10,5),
                "AREQUIPA", new BigDecimal("2000.00"), "PEN", "Kickoff",
                List.of(
                        new ViaticoGastoItemDTO("TRANSPORTE", LocalDate.of(2025,10,1), new BigDecimal("250.00"), "PEN", "Taxi", null),
                        new ViaticoGastoItemDTO("ALIMENTACION", LocalDate.of(2025,10,2), new BigDecimal("80.00"), "PEN", "Almuerzo", null)
                )
        );

        var out = service.create(dto);

        ArgumentCaptor<GastoBatchCreateDTO> cap = ArgumentCaptor.forClass(GastoBatchCreateDTO.class);
        verify(gastoClient).createBatch(eq(123L), cap.capture());
        var items = cap.getValue().items();
        assertThat(items).hasSize(2);
        assertThat(items.get(0).categoria()).isEqualTo("TRANSPORTE");
        assertThat(out.estado()).isEqualTo("EN_APROBACION");
    }

    @Test
    void create_conGastos_batchFalla_noRompe() {
        when(empleadoClient.getEmployee(5001L)).thenReturn(empleado(5001L));
        when(empleadoClient.getProject(777L)).thenReturn(proyecto(777L));
        when(repo.save(any(ViaticoEntity.class)))
                .thenAnswer(inv -> { ViaticoEntity e = inv.getArgument(0); if (e.getId()==null) e.setId(123L); return e; });
        doThrow(new RuntimeException("timeout gasto-ms"))
                .when(gastoClient).createBatch(anyLong(), any(GastoBatchCreateDTO.class));

        var dto = new ViaticoCreateWithGastosDTO(
                5001L, 777L,
                LocalDate.of(2025,10,1), LocalDate.of(2025,10,5),
                "AREQUIPA", new BigDecimal("2000.00"), "PEN", "Kickoff",
                List.of(new ViaticoGastoItemDTO("TRANSPORTE", LocalDate.of(2025,10,1), new BigDecimal("250.00"), "PEN", "Taxi", null))
        );

        var out = service.create(dto);
        assertThat(out.estado()).isEqualTo("EN_APROBACION");
    }

    @Test
    void create_empleadoNullData_lanzaReferenceNotFound() {
        when(empleadoClient.getEmployee(5001L)).thenReturn(new ApiResponse<>(200,"ok", null));

        var dto = new ViaticoCreateWithGastosDTO(
                5001L, 777L, LocalDate.now(), LocalDate.now().plusDays(1),
                "CUSCO", new BigDecimal("100.00"), "PEN", "Visita", null
        );

        assertThrows(ReferenceNotFoundException.class, () -> service.create(dto));
        verifyNoInteractions(aprobacionClient, gastoClient);
        verify(repo, never()).save(any());
    }

    @Test
    void create_proyectoNullData_lanzaReferenceNotFound() {
        when(empleadoClient.getEmployee(5001L)).thenReturn(empleado(5001L));
        when(empleadoClient.getProject(777L)).thenReturn(new ApiResponse<>(200,"ok", null));

        var dto = new ViaticoCreateWithGastosDTO(
                5001L, 777L, LocalDate.now(), LocalDate.now().plusDays(1),
                "CUSCO", new BigDecimal("100.00"), "PEN", "Visita", null
        );

        assertThrows(ReferenceNotFoundException.class, () -> service.create(dto));
        verifyNoInteractions(aprobacionClient, gastoClient);
        verify(repo, never()).save(any());
    }

    @Test
    void get_ok() {
        var e = new ViaticoEntity();
        e.setId(321L);
        e.setEmpleadoId(5001L);
        e.setProyectoId(777L);
        e.setDestino("CHACHAPOYAS");
        e.setFechaInicio(LocalDate.of(2025,9,20));
        e.setFechaFin(LocalDate.of(2025,9,25));
        e.setMoneda("PEN");
        e.setMontoEstimado(new BigDecimal("1500.00"));
        e.setEstado(ViaticoEntity.Estado.CREADO);
        when(repo.findById(321L)).thenReturn(Optional.of(e));

        var out = service.get(321L);
        assertThat(out.id()).isEqualTo(321L);
        assertThat(out.destino()).isEqualTo("CHACHAPOYAS");
        assertThat(out.estado()).isEqualTo("CREADO");
    }

    @Test
    void get_notFound() {
        when(repo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ViaticoNotFoundException.class, () -> service.get(999L));
    }

    @Test
    void updateEstado_ok() {
        var e = new ViaticoEntity();
        e.setId(123L);
        e.setEstado(ViaticoEntity.Estado.EN_APROBACION);

        when(repo.findById(123L)).thenReturn(Optional.of(e));
        when(repo.save(any(ViaticoEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        var out = service.updateEstado(123L, "APROBADO");
        assertThat(out.estado()).isEqualTo("APROBADO");
        verify(repo).save(any(ViaticoEntity.class));
    }

    @Test
    void updateEstado_invalido_throwIllegalArgument() {
        var e = new ViaticoEntity();
        e.setId(123L);
        e.setEstado(ViaticoEntity.Estado.EN_APROBACION);
        when(repo.findById(123L)).thenReturn(Optional.of(e));

        assertThrows(IllegalArgumentException.class, () -> service.updateEstado(123L, "EN_REVISION"));
        verify(repo, never()).save(any());
    }
    @Test
    void dto_smoke_coverage_for_records() {
        var now = OffsetDateTime.now();
        var upd = now.plusMinutes(5);
        var dec = now.plusMinutes(10);

        var apr = new com.nttdata.viatico_ms.client.dto.AprobacionResponseDTO(
                10L, 99L, "PENDIENTE", 1, 500L, "Obs", now, upd, dec
        );
        assertThat(apr.id()).isEqualTo(10L);
        assertThat(apr.viaticoId()).isEqualTo(99L);
        assertThat(apr.estado()).isEqualTo("PENDIENTE");
        assertThat(apr.toString()).contains("AprobacionResponseDTO");

        var g = new com.nttdata.viatico_ms.client.dto.GastoResponseDTO(
                7L, 99L, "TRANSPORTE", LocalDate.of(2025,10,1),
                new BigDecimal("250.00"), "PEN", "Taxi", null
        );
        assertThat(g.id()).isEqualTo(7L);
        assertThat(g.categoria()).isEqualTo("TRANSPORTE");
        assertThat(g.monto()).isEqualByComparingTo("250.00");
        assertThat(g.toString()).contains("GastoResponseDTO");
    }

}
