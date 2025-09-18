package com.nttdata.viatico_ms.client.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GastoItemCreateDTO(
        String categoria, LocalDate fecha, BigDecimal monto, String moneda,
        String descripcion, String comprobanteUrl
) {}
