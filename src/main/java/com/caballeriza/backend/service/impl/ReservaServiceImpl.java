package com.caballeriza.backend.service.impl;

import com.caballeriza.backend.model.*;
import com.caballeriza.backend.repository.CaballoRepository;
import com.caballeriza.backend.repository.EmpleadoRepository;
import com.caballeriza.backend.repository.ReservaRepository;
import com.caballeriza.backend.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final CaballoRepository caballoRepository;
    private final EmpleadoRepository empleadoRepository;

    @Override
    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }

    @Override
    public List<Reserva> listarPorRango(LocalDate inicio, LocalDate fin) {
        return reservaRepository.findByFechaBetween(inicio, fin);
    }

    @Override
    public Reserva obtenerPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    @Override
    public Reserva guardar(Reserva reserva) {
        asignarRelaciones(reserva);
        if (reserva.getEstado() == null) {
            reserva.setEstado(EstadoReserva.PROGRAMADA);
        }
        return reservaRepository.save(reserva);
    }

    @Override
    public Reserva actualizar(Long id, Reserva datos) {
        Reserva reserva = obtenerPorId(id);

        reserva.setTipo(datos.getTipo());
        reserva.setEstado(datos.getEstado() != null ? datos.getEstado() : reserva.getEstado());
        reserva.setFecha(datos.getFecha());
        reserva.setHoraInicio(datos.getHoraInicio());
        reserva.setHoraFin(datos.getHoraFin());
        reserva.setCliente(datos.getCliente());
        reserva.setObservaciones(datos.getObservaciones());
        reserva.setCaballo(datos.getCaballo());
        reserva.setEmpleado(datos.getEmpleado());

        asignarRelaciones(reserva);
        return reservaRepository.save(reserva);
    }

    @Override
    public Reserva cancelar(Long id) {
        Reserva reserva = obtenerPorId(id);
        reserva.setEstado(EstadoReserva.CANCELADA);
        return reservaRepository.save(reserva);
    }

    @Override
    public void eliminar(Long id) {
        reservaRepository.delete(obtenerPorId(id));
    }

    private void asignarRelaciones(Reserva reserva) {
        Long caballoId = reserva.getCaballo() != null ? reserva.getCaballo().getId() : null;
        if (caballoId == null) {
            throw new RuntimeException("Caballo no encontrado");
        }

        Caballo caballo = caballoRepository.findById(caballoId)
                .orElseThrow(() -> new RuntimeException("Caballo no encontrado"));
        reserva.setCaballo(caballo);

        Long empleadoId = reserva.getEmpleado() != null ? reserva.getEmpleado().getId() : null;
        if (empleadoId != null) {
            Empleado empleado = empleadoRepository.findById(empleadoId)
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
            reserva.setEmpleado(empleado);
        } else {
            reserva.setEmpleado(null);
        }
    }
}
