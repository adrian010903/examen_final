package com.caballeriza.backend.repository;

import com.caballeriza.backend.model.Caballo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaballoRepository extends JpaRepository<Caballo, Long> {
    boolean existsByIdentificador(String identificador);

    boolean existsByIdentificadorAndIdNot(String identificador, Long id);

    List<Caballo> findByClienteEmail(String email);
}
