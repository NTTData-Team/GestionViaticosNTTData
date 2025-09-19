package com.nttdata.viatico_ms.client.dto;

import java.time.LocalDateTime;

public record EmployeeResponseDTO(
        Long id,
        Long userId,
        String codeEmployee,
        String position,
        String documentIdentity,
        String phone,
        String address,
        Boolean active,
        Long areaId,
        String areaName,
        Long projectId,
        String projectName
) {}
