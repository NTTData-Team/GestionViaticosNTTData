package com.nttdata.viatico_ms.client.dto;

public record ProjectResponseDTO(
        Long id,
        String name,
        String description,
        Boolean active
) {}
