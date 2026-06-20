package com.caballeriza.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuministroDTO {

    private Long id;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    @NotBlank(message = "La cantidad es obligatoria")
    private String cantidad;

    @NotNull(message = "El caballo es obligatorio")
    private Long caballoId;

    private String nombreCaballo;
}