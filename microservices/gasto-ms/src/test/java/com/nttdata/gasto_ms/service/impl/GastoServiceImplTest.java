package com.nttdata.gasto_ms.service.impl;

import com.nttdata.gasto_ms.client.ViaticoClient;
import com.nttdata.gasto_ms.client.dto.ViaticoResponseDTO;
import com.nttdata.gasto_ms.exception.BusinessRuleException;
import com.nttdata.gasto_ms.exception.GastoNotFoundException;
import com.nttdata.gasto_ms.model.dto.*;
import com.nttdata.gasto_ms.model.entity.GastoEntity;
import com.nttdata.gasto_ms.repository.GastoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GastoServiceImplTest {

    @Mock private GastoRepository repo;
    @Mock private ViaticoClient viaticoClient;

    @InjectMocks
    private GastoServiceImpl service;

    private static ViaticoResponseDTO viatico(LocalDate ini, LocalDate fin, String estado) {
        return new ViaticoResponseDTO(
                777L, 5001L, 888L, estado,
                ini, fin, "CUSCO", new BigDecimal("1200.00"), "PEN", "Viaje de trabajo"
        );
    }

    // ---------- CREATE ----------
    @Test
    void create_ok_guarda_y_mapea() {
        var ini = LocalDate.of(2025, 10, 1);
        var fin = LocalDate.of(2025, 10, 5);
        when(viaticoClient.get(777L)).thenReturn(viatico(ini, fin, "EN_APROBACION"));

        when(repo.save(any(GastoEntity.class)))
                .thenAnswer(inv -> { var e = (GastoEntity) inv.getArgument(0); e.setId(1L); return e; });

        var dto = new GastoCreateDTO(
                777L, "TRANSPORTE", LocalDate.of(2025,10,2),
                new BigDecimal("50.00"), "PEN", "Taxi hotel", null
        );

        var out = service.create(dto);

        assertThat(out.id()).isEqualTo(1L);
        assertThat(out.categoria()).isEqualTo("TRANSPORTE");
        verify(repo).save(any(GastoEntity.class));
    }

    @Test
    void create_viaticoRechazado_o_Aprobado_reglaNegocio() {
        var ini = LocalDate.of(2025, 10, 1);
        var fin = LocalDate.of(2025, 10, 5);
        when(viaticoClient.get(777L)).thenReturn(viatico(ini, fin, "RECHAZADO"));

        var dto = new GastoCreateDTO(777L, "TRANSPORTE", LocalDate.of(2025,10,2),
                new BigDecimal("50.00"), "PEN", "Taxi", null);

        assertThrows(BusinessRuleException.class, () -> service.create(dto));
        verify(repo, never()).save(any());

        when(viaticoClient.get(777L)).thenReturn(viatico(ini, fin, "APROBADO"));
        assertThrows(BusinessRuleException.class, () -> service.create(dto));
    }

    @Test
    void create_fecha_fuera_de_rango_reglaNegocio() {
        var ini = LocalDate.of(2025, 10, 1);
        var fin = LocalDate.of(2025, 10, 5);
        when(viaticoClient.get(777L)).thenReturn(viatico(ini, fin, "EN_APROBACION"));

        var dto = new GastoCreateDTO(777L, "ALIMENTACION", LocalDate.of(2025,9,30),
                new BigDecimal("30.00"), "PEN", "Desayuno", null);

        assertThrows(BusinessRuleException.class, () -> service.create(dto));
        verify(repo, never()).save(any());
    }

    // ---------- CREATE BATCH ----------
    @Test
    void createBatch_ok_guarda_lista() {
        var ini = LocalDate.of(2025, 10, 1);
        var fin = LocalDate.of(2025, 10, 5);
        when(viaticoClient.get(777L)).thenReturn(viatico(ini, fin, "EN_APROBACION"));

        when(repo.saveAll(anyList())).thenAnswer(inv -> {
            List<GastoEntity> list = inv.getArgument(0);
            long[] seq = {100L};
            list.forEach(e -> { if (e.getId() == null) e.setId(seq[0]++); });
            return list;
        });

        var items = List.of(
                new GastoItemCreateDTO("TRANSPORTE", LocalDate.of(2025,10,1), new BigDecimal("20.00"), "PEN", "Metro", null),
                new GastoItemCreateDTO("ALIMENTACION", LocalDate.of(2025,10,2), new BigDecimal("35.50"), "PEN", "Almuerzo", null)
        );

        var out = service.createBatch(777L, items);

        assertThat(out).hasSize(2);
        assertThat(out.get(0).categoria()).isEqualTo("TRANSPORTE");
        verify(repo).saveAll(anyList());
    }

    @Test
    void createBatch_viaticoRechazado_reglaNegocio() {
        var ini = LocalDate.of(2025, 10, 1);
        var fin = LocalDate.of(2025, 10, 5);
        when(viaticoClient.get(777L)).thenReturn(viatico(ini, fin, "RECHAZADO"));

        var items = List.of(
                new GastoItemCreateDTO("TRANSPORTE", LocalDate.of(2025,10,1), new BigDecimal("20.00"), "PEN", "Bus", null)
        );

        assertThrows(BusinessRuleException.class, () -> service.createBatch(777L, items));
        verify(repo, never()).saveAll(anyList());
    }

    @Test
    void createBatch_item_fuera_de_rango_reglaNegocio() {
        var ini = LocalDate.of(2025, 10, 1);
        var fin = LocalDate.of(2025, 10, 5);
        when(viaticoClient.get(777L)).thenReturn(viatico(ini, fin, "EN_APROBACION"));

        var items = List.of(
                new GastoItemCreateDTO("TRANSPORTE", LocalDate.of(2025, 9, 30), new BigDecimal("20.00"), "PEN", "Bus", null)
        );

        assertThrows(BusinessRuleException.class, () -> service.createBatch(777L, items));
        verify(repo, never()).saveAll(anyList());
    }

    // ---------- GET / LIST ----------
    @Test
    void get_ok_mapea() {
        var e = new GastoEntity();
        e.setId(11L); e.setViaticoId(777L);
        e.setCategoria(GastoEntity.Categoria.ALIMENTACION);
        e.setFecha(LocalDate.of(2025,10,3));
        e.setMonto(new BigDecimal("18.00")); e.setMoneda("PEN");
        e.setDescripcion("Snack");

        when(repo.findById(11L)).thenReturn(Optional.of(e));

        var out = service.get(11L);

        assertThat(out.id()).isEqualTo(11L);
        assertThat(out.categoria()).isEqualTo("ALIMENTACION");
    }

    @Test
    void get_notFound_lanza_excepcion() {
        when(repo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(GastoNotFoundException.class, () -> service.get(999L));
    }

    @Test
    void listByViatico_ok() {
        var e1 = new GastoEntity(); e1.setId(1L); e1.setViaticoId(777L);
        e1.setCategoria(GastoEntity.Categoria.TRANSPORTE); e1.setFecha(LocalDate.of(2025,10,1));
        e1.setMonto(new BigDecimal("15.00")); e1.setMoneda("PEN");

        var e2 = new GastoEntity(); e2.setId(2L); e2.setViaticoId(777L);
        e2.setCategoria(GastoEntity.Categoria.ALIMENTACION); e2.setFecha(LocalDate.of(2025,10,2));
        e2.setMonto(new BigDecimal("25.00")); e2.setMoneda("PEN");

        when(repo.findByViaticoId(777L)).thenReturn(List.of(e1, e2));

        var out = service.listByViatico(777L);

        assertThat(out).hasSize(2);
        assertThat(out.get(0).categoria()).isEqualTo("TRANSPORTE");
    }

    // ---------- UPDATE ----------
    @Test
    void update_ok_mapea_y_guarda() {
        var ini = LocalDate.of(2025,10,1);
        var fin = LocalDate.of(2025,10,5);

        var e = new GastoEntity();
        e.setId(33L); e.setViaticoId(777L);
        e.setCategoria(GastoEntity.Categoria.ALIMENTACION);
        e.setFecha(LocalDate.of(2025,10,2));
        e.setMonto(new BigDecimal("30.00")); e.setMoneda("PEN");

        when(repo.findById(33L)).thenReturn(Optional.of(e));
        when(viaticoClient.get(777L)).thenReturn(viatico(ini, fin, "EN_APROBACION"));
        when(repo.save(any(GastoEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        var dto = new GastoUpdateDTO(777L, "TRANSPORTE", LocalDate.of(2025,10,3),
                new BigDecimal("40.00"), "PEN", "Taxi", null);

        var out = service.update(33L, dto);

        assertThat(out.categoria()).isEqualTo("TRANSPORTE");
        assertThat(out.monto()).isEqualByComparingTo("40.00");
        verify(repo).save(any(GastoEntity.class));
    }

    @Test
    void update_notFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        var dto = new GastoUpdateDTO(777L, "TRANSPORTE", LocalDate.now(), new BigDecimal("1.00"), "PEN", null, null);
        assertThrows(GastoNotFoundException.class, () -> service.update(99L, dto));
    }

    @Test
    void update_viaticoRechazado_reglaNegocio() {
        var e = new GastoEntity(); e.setId(33L); e.setViaticoId(777L);
        when(repo.findById(33L)).thenReturn(Optional.of(e));
        when(viaticoClient.get(777L)).thenReturn(viatico(LocalDate.now(), LocalDate.now().plusDays(1), "RECHAZADO"));

        var dto = new GastoUpdateDTO(777L, "TRANSPORTE", LocalDate.now(), new BigDecimal("1.00"), "PEN", null, null);

        assertThrows(BusinessRuleException.class, () -> service.update(33L, dto));
        verify(repo, never()).save(any());
    }

    @Test
    void update_fecha_fuera_de_rango_reglaNegocio() {
        var e = new GastoEntity(); e.setId(33L); e.setViaticoId(777L);
        when(repo.findById(33L)).thenReturn(Optional.of(e));

        var ini = LocalDate.of(2025,10,1);
        var fin = LocalDate.of(2025,10,5);
        when(viaticoClient.get(777L)).thenReturn(viatico(ini, fin, "EN_APROBACION"));

        var dto = new GastoUpdateDTO(777L, "TRANSPORTE", LocalDate.of(2025,9,30), new BigDecimal("1.00"), "PEN", null, null);

        assertThrows(BusinessRuleException.class, () -> service.update(33L, dto));
        verify(repo, never()).save(any());
    }

    // ---------- DELETE ----------
    @Test
    void delete_ok_devuelveDTO_y_elimina() {
        var e = new GastoEntity();
        e.setId(44L); e.setViaticoId(777L);
        e.setCategoria(GastoEntity.Categoria.OTROS);
        e.setFecha(LocalDate.of(2025,10,4));
        e.setMonto(new BigDecimal("10.00")); e.setMoneda("PEN");

        when(repo.findById(44L)).thenReturn(Optional.of(e));
        when(viaticoClient.get(777L)).thenReturn(viatico(LocalDate.now(), LocalDate.now().plusDays(2), "EN_APROBACION"));

        var out = service.delete(44L);

        assertThat(out.id()).isEqualTo(44L);
        verify(repo).delete(any(GastoEntity.class));
    }

    @Test
    void delete_notFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(GastoNotFoundException.class, () -> service.delete(1L));
    }

    @Test
    void delete_viaticoRechazado_reglaNegocio() {
        var e = new GastoEntity(); e.setId(44L); e.setViaticoId(777L);
        when(repo.findById(44L)).thenReturn(Optional.of(e));
        when(viaticoClient.get(777L)).thenReturn(viatico(LocalDate.now(), LocalDate.now().plusDays(1), "RECHAZADO"));

        assertThrows(BusinessRuleException.class, () -> service.delete(44L));
        verify(repo, never()).delete(any());
    }

    @Test
    void record_viaticoResponseDTO_smoke() {
        var dto = new ViaticoResponseDTO(
                777L, 5001L, 888L, "EN_APROBACION",
                LocalDate.of(2025,10,1), LocalDate.of(2025,10,5),
                "CUSCO", new BigDecimal("1200.00"), "PEN", "Trabajo"
        );
        assertThat(dto.id()).isEqualTo(777L);
        assertThat(dto.estado()).isEqualTo("EN_APROBACION");
        assertThat(dto.toString()).contains("ViaticoResponseDTO");
    }
}
