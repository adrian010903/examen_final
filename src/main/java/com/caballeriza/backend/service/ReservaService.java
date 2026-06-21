package com.caballeriza.backend.service;

import com.caballeriza.backend.model.Reserva;

import java.time.LocalDate;
import java.util.List;

public interface ReservaService {
    List<Reserva> listar();
    List<Reserva> listarPorRango(LocalDate inicio, LocalDate fin);
    Reserva obtenerPorId(Long id);
    Reserva guardar(Reserva reserva);
    Reserva actualizar(Long id, Reserva reserva);
    Reserva cancelar(Long id);
    void eliminar(Long id);
}
