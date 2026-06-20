package com.caballeriza.backend.service;

import com.caballeriza.backend.model.PlanAlimentacion;

import java.util.List;

public interface PlanAlimentacionService {

    List<PlanAlimentacion> listar();

    List<PlanAlimentacion> listarPorCaballo(Long caballoId);

    PlanAlimentacion obtenerPorId(Long id);

    PlanAlimentacion guardar(PlanAlimentacion planAlimentacion);

    PlanAlimentacion actualizar(Long id, PlanAlimentacion planAlimentacion);

    void eliminar(Long id);
}