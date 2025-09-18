package com.nttdata.viatico_ms.client.dto;

import java.time.OffsetDateTime;

public record AprobacionResponseDTO(
        Long id,
        Long viaticoId,
        String estado,
        Integer nivel,
        Long aprobadorId,
        String comentario,
        OffsetDateTime creadoEn,
        OffsetDateTime actualizadoEn,
        OffsetDateTime decididoEn
) {}
