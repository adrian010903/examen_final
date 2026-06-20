package com.caballeriza.backend.controller;

import com.caballeriza.backend.dto.CaballoDTO;
import com.caballeriza.backend.mapper.CaballerizaMapper;
import com.caballeriza.backend.model.Caballo;
import com.caballeriza.backend.service.CaballoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/caballos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CaballoController {

    private final CaballoService caballoService;

    @GetMapping
    public List<CaballoDTO> listar() {
        return caballoService.listar()
                .stream()
                .map(CaballerizaMapper::toCaballoDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public CaballoDTO obtenerPorId(@PathVariable Long id) {
        Caballo caballo = caballoService.obtenerPorId(id);
        return CaballerizaMapper.toCaballoDTO(caballo);
    }

    @PostMapping
    public CaballoDTO guardar(@Valid @RequestBody CaballoDTO caballoDTO) {
        Caballo caballo = CaballerizaMapper.toCaballoEntity(caballoDTO);
        Caballo guardado = caballoService.guardar(caballo);
        return CaballerizaMapper.toCaballoDTO(guardado);
    }

    @PutMapping("/{id}")
    public CaballoDTO actualizar(@PathVariable Long id, @Valid @RequestBody CaballoDTO caballoDTO) {
        Caballo caballo = CaballerizaMapper.toCaballoEntity(caballoDTO);
        Caballo actualizado = caballoService.actualizar(id, caballo);
        return CaballerizaMapper.toCaballoDTO(actualizado);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        caballoService.eliminar(id);
    }
}