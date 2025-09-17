package com.nttdata.empleado_ms.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {
    private Long id;
    private Long userId;
    private String codeEmployee;
    private String position;
    private String documentIdentity;
    private String phone;
    private String address;
    private Boolean active;
    private Long areaId;
    private String areaName;
    private Long projectId;
    private String projectName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
