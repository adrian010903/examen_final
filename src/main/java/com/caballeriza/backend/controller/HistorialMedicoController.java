package com.caballeriza.backend.controller;

import com.caballeriza.backend.dto.HistorialMedicoDTO;
import com.caballeriza.backend.mapper.CaballerizaMapper;
import com.caballeriza.backend.model.Caballo;
import com.caballeriza.backend.model.HistorialMedico;
import com.caballeriza.backend.service.HistorialMedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial-medico")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HistorialMedicoController {

    private final HistorialMedicoService historialMedicoService;

    @GetMapping
    public List<HistorialMedicoDTO> listar() {
        return historialMedicoService.listar()
                .stream()
                .map(CaballerizaMapper::toHistorialMedicoDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public HistorialMedicoDTO obtenerPorId(@PathVariable Long id) {
        HistorialMedico historial = historialMedicoService.obtenerPorId(id);
        return CaballerizaMapper.toHistorialMedicoDTO(historial);
    }

    @GetMapping("/caballo/{caballoId}")
    public List<HistorialMedicoDTO> listarPorCaballo(@PathVariable Long caballoId) {
        return historialMedicoService.listarPorCaballo(caballoId)
                .stream()
                .map(CaballerizaMapper::toHistorialMedicoDTO)
                .toList();
    }

    @PostMapping
    public HistorialMedicoDTO guardar(@Valid @RequestBody HistorialMedicoDTO dto) {
        HistorialMedico historial = convertirAEntidad(dto);
        HistorialMedico guardado = historialMedicoService.guardar(historial);
        return CaballerizaMapper.toHistorialMedicoDTO(guardado);
    }

    @PutMapping("/{id}")
    public HistorialMedicoDTO actualizar(@PathVariable Long id, @Valid @RequestBody HistorialMedicoDTO dto) {
        HistorialMedico historial = convertirAEntidad(dto);
        HistorialMedico actualizado = historialMedicoService.actualizar(id, historial);
        return CaballerizaMapper.toHistorialMedicoDTO(actualizado);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        historialMedicoService.eliminar(id);
    }

    private HistorialMedico convertirAEntidad(HistorialMedicoDTO dto) {
        Caballo caballo = new Caballo();
        caballo.setId(dto.getCaballoId());

        return HistorialMedico.builder()
                .id(dto.getId())
                .tipo(dto.getTipo())
                .descripcion(dto.getDescripcion())
                .alergias(dto.getAlergias())
                .observaciones(dto.getObservaciones())
                .fecha(dto.getFecha())
                .fechaProxima(dto.getFechaProxima())
                .responsable(dto.getResponsable())
                .caballo(caballo)
                .build();
    }
}
