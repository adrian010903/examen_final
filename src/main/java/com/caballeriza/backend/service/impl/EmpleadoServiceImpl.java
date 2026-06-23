package com.caballeriza.backend.service.impl;

import com.caballeriza.backend.model.Empleado;
import com.caballeriza.backend.repository.EmpleadoRepository;
import com.caballeriza.backend.service.EmpleadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    @Override
    public List<Empleado> listar() {
        return empleadoRepository.findAll();
    }

    @Override
    public List<Empleado> listarPorContacto(String contacto) {
        return empleadoRepository.findByContacto(contacto)
                .map(List::of)
                .orElse(List.of());
    }

    @Override
    public Empleado obtenerPorId(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
    }

    @Override
    public Empleado obtenerPorIdParaContacto(Long id, String contacto) {
        Empleado empleado = obtenerPorId(id);

        if (!Objects.equals(empleado.getContacto(), contacto)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No puede acceder a datos de otro empleado"
            );
        }

        return empleado;
    }

    @Override
    public Empleado guardar(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Override
    public Empleado actualizar(Long id, Empleado datos) {
        Empleado empleado = obtenerPorId(id);

        empleado.setNombre(datos.getNombre());
        empleado.setRol(datos.getRol());
        empleado.setContacto(datos.getContacto());

        return empleadoRepository.save(empleado);
    }

    @Override
    public void eliminar(Long id) {
        Empleado empleado = obtenerPorId(id);
        empleadoRepository.delete(empleado);
    }
}
