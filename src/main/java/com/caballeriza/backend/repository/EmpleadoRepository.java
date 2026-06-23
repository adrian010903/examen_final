package com.caballeriza.backend.repository;

import com.caballeriza.backend.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByContacto(String contacto);
}
