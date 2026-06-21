package com.caballeriza.backend.controller;

import com.caballeriza.backend.dto.ReservaDTO;
import com.caballeriza.backend.mapper.CaballerizaMapper;
import com.caballeriza.backend.model.*;
import com.caballeriza.backend.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReservaController {

    private final ReservaService reservaService;

    @GetMapping
    public List<ReservaDTO> listar(
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fin
    ) {
        List<Reserva> reservas = inicio != null && fin != null
                ? reservaService.listarPorRango(inicio, fin)
                : reservaService.listar();

        return reservas.stream()
                .map(CaballerizaMapper::toReservaDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ReservaDTO obtenerPorId(@PathVariable Long id) {
        return CaballerizaMapper.toReservaDTO(reservaService.obtenerPorId(id));
    }

    @PostMapping
    public ReservaDTO guardar(@Valid @RequestBody ReservaDTO dto) {
        return CaballerizaMapper.toReservaDTO(reservaService.guardar(convertirAEntidad(dto)));
    }

    @PutMapping("/{id}")
    public ReservaDTO actualizar(@PathVariable Long id, @Valid @RequestBody ReservaDTO dto) {
        return CaballerizaMapper.toReservaDTO(reservaService.actualizar(id, convertirAEntidad(dto)));
    }

    @PatchMapping("/{id}/cancelar")
    public ReservaDTO cancelar(@PathVariable Long id) {
        return CaballerizaMapper.toReservaDTO(reservaService.cancelar(id));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        reservaService.eliminar(id);
    }

    private Reserva convertirAEntidad(ReservaDTO dto) {
        Caballo caballo = new Caballo();
        caballo.setId(dto.getCaballoId());

        Empleado empleado = null;
        if (dto.getEmpleadoId() != null) {
            empleado = new Empleado();
            empleado.setId(dto.getEmpleadoId());
        }

        return Reserva.builder()
                .id(dto.getId())
                .tipo(dto.getTipo())
                .estado(dto.getEstado())
                .fecha(dto.getFecha())
                .horaInicio(dto.getHoraInicio())
                .horaFin(dto.getHoraFin())
                .cliente(dto.getCliente())
                .observaciones(dto.getObservaciones())
                .caballo(caballo)
                .empleado(empleado)
                .build();
    }
}
