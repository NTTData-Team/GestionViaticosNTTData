package com.nttdata.auth_ms.model.dto;

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
