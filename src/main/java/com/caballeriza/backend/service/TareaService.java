package com.caballeriza.backend.service;

import com.caballeriza.backend.model.Tarea;

import java.util.List;

public interface TareaService {

    List<Tarea> listar();

    List<Tarea> listarPorEmpleado(Long empleadoId);

    List<Tarea> listarPorCaballo(Long caballoId);

    Tarea obtenerPorId(Long id);

    Tarea guardar(Tarea tarea);

    Tarea actualizar(Long id, Tarea tarea);

    void eliminar(Long id);
}