package com.nttdata.gasto_ms.client.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ViaticoResponseDTO(
        Long id, Long empleadoId, Long proyectoId, String estado,
        java.time.LocalDate fechaInicio, java.time.LocalDate fechaFin,
        String destino, java.math.BigDecimal montoEstimado, String moneda, String motivo
) {}