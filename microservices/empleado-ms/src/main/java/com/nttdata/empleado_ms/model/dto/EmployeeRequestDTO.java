package com.nttdata.empleado_ms.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmployeeRequestDTO {
    @NotNull
    @Min(value = 1, message = "El id del usuario tiene que ser válido")
    private Long userId;

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
