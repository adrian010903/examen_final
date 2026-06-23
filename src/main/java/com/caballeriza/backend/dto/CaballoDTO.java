package com.caballeriza.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaballoDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El identificador es obligatorio")
    private String identificador;

    @Min(value = 0, message = "La edad no puede ser negativa")
    private Integer edad;

    @NotBlank(message = "La raza es obligatoria")
    private String raza;

    @NotBlank(message = "El sexo es obligatorio")
    private String sexo;

    @Positive(message = "El peso debe ser mayor a 0")
    private Double peso;

    private String fotoUrl;

    private Long clienteId;

    private String nombreCliente;
}
