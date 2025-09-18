package com.nttdata.auth_ms.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    @Email(message = "Introduce un formato válido")
    @NotBlank(message = "No debe ser nulo o en blanco")
    private String email;

    @NotBlank(message = "No debe ser vacio")
    private String password;
}