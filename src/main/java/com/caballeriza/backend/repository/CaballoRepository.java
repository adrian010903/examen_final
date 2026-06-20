package com.caballeriza.backend.repository;

import com.caballeriza.backend.model.Caballo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaballoRepository extends JpaRepository<Caballo, Long> {
    boolean existsByIdentificador(String identificador);
}
