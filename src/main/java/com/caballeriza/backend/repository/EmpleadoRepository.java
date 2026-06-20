package com.caballeriza.backend.repository;

import com.caballeriza.backend.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
}