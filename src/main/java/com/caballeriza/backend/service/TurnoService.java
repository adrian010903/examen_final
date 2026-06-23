package com.caballeriza.backend.service;

import com.caballeriza.backend.model.Turno;

import java.util.List;

public interface TurnoService {

    List<Turno> listar();

    List<Turno> listarPorEmpleado(Long empleadoId);

    List<Turno> listarPorEmpleadoContacto(String contacto);

    Turno obtenerPorId(Long id);

    Turno obtenerPorIdParaEmpleado(Long id, String contacto);

    Turno guardar(Turno turno);

    Turno actualizar(Long id, Turno turno);

    void eliminar(Long id);
}
