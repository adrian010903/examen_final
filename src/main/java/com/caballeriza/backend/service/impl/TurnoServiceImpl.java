package com.caballeriza.backend.service.impl;

import com.caballeriza.backend.model.Empleado;
import com.caballeriza.backend.model.Turno;
import com.caballeriza.backend.repository.EmpleadoRepository;
import com.caballeriza.backend.repository.TurnoRepository;
import com.caballeriza.backend.service.TurnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TurnoServiceImpl implements TurnoService {

    private final TurnoRepository turnoRepository;
    private final EmpleadoRepository empleadoRepository;

    @Override
    public List<Turno> listar() {
        return turnoRepository.findAll();
    }

    @Override
    public List<Turno> listarPorEmpleado(Long empleadoId) {
        return turnoRepository.findByEmpleadoId(empleadoId);
    }

    @Override
    public Turno obtenerPorId(Long id) {
        return turnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
    }

    @Override
    public Turno guardar(Turno turno) {
        Long empleadoId = turno.getEmpleado().getId();

        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        turno.setEmpleado(empleado);

        return turnoRepository.save(turno);
    }

    @Override
    public Turno actualizar(Long id, Turno datos) {
        Turno turno = obtenerPorId(id);

        turno.setFecha(datos.getFecha());
        turno.setHoraInicio(datos.getHoraInicio());
        turno.setHoraFin(datos.getHoraFin());

        if (datos.getEmpleado() != null && datos.getEmpleado().getId() != null) {
            Empleado empleado = empleadoRepository.findById(datos.getEmpleado().getId())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

            turno.setEmpleado(empleado);
        }

        return turnoRepository.save(turno);
    }

    @Override
    public void eliminar(Long id) {
        Turno turno = obtenerPorId(id);
        turnoRepository.delete(turno);
    }
}