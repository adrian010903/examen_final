package com.caballeriza.backend.service;

import com.caballeriza.backend.model.Caballo;

import java.util.List;

public interface CaballoService {
    List<Caballo> listar();
    Caballo obtenerPorId(Long id);
    Caballo guardar(Caballo caballo);
    Caballo actualizar(Long id, Caballo caballo);
    void eliminar(Long id);
}