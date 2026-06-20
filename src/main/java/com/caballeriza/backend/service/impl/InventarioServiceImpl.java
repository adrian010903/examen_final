package com.caballeriza.backend.service.impl;

import com.caballeriza.backend.model.Inventario;
import com.caballeriza.backend.repository.InventarioRepository;
import com.caballeriza.backend.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;

    @Override
    public List<Inventario> listar() {
        return inventarioRepository.findAll();
    }

    @Override
    public List<Inventario> listarStockBajo() {
        return inventarioRepository.findAll()
                .stream()
                .filter(Inventario::getStockBajo)
                .toList();
    }

    @Override
    public Inventario obtenerPorId(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto de inventario no encontrado"));
    }

    @Override
    public Inventario guardar(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    @Override
    public Inventario actualizar(Long id, Inventario datos) {
        Inventario inventario = obtenerPorId(id);

        inventario.setNombre(datos.getNombre());
        inventario.setTipo(datos.getTipo());
        inventario.setCantidad(datos.getCantidad());
        inventario.setStockMinimo(datos.getStockMinimo());
        inventario.setUnidad(datos.getUnidad());

        return inventarioRepository.save(inventario);
    }

    @Override
    public void eliminar(Long id) {
        Inventario inventario = obtenerPorId(id);
        inventarioRepository.delete(inventario);
    }
}