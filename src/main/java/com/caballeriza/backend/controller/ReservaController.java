package com.caballeriza.backend.controller;

import com.caballeriza.backend.dto.ReservaDTO;
import com.caballeriza.backend.mapper.CaballerizaMapper;
import com.caballeriza.backend.model.*;
import com.caballeriza.backend.service.CaballoService;
import com.caballeriza.backend.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReservaController {

    private final ReservaService reservaService;
    private final CaballoService caballoService;

    @GetMapping
    public List<ReservaDTO> listar(
            @RequestParam(required = false) TipoReserva tipo,
            @RequestParam(required = false) EstadoReserva estado,
            @RequestParam(required = false) LocalDate fecha,
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fin,
            Authentication authentication
    ) {
        List<Reserva> reservas = esCliente(authentication)
                ? reservaService.listarPorCliente(authentication.getName())
                : reservaService.filtrar(tipo, fecha, estado, inicio, fin);

        return reservas
                .stream()
                .map(CaballerizaMapper::toReservaDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ReservaDTO obtenerPorId(@PathVariable Long id, Authentication authentication) {
        Reserva reserva = esCliente(authentication)
                ? reservaService.obtenerPorIdParaCliente(id, authentication.getName())
                : reservaService.obtenerPorId(id);

        return CaballerizaMapper.toReservaDTO(reserva);
    }

    @PostMapping
    public ReservaDTO guardar(@Valid @RequestBody ReservaDTO dto, Authentication authentication) {
        validarCaballoDeCliente(dto, authentication);

        Reserva reserva = esCliente(authentication)
                ? reservaService.guardarParaCliente(convertirAEntidad(dto), authentication.getName())
                : reservaService.guardar(convertirAEntidad(dto));

        return CaballerizaMapper.toReservaDTO(reserva);
    }

    @PutMapping("/{id}")
    public ReservaDTO actualizar(@PathVariable Long id, @Valid @RequestBody ReservaDTO dto, Authentication authentication) {
        validarCaballoDeCliente(dto, authentication);

        Reserva reserva = esCliente(authentication)
                ? reservaService.actualizarParaCliente(id, convertirAEntidad(dto), authentication.getName())
                : reservaService.actualizar(id, convertirAEntidad(dto));

        return CaballerizaMapper.toReservaDTO(reserva);
    }

    @PatchMapping("/{id}/cancelar")
    public ReservaDTO cancelar(@PathVariable Long id, Authentication authentication) {
        Reserva reserva = esCliente(authentication)
                ? reservaService.cancelarParaCliente(id, authentication.getName())
                : reservaService.cancelar(id);

        return CaballerizaMapper.toReservaDTO(reserva);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id, Authentication authentication) {
        if (esCliente(authentication)) {
            reservaService.obtenerPorIdParaCliente(id, authentication.getName());
        }

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
                .cantidadPersonas(dto.getCantidadPersonas())
                .cupoMaximo(dto.getCupoMaximo())
                .caballo(caballo)
                .empleado(empleado)
                .build();
    }

    private void validarCaballoDeCliente(ReservaDTO dto, Authentication authentication) {
        if (esCliente(authentication)) {
            caballoService.verificarAccesoCliente(dto.getCaballoId(), authentication.getName());
        }
    }

    private boolean esCliente(Authentication authentication) {
        return authentication != null
                && authentication.getAuthorities()
                .stream()
                .anyMatch(authority -> "ROLE_CLIENTE".equals(authority.getAuthority()));
    }
}
