package com.caballeriza.backend.service.impl;

import com.caballeriza.backend.model.Caballo;
import com.caballeriza.backend.repository.CaballoRepository;
import com.caballeriza.backend.service.CaballoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CaballoServiceImpl implements CaballoService {

    private final CaballoRepository caballoRepository;

    @Override
    public List<Caballo> listar() {
        return caballoRepository.findAll();
    }

    @Override
    public Caballo obtenerPorId(Long id) {
        return caballoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Caballo no encontrado"));
    }

    @Override
    public Caballo guardar(Caballo caballo) {
        if (caballoRepository.existsByIdentificador(caballo.getIdentificador())) {
            throw new RuntimeException("Ya existe un caballo con ese identificador");
        }
        return caballoRepository.save(caballo);
    }

    @Override
    public Caballo actualizar(Long id, Caballo datos) {
        Caballo caballo = obtenerPorId(id);

        caballo.setNombre(datos.getNombre());
        caballo.setIdentificador(datos.getIdentificador());
        caballo.setEdad(datos.getEdad());
        caballo.setRaza(datos.getRaza());
        caballo.setSexo(datos.getSexo());
        caballo.setPeso(datos.getPeso());
        caballo.setFotoUrl(datos.getFotoUrl());

        return caballoRepository.save(caballo);
    }

    @Override
    public void eliminar(Long id) {
        Caballo caballo = obtenerPorId(id);
        caballoRepository.delete(caballo);
    }
}