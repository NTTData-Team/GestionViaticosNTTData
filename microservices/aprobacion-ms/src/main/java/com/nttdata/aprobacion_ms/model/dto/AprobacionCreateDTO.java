package com.nttdata.aprobacion_ms.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AprobacionCreateDTO(
        @NotNull Long viaticoId, @Min(1) Integer nivel
) {}
