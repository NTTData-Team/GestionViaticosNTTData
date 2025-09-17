package com.nttdata.empleado_ms.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeUpdateDTO {
    @Size(max = 20, message = "El código no puede superar 20 caracteres")
    private String codeEmployee;

    @Pattern(regexp = "\\d{8}", message = "El documento debe tener 8 dígitos")
    private String documentIdentity;

    @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
    private String name;

    @Size(max = 50, message = "El apellido no puede superar 50 caracteres")
    private String lastName;

    @Email(message = "El email debe ser válido")
    private String email;

    private Long areaId;

    private Long projectId;

    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener 9 dígitos")
    private String phone;

    @Size(max = 100, message = "La dirección no puede superar 100 caracteres")
    private String address;

    @Size(max = 50, message = "La posición no puede superar 50 caracteres")
    private String position;

}
