package com.nttdata.gasto_ms.model.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record GastoItemCreateDTO(
        @NotBlank String categoria,
        @NotNull LocalDate fecha,
        @NotNull @DecimalMin("0.00") BigDecimal monto,
        @Pattern(regexp = "^[A-Z]{3}$", message = "Moneda debe ser ISO-4217 de 3 letras")
        String moneda,
        String descripcion,
        String comprobanteUrl
) {}
