package com.caballeriza.backend.repository;

import com.caballeriza.backend.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    Optional<Alerta> findByClave(String clave);

    List<Alerta> findAllByOrderByLeidaAscFechaCreacionDesc();

    List<Alerta> findByLeidaOrderByFechaCreacionDesc(Boolean leida);

    long countByLeidaFalse();
}