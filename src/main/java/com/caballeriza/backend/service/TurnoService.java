package com.caballeriza.backend.service;

import com.caballeriza.backend.model.Turno;

import java.util.List;

public interface TurnoService {

    List<Turno> listar();

    List<Turno> listarPorEmpleado(Long empleadoId);

    Turno obtenerPorId(Long id);

    Turno guardar(Turno turno);

    Turno actualizar(Long id, Turno turno);

    void eliminar(Long id);
}