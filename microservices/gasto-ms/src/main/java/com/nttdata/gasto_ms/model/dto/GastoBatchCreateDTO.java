package com.nttdata.gasto_ms.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record GastoBatchCreateDTO(
        @NotEmpty List<@Valid GastoItemCreateDTO> items
) {}
