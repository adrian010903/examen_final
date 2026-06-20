package com.caballeriza.backend.service.impl;

import com.caballeriza.backend.model.Caballo;
import com.caballeriza.backend.model.Empleado;
import com.caballeriza.backend.model.Tarea;
import com.caballeriza.backend.repository.CaballoRepository;
import com.caballeriza.backend.repository.EmpleadoRepository;
import com.caballeriza.backend.repository.TareaRepository;
import com.caballeriza.backend.service.TareaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TareaServiceImpl implements TareaService {

    private final TareaRepository tareaRepository;
    private final EmpleadoRepository empleadoRepository;
    private final CaballoRepository caballoRepository;

    @Override
    public List<Tarea> listar() {
        return tareaRepository.findAll();
    }

    @Override
    public List<Tarea> listarPorEmpleado(Long empleadoId) {
        return tareaRepository.findByEmpleadoId(empleadoId);
    }

    @Override
    public List<Tarea> listarPorCaballo(Long caballoId) {
        return tareaRepository.findByCaballoId(caballoId);
    }

    @Override
    public Tarea obtenerPorId(Long id) {
        return tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
    }

    @Override
    public Tarea guardar(Tarea tarea) {
        Long empleadoId = tarea.getEmpleado().getId();

        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        tarea.setEmpleado(empleado);

        if (tarea.getCaballo() != null && tarea.getCaballo().getId() != null) {
            Caballo caballo = caballoRepository.findById(tarea.getCaballo().getId())
                    .orElseThrow(() -> new RuntimeException("Caballo no encontrado"));

            tarea.setCaballo(caballo);
        }

        if (tarea.getCompletada() == null) {
            tarea.setCompletada(false);
        }

        return tareaRepository.save(tarea);
    }

    @Override
    public Tarea actualizar(Long id, Tarea datos) {
        Tarea tarea = obtenerPorId(id);

        tarea.setDescripcion(datos.getDescripcion());
        tarea.setFecha(datos.getFecha());
        tarea.setCompletada(datos.getCompletada());

        if (datos.getEmpleado() != null && datos.getEmpleado().getId() != null) {
            Empleado empleado = empleadoRepository.findById(datos.getEmpleado().getId())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

            tarea.setEmpleado(empleado);
        }

        if (datos.getCaballo() != null && datos.getCaballo().getId() != null) {
            Caballo caballo = caballoRepository.findById(datos.getCaballo().getId())
                    .orElseThrow(() -> new RuntimeException("Caballo no encontrado"));

            tarea.setCaballo(caballo);
        } else {
            tarea.setCaballo(null);
        }

        return tareaRepository.save(tarea);
    }

    @Override
    public void eliminar(Long id) {
        Tarea tarea = obtenerPorId(id);
        tareaRepository.delete(tarea);
    }
}