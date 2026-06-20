package com.caballeriza.backend.service;

import com.caballeriza.backend.model.Empleado;

import java.util.List;

public interface EmpleadoService {

    List<Empleado> listar();

    Empleado obtenerPorId(Long id);

    Empleado guardar(Empleado empleado);

    Empleado actualizar(Long id, Empleado empleado);

    void eliminar(Long id);
}