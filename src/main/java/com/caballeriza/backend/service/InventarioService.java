package com.caballeriza.backend.service;

import com.caballeriza.backend.model.Inventario;

import java.util.List;

public interface InventarioService {

    List<Inventario> listar();

    List<Inventario> listarStockBajo();

    Inventario obtenerPorId(Long id);

    Inventario guardar(Inventario inventario);

    Inventario actualizar(Long id, Inventario inventario);

    void eliminar(Long id);
}