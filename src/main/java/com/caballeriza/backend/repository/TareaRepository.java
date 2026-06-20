package com.caballeriza.backend.repository;

import com.caballeriza.backend.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TareaRepository extends JpaRepository<Tarea, Long> {
    List<Tarea> findByEmpleadoId(Long empleadoId);
    List<Tarea> findByCaballoId(Long caballoId);
}