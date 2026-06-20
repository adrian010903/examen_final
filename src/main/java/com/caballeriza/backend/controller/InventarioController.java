package com.caballeriza.backend.controller;

import com.caballeriza.backend.dto.InventarioDTO;
import com.caballeriza.backend.mapper.CaballerizaMapper;
import com.caballeriza.backend.model.Inventario;
import com.caballeriza.backend.service.InventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping
    public List<InventarioDTO> listar() {
        return inventarioService.listar()
                .stream()
                .map(CaballerizaMapper::toInventarioDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public InventarioDTO obtenerPorId(@PathVariable Long id) {
        Inventario inventario = inventarioService.obtenerPorId(id);
        return CaballerizaMapper.toInventarioDTO(inventario);
    }

    @GetMapping("/stock-bajo")
    public List<InventarioDTO> listarStockBajo() {
        return inventarioService.listarStockBajo()
                .stream()
                .map(CaballerizaMapper::toInventarioDTO)
                .toList();
    }

    @PostMapping
    public InventarioDTO guardar(@Valid @RequestBody InventarioDTO inventarioDTO) {
        Inventario inventario = CaballerizaMapper.toInventarioEntity(inventarioDTO);
        Inventario guardado = inventarioService.guardar(inventario);
        return CaballerizaMapper.toInventarioDTO(guardado);
    }

    @PutMapping("/{id}")
    public InventarioDTO actualizar(@PathVariable Long id, @Valid @RequestBody InventarioDTO inventarioDTO) {
        Inventario inventario = CaballerizaMapper.toInventarioEntity(inventarioDTO);
        Inventario actualizado = inventarioService.actualizar(id, inventario);
        return CaballerizaMapper.toInventarioDTO(actualizado);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        inventarioService.eliminar(id);
    }
}