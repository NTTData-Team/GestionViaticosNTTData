package com.nttdata.viatico_ms.model.dto;

import jakarta.validation.constraints.NotNull;

public record ViaticoEstadoUpdateDTO(
        @NotNull String estado
) {}
