package com.nttdata.aprobacion_ms.model.mapper;

import com.nttdata.aprobacion_ms.model.dto.AprobacionCreateDTO;
import com.nttdata.aprobacion_ms.model.dto.AprobacionResponseDTO;
import com.nttdata.aprobacion_ms.model.entity.AprobacionEntity;

public final class AprobacionMapper {
    private AprobacionMapper(){}

    public static AprobacionEntity toEntity(AprobacionCreateDTO dto, AprobacionEntity target){
        target.setViaticoId(dto.viaticoId());
        if (dto.nivel() != null) target.setNivel(dto.nivel());
        target.setEstado(AprobacionEntity.Estado.PENDIENTE);
        target.setComentario(null);
        target.setAprobadorId(null);
        target.setDecididoEn(null);
        return target;
    }

    public static AprobacionResponseDTO toDto(AprobacionEntity e){
        return new AprobacionResponseDTO(
                e.getId(),
                e.getViaticoId(),
                e.getEstado() != null ? e.getEstado().name() : null,
                e.getNivel(),
                e.getAprobadorId(),
                e.getComentario(),
                e.getCreadoEn(),
                e.getActualizadoEn(),
                e.getDecididoEn()
        );
    }
}
