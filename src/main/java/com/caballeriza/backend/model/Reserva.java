package com.caballeriza.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TipoReserva tipo;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Builder.Default
    private EstadoReserva estado = EstadoReserva.PROGRAMADA;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private LocalTime horaInicio;

    @NotNull
    private LocalTime horaFin;

    @NotBlank
    private String cliente;

    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "caballo_id", nullable = false)
    private Caballo caballo;

    @ManyToOne
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;
}
