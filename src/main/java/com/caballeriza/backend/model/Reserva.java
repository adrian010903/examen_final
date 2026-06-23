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

    @Min(value = 1, message = "La cantidad de personas debe ser mayor a cero")
    @Column(name = "cantidad_personas")
    @Builder.Default
    private Integer cantidadPersonas = 1;

    @Min(value = 1, message = "El cupo máximo debe ser mayor a cero")
    @Column(name = "cupo_maximo")
    private Integer cupoMaximo;

    @ManyToOne
    @JoinColumn(name = "caballo_id", nullable = false)
    private Caballo caballo;

    @ManyToOne
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name = "cliente_usuario_id")
    private User clienteUsuario;
}
