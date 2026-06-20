package com.caballeriza.backend.service;

import com.caballeriza.backend.model.HistorialMedico;

import java.util.List;

public interface HistorialMedicoService {

    List<HistorialMedico> listar();

    List<HistorialMedico> listarPorCaballo(Long caballoId);

    HistorialMedico obtenerPorId(Long id);

    HistorialMedico guardar(HistorialMedico historialMedico);

    HistorialMedico actualizar(Long id, HistorialMedico historialMedico);

    void eliminar(Long id);
}