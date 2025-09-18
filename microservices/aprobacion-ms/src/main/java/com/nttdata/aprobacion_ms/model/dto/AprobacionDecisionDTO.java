package com.nttdata.aprobacion_ms.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AprobacionDecisionDTO(
        @NotNull Long aprobadorId,
        @Size(max = 2000) String comentario
) {}
