package com.caballeriza.backend.repository;

import com.caballeriza.backend.model.PlanAlimentacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanAlimentacionRepository extends JpaRepository<PlanAlimentacion, Long> {
    List<PlanAlimentacion> findByCaballoId(Long caballoId);
}