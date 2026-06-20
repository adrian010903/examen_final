package com.caballeriza.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanAlimentacionDTO {

    private Long id;

    @NotBlank(message = "El tipo de alimento es obligatorio")
    private String tipoAlimento;

    @NotBlank(message = "La cantidad es obligatoria")
    private String cantidad;

    @NotBlank(message = "La frecuencia es obligatoria")
    private String frecuencia;

    private String observaciones;

    @NotNull(message = "El caballo es obligatorio")
    private Long caballoId;

    private String nombreCaballo;
}