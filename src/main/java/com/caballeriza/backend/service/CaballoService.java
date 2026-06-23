package com.caballeriza.backend.service;

import com.caballeriza.backend.model.Caballo;

import java.util.List;

public interface CaballoService {
    List<Caballo> listar();
    List<Caballo> listarPorCliente(String email);
    Caballo obtenerPorId(Long id);
    Caballo obtenerPorIdParaCliente(Long id, String email);
    Caballo guardar(Caballo caballo);
    Caballo guardarParaCliente(Caballo caballo, String email);
    Caballo actualizar(Long id, Caballo caballo);
    Caballo actualizarParaCliente(Long id, Caballo caballo, String email);
    void eliminar(Long id);
    void eliminarParaCliente(Long id, String email);
    void verificarAccesoCliente(Long id, String email);
}
