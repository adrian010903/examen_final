package com.caballeriza.backend.service.impl;

import com.caballeriza.backend.model.Caballo;
import com.caballeriza.backend.model.Suministro;
import com.caballeriza.backend.repository.CaballoRepository;
import com.caballeriza.backend.repository.SuministroRepository;
import com.caballeriza.backend.service.SuministroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuministroServiceImpl implements SuministroService {

    private final SuministroRepository suministroRepository;
    private final CaballoRepository caballoRepository;

    @Override
    public List<Suministro> listar() {
        return suministroRepository.findAll();
    }

    @Override
    public List<Suministro> listarPorCaballo(Long caballoId) {
        return suministroRepository.findByCaballoId(caballoId);
    }

    @Override
    public Suministro obtenerPorId(Long id) {
        return suministroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Suministro no encontrado"));
    }

    @Override
    public Suministro guardar(Suministro suministro) {
        Long caballoId = suministro.getCaballo().getId();

        Caballo caballo = caballoRepository.findById(caballoId)
                .orElseThrow(() -> new RuntimeException("Caballo no encontrado"));

        suministro.setCaballo(caballo);

        return suministroRepository.save(suministro);
    }

    @Override
    public Suministro actualizar(Long id, Suministro datos) {
        Suministro suministro = obtenerPorId(id);

        suministro.setFecha(datos.getFecha());
        suministro.setTipo(datos.getTipo());
        suministro.setCantidad(datos.getCantidad());

        if (datos.getCaballo() != null && datos.getCaballo().getId() != null) {
            Caballo caballo = caballoRepository.findById(datos.getCaballo().getId())
                    .orElseThrow(() -> new RuntimeException("Caballo no encontrado"));

            suministro.setCaballo(caballo);
        }

        return suministroRepository.save(suministro);
    }

    @Override
    public void eliminar(Long id) {
        Suministro suministro = obtenerPorId(id);
        suministroRepository.delete(suministro);
    }
}