package com.caballeriza.backend.service;

import com.caballeriza.backend.model.Empleado;

import java.util.List;

public interface EmpleadoService {

    List<Empleado> listar();

    List<Empleado> listarPorContacto(String contacto);

    Empleado obtenerPorId(Long id);

    Empleado obtenerPorIdParaContacto(Long id, String contacto);

    Empleado guardar(Empleado empleado);

    Empleado actualizar(Long id, Empleado empleado);

    void eliminar(Long id);
}
