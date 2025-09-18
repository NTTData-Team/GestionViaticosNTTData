package com.nttdata.empleado_ms.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
}
