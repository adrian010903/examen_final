package com.caballeriza.backend.service;

import com.caballeriza.backend.dto.AlertaDTO;

import java.util.List;

public interface AlertaService {

    List<AlertaDTO> listar(Boolean leida);

    AlertaDTO marcarLeida(Long id);

    AlertaDTO marcarNoLeida(Long id);

    long contarNoLeidas();
}