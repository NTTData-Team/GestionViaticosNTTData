package com.nttdata.viatico_ms.client.dto;

public record EmployeeResponseDTO(
        Long id,
        Long userId,
        String codeEmployee,
        String position,
        String documentIdentity,
        String phone,
        String address,
        Boolean active
) {}
