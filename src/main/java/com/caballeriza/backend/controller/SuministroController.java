package com.caballeriza.backend.controller;

import com.caballeriza.backend.dto.SuministroDTO;
import com.caballeriza.backend.mapper.CaballerizaMapper;
import com.caballeriza.backend.model.Caballo;
import com.caballeriza.backend.model.Suministro;
import com.caballeriza.backend.service.SuministroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suministros")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SuministroController {

    private final SuministroService suministroService;

    @GetMapping
    public List<SuministroDTO> listar() {
        return suministroService.listar()
                .stream()
                .map(CaballerizaMapper::toSuministroDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public SuministroDTO obtenerPorId(@PathVariable Long id) {
        Suministro suministro = suministroService.obtenerPorId(id);
        return CaballerizaMapper.toSuministroDTO(suministro);
    }

    @GetMapping("/caballo/{caballoId}")
    public List<SuministroDTO> listarPorCaballo(@PathVariable Long caballoId) {
        return suministroService.listarPorCaballo(caballoId)
                .stream()
                .map(CaballerizaMapper::toSuministroDTO)
                .toList();
    }

    @PostMapping
    public SuministroDTO guardar(@Valid @RequestBody SuministroDTO dto) {
        Suministro suministro = convertirAEntidad(dto);
        Suministro guardado = suministroService.guardar(suministro);
        return CaballerizaMapper.toSuministroDTO(guardado);
    }

    @PutMapping("/{id}")
    public SuministroDTO actualizar(@PathVariable Long id, @Valid @RequestBody SuministroDTO dto) {
        Suministro suministro = convertirAEntidad(dto);
        Suministro actualizado = suministroService.actualizar(id, suministro);
        return CaballerizaMapper.toSuministroDTO(actualizado);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        suministroService.eliminar(id);
    }

    private Suministro convertirAEntidad(SuministroDTO dto) {
        Caballo caballo = new Caballo();
        caballo.setId(dto.getCaballoId());

        return Suministro.builder()
                .id(dto.getId())
                .fecha(dto.getFecha())
                .tipo(dto.getTipo())
                .cantidad(dto.getCantidad())
                .caballo(caballo)
                .build();
    }
}