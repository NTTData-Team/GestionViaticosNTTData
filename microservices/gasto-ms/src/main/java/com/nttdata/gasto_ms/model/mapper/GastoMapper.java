package com.nttdata.gasto_ms.model.mapper;

import com.nttdata.gasto_ms.model.dto.GastoCreateDTO;
import com.nttdata.gasto_ms.model.dto.GastoItemCreateDTO;
import com.nttdata.gasto_ms.model.dto.GastoResponseDTO;
import com.nttdata.gasto_ms.model.dto.GastoUpdateDTO;
import com.nttdata.gasto_ms.model.entity.GastoEntity;

public final class GastoMapper {
    private GastoMapper(){}

    public static GastoEntity toEntity(GastoCreateDTO dto, GastoEntity target) {
        target.setViaticoId(dto.viaticoId());
        target.setCategoria(parseCategoria(dto.categoria()));
        target.setFecha(dto.fecha());
        target.setMonto(dto.monto());
        target.setMoneda(dto.moneda() != null ? dto.moneda() : "PEN");
        target.setDescripcion(dto.descripcion());
        target.setComprobanteUrl(dto.comprobanteUrl());
        return target;
    }

    public static GastoEntity toEntity(GastoUpdateDTO dto, GastoEntity target) {
        target.setViaticoId(dto.viaticoId());
        target.setCategoria(parseCategoria(dto.categoria()));
        target.setFecha(dto.fecha());
        target.setMonto(dto.monto());
        target.setMoneda(dto.moneda() != null ? dto.moneda() : "PEN");
        target.setDescripcion(dto.descripcion());
        target.setComprobanteUrl(dto.comprobanteUrl());
        return target;
    }
    public static GastoEntity toEntity(Long viaticoId, GastoItemCreateDTO dto, GastoEntity target) {
        target.setViaticoId(viaticoId);
        target.setCategoria(parseCategoria(dto.categoria()));
        target.setFecha(dto.fecha());
        target.setMonto(dto.monto());
        target.setMoneda(dto.moneda() != null ? dto.moneda() : "PEN");
        target.setDescripcion(dto.descripcion());
        target.setComprobanteUrl(dto.comprobanteUrl());
        return target;
    }
    public static GastoResponseDTO toDto(GastoEntity e) {
        return new GastoResponseDTO(
                e.getId(),
                e.getViaticoId(),
                e.getCategoria() != null ? e.getCategoria().name() : null,
                e.getFecha(),
                e.getMonto(),
                e.getMoneda(),
                e.getDescripcion(),
                e.getComprobanteUrl()
        );
    }

    private static GastoEntity.Categoria parseCategoria(String raw){
        if (raw == null) return null;
        try {
            return GastoEntity.Categoria.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Categoria inválida: " + raw + ". Permitidas: TRANSPORTE, ALOJAMIENTO, ALIMENTACION, OTROS");
        }
    }

}
