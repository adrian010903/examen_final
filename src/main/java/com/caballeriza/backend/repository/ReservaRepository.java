package com.caballeriza.backend.repository;

import com.caballeriza.backend.model.EstadoReserva;
import com.caballeriza.backend.model.Reserva;
import com.caballeriza.backend.model.TipoReserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByFechaBetween(
            LocalDate inicio,
            LocalDate fin
    );

    List<Reserva> findByEstado(
            EstadoReserva estado
    );

    List<Reserva> findByClienteUsuarioEmail(String email);

    List<Reserva> findByTipoAndFechaAndHoraInicioAndHoraFinAndEstadoNot(
            TipoReserva tipo,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin,
            EstadoReserva estado
    );
}
