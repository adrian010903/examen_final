package com.caballeriza.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TareaDTO {

    private Long id;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    private Boolean completada;

    @NotNull(message = "El empleado es obligatorio")
    private Long empleadoId;

    private String nombreEmpleado;

    private Long caballoId;

    private String nombreCaballo;
}