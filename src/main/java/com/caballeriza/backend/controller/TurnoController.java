package com.caballeriza.backend.controller;

import com.caballeriza.backend.dto.TurnoDTO;
import com.caballeriza.backend.mapper.CaballerizaMapper;
import com.caballeriza.backend.model.Empleado;
import com.caballeriza.backend.model.Turno;
import com.caballeriza.backend.service.TurnoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TurnoController {

    private final TurnoService turnoService;

    @GetMapping
    public List<TurnoDTO> listar(Authentication authentication) {
        List<Turno> turnos = esCuidador(authentication)
                ? turnoService.listarPorEmpleadoContacto(authentication.getName())
                : turnoService.listar();

        return turnos
                .stream()
                .map(CaballerizaMapper::toTurnoDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public TurnoDTO obtenerPorId(@PathVariable Long id, Authentication authentication) {
        Turno turno = esCuidador(authentication)
                ? turnoService.obtenerPorIdParaEmpleado(id, authentication.getName())
                : turnoService.obtenerPorId(id);

        return CaballerizaMapper.toTurnoDTO(turno);
    }

    @GetMapping("/empleado/{empleadoId}")
    public List<TurnoDTO> listarPorEmpleado(@PathVariable Long empleadoId, Authentication authentication) {
        List<Turno> turnos = esCuidador(authentication)
                ? turnoService.listarPorEmpleadoContacto(authentication.getName())
                : turnoService.listarPorEmpleado(empleadoId);

        return turnos
                .stream()
                .map(CaballerizaMapper::toTurnoDTO)
                .toList();
    }

    @PostMapping
    public TurnoDTO guardar(@Valid @RequestBody TurnoDTO dto, Authentication authentication) {
        bloquearCuidador(authentication);
        Turno turno = convertirAEntidad(dto);
        Turno guardado = turnoService.guardar(turno);
        return CaballerizaMapper.toTurnoDTO(guardado);
    }

    @PutMapping("/{id}")
    public TurnoDTO actualizar(@PathVariable Long id, @Valid @RequestBody TurnoDTO dto, Authentication authentication) {
        bloquearCuidador(authentication);
        Turno turno = convertirAEntidad(dto);
        Turno actualizado = turnoService.actualizar(id, turno);
        return CaballerizaMapper.toTurnoDTO(actualizado);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id, Authentication authentication) {
        bloquearCuidador(authentication);
        turnoService.eliminar(id);
    }

    private Turno convertirAEntidad(TurnoDTO dto) {
        Empleado empleado = new Empleado();
        empleado.setId(dto.getEmpleadoId());

        return Turno.builder()
                .id(dto.getId())
                .fecha(dto.getFecha())
                .horaInicio(dto.getHoraInicio())
                .horaFin(dto.getHoraFin())
                .empleado(empleado)
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
                    "El cuidador solo puede consultar sus turnos asignados"
            );
        }
    }
}
