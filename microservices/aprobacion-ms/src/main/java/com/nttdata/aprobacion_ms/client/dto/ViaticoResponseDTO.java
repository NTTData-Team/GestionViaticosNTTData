package com.nttdata.aprobacion_ms.client.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ViaticoResponseDTO(
        Long id, Long empleadoId, Long proyectoId, String estado,
        LocalDate fechaInicio, LocalDate fechaFin, String destino,
        BigDecimal montoEstimado, String moneda, String motivo
) {}
