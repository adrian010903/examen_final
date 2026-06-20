package com.caballeriza.backend.service;

import com.caballeriza.backend.model.Suministro;

import java.util.List;

public interface SuministroService {

    List<Suministro> listar();

    List<Suministro> listarPorCaballo(Long caballoId);

    Suministro obtenerPorId(Long id);

    Suministro guardar(Suministro suministro);

    Suministro actualizar(Long id, Suministro suministro);

    void eliminar(Long id);
}