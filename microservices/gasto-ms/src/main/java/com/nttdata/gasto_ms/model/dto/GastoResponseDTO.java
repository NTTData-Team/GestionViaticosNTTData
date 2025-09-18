package com.nttdata.gasto_ms.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GastoResponseDTO(
        Long id,
        Long viaticoId,
        String categoria,
        LocalDate fecha,
        BigDecimal monto,
        String moneda,
        String descripcion,
        String comprobanteUrl
) {}