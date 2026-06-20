package com.caballeriza.backend.controller;

import com.caballeriza.backend.dto.PlanAlimentacionDTO;
import com.caballeriza.backend.mapper.CaballerizaMapper;
import com.caballeriza.backend.model.Caballo;
import com.caballeriza.backend.model.PlanAlimentacion;
import com.caballeriza.backend.service.PlanAlimentacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planes-alimentacion")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PlanAlimentacionController {

    private final PlanAlimentacionService planAlimentacionService;

    @GetMapping
    public List<PlanAlimentacionDTO> listar() {
        return planAlimentacionService.listar()
                .stream()
                .map(CaballerizaMapper::toPlanAlimentacionDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public PlanAlimentacionDTO obtenerPorId(@PathVariable Long id) {
        PlanAlimentacion plan = planAlimentacionService.obtenerPorId(id);
        return CaballerizaMapper.toPlanAlimentacionDTO(plan);
    }

    @GetMapping("/caballo/{caballoId}")
    public List<PlanAlimentacionDTO> listarPorCaballo(@PathVariable Long caballoId) {
        return planAlimentacionService.listarPorCaballo(caballoId)
                .stream()
                .map(CaballerizaMapper::toPlanAlimentacionDTO)
                .toList();
    }

    @PostMapping
    public PlanAlimentacionDTO guardar(@Valid @RequestBody PlanAlimentacionDTO dto) {
        PlanAlimentacion plan = convertirAEntidad(dto);
        PlanAlimentacion guardado = planAlimentacionService.guardar(plan);
        return CaballerizaMapper.toPlanAlimentacionDTO(guardado);
    }

    @PutMapping("/{id}")
    public PlanAlimentacionDTO actualizar(@PathVariable Long id, @Valid @RequestBody PlanAlimentacionDTO dto) {
        PlanAlimentacion plan = convertirAEntidad(dto);
        PlanAlimentacion actualizado = planAlimentacionService.actualizar(id, plan);
        return CaballerizaMapper.toPlanAlimentacionDTO(actualizado);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        planAlimentacionService.eliminar(id);
    }

    private PlanAlimentacion convertirAEntidad(PlanAlimentacionDTO dto) {
        Caballo caballo = new Caballo();
        caballo.setId(dto.getCaballoId());

        return PlanAlimentacion.builder()
                .id(dto.getId())
                .tipoAlimento(dto.getTipoAlimento())
                .cantidad(dto.getCantidad())
                .frecuencia(dto.getFrecuencia())
                .observaciones(dto.getObservaciones())
                .caballo(caballo)
                .build();
    }
}