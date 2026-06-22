package com.caballeriza.backend.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertaDTO {

    private Long id;

    private String tipo;

    private String severidad;

    private String titulo;

    private String descripcion;

    private LocalDate fecha;

    private Long referenciaId;

    private Boolean leida;

    private LocalDateTime fechaCreacion;
}