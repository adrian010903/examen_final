package com.caballeriza.backend.dto;

import com.caballeriza.backend.model.RolEmpleado;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El rol es obligatorio")
    private RolEmpleado rol;

    @NotBlank(message = "El contacto es obligatorio")
    private String contacto;
}