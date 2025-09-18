package com.nttdata.viatico_ms.model.mapper;

import com.nttdata.viatico_ms.model.dto.ViaticoCreateDTO;
import com.nttdata.viatico_ms.model.dto.ViaticoResponseDTO;
import com.nttdata.viatico_ms.model.entity.ViaticoEntity;

public final class ViaticoMapper {
    private ViaticoMapper(){}

    public static ViaticoEntity toEntity(ViaticoCreateDTO d, ViaticoEntity t) {
        t.setEmpleadoId(d.empleadoId());
        t.setProyectoId(d.proyectoId());
        t.setFechaInicio(d.fechaInicio());
        t.setFechaFin(d.fechaFin());
        t.setDestino(d.destino());
        t.setMontoEstimado(d.montoEstimado());
        t.setMoneda(d.moneda() != null ? d.moneda() : "PEN");
        t.setMotivo(d.motivo());
        t.setEstado(ViaticoEntity.Estado.CREADO);
        return t;
    }

    public static ViaticoResponseDTO toDto(ViaticoEntity e) {
        return new ViaticoResponseDTO(
                e.getId(),
                e.getEmpleadoId(),
                e.getProyectoId(),
                e.getEstado() != null ? e.getEstado().name() : null,
                e.getFechaInicio(),
                e.getFechaFin(),
                e.getDestino(),
                e.getMontoEstimado(),
                e.getMoneda(),
                e.getMotivo()
        );
    }
}
