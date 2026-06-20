package com.caballeriza.backend.controller;

import com.caballeriza.backend.dto.TareaDTO;
import com.caballeriza.backend.mapper.CaballerizaMapper;
import com.caballeriza.backend.model.Caballo;
import com.caballeriza.backend.model.Empleado;
import com.caballeriza.backend.model.Tarea;
import com.caballeriza.backend.service.TareaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TareaController {

    private final TareaService tareaService;

    @GetMapping
    public List<TareaDTO> listar() {
        return tareaService.listar()
                .stream()
                .map(CaballerizaMapper::toTareaDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public TareaDTO obtenerPorId(@PathVariable Long id) {
        Tarea tarea = tareaService.obtenerPorId(id);
        return CaballerizaMapper.toTareaDTO(tarea);
    }

    @GetMapping("/empleado/{empleadoId}")
    public List<TareaDTO> listarPorEmpleado(@PathVariable Long empleadoId) {
        return tareaService.listarPorEmpleado(empleadoId)
                .stream()
                .map(CaballerizaMapper::toTareaDTO)
                .toList();
    }

    @GetMapping("/caballo/{caballoId}")
    public List<TareaDTO> listarPorCaballo(@PathVariable Long caballoId) {
        return tareaService.listarPorCaballo(caballoId)
                .stream()
                .map(CaballerizaMapper::toTareaDTO)
                .toList();
    }

    @PostMapping
    public TareaDTO guardar(@Valid @RequestBody TareaDTO dto) {
        Tarea tarea = convertirAEntidad(dto);
        Tarea guardada = tareaService.guardar(tarea);
        return CaballerizaMapper.toTareaDTO(guardada);
    }

    @PutMapping("/{id}")
    public TareaDTO actualizar(@PathVariable Long id, @Valid @RequestBody TareaDTO dto) {
        Tarea tarea = convertirAEntidad(dto);
        Tarea actualizada = tareaService.actualizar(id, tarea);
        return CaballerizaMapper.toTareaDTO(actualizada);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        tareaService.eliminar(id);
    }

    private Tarea convertirAEntidad(TareaDTO dto) {
        Empleado empleado = new Empleado();
        empleado.setId(dto.getEmpleadoId());

        Caballo caballo = null;

        if (dto.getCaballoId() != null) {
            caballo = new Caballo();
            caballo.setId(dto.getCaballoId());
        }

        return Tarea.builder()
                .id(dto.getId())
                .descripcion(dto.getDescripcion())
                .fecha(dto.getFecha())
                .completada(dto.getCompletada())
                .empleado(empleado)
                .caballo(caballo)
                .build();
    }
}