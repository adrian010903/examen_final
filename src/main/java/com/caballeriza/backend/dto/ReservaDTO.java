package com.caballeriza.backend.dto;

import com.caballeriza.backend.model.EstadoReserva;
import com.caballeriza.backend.model.TipoReserva;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaDTO {

    private Long id;

    @NotNull(message = "El tipo de reserva es obligatorio")
    private TipoReserva tipo;

    private EstadoReserva estado;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    @NotBlank(message = "El cliente es obligatorio")
    private String cliente;

    private String observaciones;

    @NotNull(message = "El caballo es obligatorio")
    private Long caballoId;

    private String nombreCaballo;

    private Long empleadoId;

    private String nombreEmpleado;
}
