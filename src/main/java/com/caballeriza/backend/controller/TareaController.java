package com.caballeriza.backend.controller;

import com.caballeriza.backend.dto.TareaDTO;
import com.caballeriza.backend.mapper.CaballerizaMapper;
import com.caballeriza.backend.model.Caballo;
import com.caballeriza.backend.model.Empleado;
import com.caballeriza.backend.model.Tarea;
import com.caballeriza.backend.service.TareaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TareaController {

    private final TareaService tareaService;

    @GetMapping
    public List<TareaDTO> listar(Authentication authentication) {
        List<Tarea> tareas = esCuidador(authentication)
                ? tareaService.listarPorEmpleadoContacto(authentication.getName())
                : tareaService.listar();

        return tareas
                .stream()
                .map(CaballerizaMapper::toTareaDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public TareaDTO obtenerPorId(@PathVariable Long id, Authentication authentication) {
        Tarea tarea = esCuidador(authentication)
                ? tareaService.obtenerPorIdParaEmpleado(id, authentication.getName())
                : tareaService.obtenerPorId(id);

        return CaballerizaMapper.toTareaDTO(tarea);
    }

    @GetMapping("/empleado/{empleadoId}")
    public List<TareaDTO> listarPorEmpleado(@PathVariable Long empleadoId, Authentication authentication) {
        List<Tarea> tareas = esCuidador(authentication)
                ? tareaService.listarPorEmpleadoContacto(authentication.getName())
                : tareaService.listarPorEmpleado(empleadoId);

        return tareas
                .stream()
                .map(CaballerizaMapper::toTareaDTO)
                .toList();
    }

    @GetMapping("/caballo/{caballoId}")
    public List<TareaDTO> listarPorCaballo(@PathVariable Long caballoId, Authentication authentication) {
        List<Tarea> tareas = esCuidador(authentication)
                ? tareaService.listarPorEmpleadoContacto(authentication.getName())
                : tareaService.listarPorCaballo(caballoId);

        return tareas
                .stream()
                .filter(tarea -> !esCuidador(authentication)
                        || tarea.getCaballo() != null
                        && tarea.getCaballo().getId().equals(caballoId))
                .map(CaballerizaMapper::toTareaDTO)
                .toList();
    }

    @PostMapping
    public TareaDTO guardar(@Valid @RequestBody TareaDTO dto, Authentication authentication) {
        bloquearCuidador(authentication);
        Tarea tarea = convertirAEntidad(dto);
        Tarea guardada = tareaService.guardar(tarea);
        return CaballerizaMapper.toTareaDTO(guardada);
    }

    @PutMapping("/{id}")
    public TareaDTO actualizar(@PathVariable Long id, @Valid @RequestBody TareaDTO dto, Authentication authentication) {
        Tarea tarea = convertirAEntidad(dto);
        Tarea actualizada = esCuidador(authentication)
                ? tareaService.actualizarParaEmpleado(id, tarea, authentication.getName())
                : tareaService.actualizar(id, tarea);

        return CaballerizaMapper.toTareaDTO(actualizada);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id, Authentication authentication) {
        bloquearCuidador(authentication);
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

    private boolean esCuidador(Authentication authentication) {
        return authentication != null
                && authentication.getAuthorities()
                .stream()
                .anyMatch(authority -> "ROLE_CUIDADOR".equals(authority.getAuthority()));
    }

    private void bloquearCuidador(Authentication authentication) {
        if (esCuidador(authentication)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "El cuidador solo puede consultar y completar sus tareas asignadas"
            );
        }
    }
}
