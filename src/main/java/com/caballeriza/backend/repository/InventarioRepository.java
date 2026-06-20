package com.caballeriza.backend.repository;

import com.caballeriza.backend.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    List<Inventario> findByCantidadLessThanEqual(Integer cantidad);
}