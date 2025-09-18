package com.nttdata.empleado_ms.model.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmployeeRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @NotNull(message = "El rol es obligatorio")
    private Long roleId;

    @NotBlank(message = "El código de empleado es obligatorio")
    private String codeEmployee;

    @NotBlank(message = "La posición es obligatoria")
    private String position;

    @NotBlank(message = "El documento de identidad del empleado es obligatorio")
    private String documentIdentity;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{7,15}$", message = "El teléfono debe contener entre 7 y 15 dígitos")
    private String phone;

    @NotBlank(message = "La dirección es obligatoria")
    private String address;

    @NotNull
    @Min(value = 1, message = "Tiene que ser un id del projecto válido")
    private Long projectId;

    @NotNull
    @Min(value = 1, message = "Tiene que ser un id del área válido")
    private Long areaId;

}
