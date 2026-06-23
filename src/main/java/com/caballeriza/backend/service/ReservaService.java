package com.caballeriza.backend.service;

import com.caballeriza.backend.model.EstadoReserva;
import com.caballeriza.backend.model.Reserva;
import com.caballeriza.backend.model.TipoReserva;

import java.time.LocalDate;
import java.util.List;

public interface ReservaService {

    List<Reserva> listar();

    List<Reserva> listarPorCliente(String email);

    List<Reserva> listarPorRango(
            LocalDate inicio,
            LocalDate fin
    );

    List<Reserva> filtrar(
            TipoReserva tipo,
            LocalDate fecha,
            EstadoReserva estado,
            LocalDate inicio,
            LocalDate fin
    );

    Reserva obtenerPorId(Long id);

    Reserva obtenerPorIdParaCliente(Long id, String email);

    Reserva guardar(Reserva reserva);

    Reserva guardarParaCliente(Reserva reserva, String email);

    Reserva actualizar(Long id, Reserva reserva);

    Reserva actualizarParaCliente(Long id, Reserva reserva, String email);

    Reserva cancelar(Long id);

    Reserva cancelarParaCliente(Long id, String email);

    void eliminar(Long id);
}
