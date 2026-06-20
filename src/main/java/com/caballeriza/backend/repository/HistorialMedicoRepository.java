package com.caballeriza.backend.repository;

import com.caballeriza.backend.model.HistorialMedico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, Long> {
    List<HistorialMedico> findByCaballoId(Long caballoId);
}