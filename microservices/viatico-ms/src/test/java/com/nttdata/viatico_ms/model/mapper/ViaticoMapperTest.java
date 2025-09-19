package com.nttdata.viatico_ms.model.mapper;

import com.nttdata.viatico_ms.model.dto.ViaticoCreateDTO;
import com.nttdata.viatico_ms.model.dto.ViaticoResponseDTO;
import com.nttdata.viatico_ms.model.entity.ViaticoEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ViaticoMapperTest {

    @Test
    void toEntity_asigna_defaults_y_campos() {
        var dto = new ViaticoCreateDTO(
                5001L, 777L,
                LocalDate.of(2025,10,1), LocalDate.of(2025,10,5),
                "AREQUIPA", new BigDecimal("2000.00"),
                null, // moneda null => debe ir "PEN" por defecto en entity
                "Kickoff"
        );
        var e = ViaticoMapper.toEntity(dto, new ViaticoEntity());

        assertThat(e.getEmpleadoId()).isEqualTo(5001L);
        assertThat(e.getProyectoId()).isEqualTo(777L);
        assertThat(e.getDestino()).isEqualTo("AREQUIPA");
        assertThat(e.getMoneda()).isEqualTo("PEN");
        assertThat(e.getEstado()).isEqualTo(ViaticoEntity.Estado.CREADO);
    }

    @Test
    void toDto_ok() {
        var e = new ViaticoEntity();
        e.setId(123L);
        e.setEmpleadoId(5001L);
        e.setProyectoId(777L);
        e.setFechaInicio(LocalDate.of(2025,10,1));
        e.setFechaFin(LocalDate.of(2025,10,5));
        e.setDestino("AREQUIPA");
        e.setMontoEstimado(new BigDecimal("2000.00"));
        e.setMoneda("PEN");
        e.setMotivo("Kickoff");
        e.setEstado(ViaticoEntity.Estado.EN_APROBACION);

        ViaticoResponseDTO out = ViaticoMapper.toDto(e);

        assertThat(out.id()).isEqualTo(123L);
        assertThat(out.estado()).isEqualTo("EN_APROBACION");
        assertThat(out.moneda()).isEqualTo("PEN");
    }
}
