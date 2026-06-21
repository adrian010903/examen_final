package com.caballeriza.backend.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertaDTO {

    private String tipo;
    private String severidad;
    private String titulo;
    private String descripcion;
    private LocalDate fecha;
    private Long referenciaId;
}
