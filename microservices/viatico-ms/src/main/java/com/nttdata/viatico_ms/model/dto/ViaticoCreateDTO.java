package com.nttdata.viatico_ms.model.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ViaticoCreateDTO(
        @NotNull Long empleadoId,
        @NotNull Long proyectoId,
        @NotNull LocalDate fechaInicio,
        @NotNull LocalDate fechaFin,
        @NotBlank String destino,
        @NotNull @DecimalMin("0.00") BigDecimal montoEstimado,
        @Pattern(regexp = "^[A-Z]{3}$", message = "Moneda debe ser ISO-4217 de 3 letras")
        String moneda,
        String motivo
) {}
