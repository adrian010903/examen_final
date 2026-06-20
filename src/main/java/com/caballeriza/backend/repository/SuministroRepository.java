package com.caballeriza.backend.repository;


import com.caballeriza.backend.model.Suministro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuministroRepository extends JpaRepository<Suministro, Long> {
    List<Suministro> findByCaballoId(Long caballoId);
}