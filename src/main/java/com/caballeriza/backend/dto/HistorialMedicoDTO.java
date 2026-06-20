package com.caballeriza.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialMedicoDTO {

    private Long id;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    private String alergias;

    private String observaciones;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El responsable es obligatorio")
    private String responsable;

    @NotNull(message = "El caballo es obligatorio")
    private Long caballoId;

    private String nombreCaballo;
}