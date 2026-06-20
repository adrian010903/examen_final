package com.caballeriza.backend.service.impl;

import com.caballeriza.backend.model.Caballo;
import com.caballeriza.backend.model.HistorialMedico;
import com.caballeriza.backend.repository.CaballoRepository;
import com.caballeriza.backend.repository.HistorialMedicoRepository;
import com.caballeriza.backend.service.HistorialMedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistorialMedicoServiceImpl implements HistorialMedicoService {

    private final HistorialMedicoRepository historialMedicoRepository;
    private final CaballoRepository caballoRepository;

    @Override
    public List<HistorialMedico> listar() {
        return historialMedicoRepository.findAll();
    }

    @Override
    public List<HistorialMedico> listarPorCaballo(Long caballoId) {
        return historialMedicoRepository.findByCaballoId(caballoId);
    }

    @Override
    public HistorialMedico obtenerPorId(Long id) {
        return historialMedicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial médico no encontrado"));
    }

    @Override
    public HistorialMedico guardar(HistorialMedico historialMedico) {
        Long caballoId = historialMedico.getCaballo().getId();

        Caballo caballo = caballoRepository.findById(caballoId)
                .orElseThrow(() -> new RuntimeException("Caballo no encontrado"));

        historialMedico.setCaballo(caballo);

        return historialMedicoRepository.save(historialMedico);
    }

    @Override
    public HistorialMedico actualizar(Long id, HistorialMedico datos) {
        HistorialMedico historial = obtenerPorId(id);

        historial.setTipo(datos.getTipo());
        historial.setDescripcion(datos.getDescripcion());
        historial.setAlergias(datos.getAlergias());
        historial.setObservaciones(datos.getObservaciones());
        historial.setFecha(datos.getFecha());
        historial.setResponsable(datos.getResponsable());

        if (datos.getCaballo() != null && datos.getCaballo().getId() != null) {
            Caballo caballo = caballoRepository.findById(datos.getCaballo().getId())
                    .orElseThrow(() -> new RuntimeException("Caballo no encontrado"));

            historial.setCaballo(caballo);
        }

        return historialMedicoRepository.save(historial);
    }

    @Override
    public void eliminar(Long id) {
        HistorialMedico historial = obtenerPorId(id);
        historialMedicoRepository.delete(historial);
    }
}