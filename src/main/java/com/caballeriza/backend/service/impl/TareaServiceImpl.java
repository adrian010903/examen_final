package com.caballeriza.backend.service.impl;

import com.caballeriza.backend.model.Caballo;
import com.caballeriza.backend.model.Empleado;
import com.caballeriza.backend.model.Tarea;
import com.caballeriza.backend.repository.CaballoRepository;
import com.caballeriza.backend.repository.EmpleadoRepository;
import com.caballeriza.backend.repository.TareaRepository;
import com.caballeriza.backend.service.TareaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

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
    public List<Tarea> listarPorEmpleadoContacto(String contacto) {
        return empleadoRepository.findByContacto(contacto)
                .map(empleado -> tareaRepository.findByEmpleadoId(empleado.getId()))
                .orElse(List.of());
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
    public Tarea obtenerPorIdParaEmpleado(Long id, String contacto) {
        Tarea tarea = obtenerPorId(id);
        validarAsignadaAEmpleado(tarea, contacto);
        return tarea;
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
    public Tarea actualizarParaEmpleado(Long id, Tarea datos, String contacto) {
        Tarea tarea = obtenerPorIdParaEmpleado(id, contacto);
        tarea.setCompletada(datos.getCompletada());
        return tareaRepository.save(tarea);
    }

    @Override
    public void eliminar(Long id) {
        Tarea tarea = obtenerPorId(id);
        tareaRepository.delete(tarea);
    }

    private void validarAsignadaAEmpleado(Tarea tarea, String contacto) {
        String contactoEmpleado = tarea.getEmpleado() != null
                ? tarea.getEmpleado().getContacto()
                : null;

        if (!Objects.equals(contactoEmpleado, contacto)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No puede acceder a una tarea asignada a otro empleado"
            );
        }
    }
}
