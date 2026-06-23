package com.caballeriza.backend.service;

import com.caballeriza.backend.model.Tarea;

import java.util.List;

public interface TareaService {

    List<Tarea> listar();

    List<Tarea> listarPorEmpleado(Long empleadoId);

    List<Tarea> listarPorEmpleadoContacto(String contacto);

    List<Tarea> listarPorCaballo(Long caballoId);

    Tarea obtenerPorId(Long id);

    Tarea obtenerPorIdParaEmpleado(Long id, String contacto);

    Tarea guardar(Tarea tarea);

    Tarea actualizar(Long id, Tarea tarea);

    Tarea actualizarParaEmpleado(Long id, Tarea tarea, String contacto);

    void eliminar(Long id);
}
