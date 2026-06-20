package com.caballeriza.backend.service.impl;

import com.caballeriza.backend.model.Caballo;
import com.caballeriza.backend.model.PlanAlimentacion;
import com.caballeriza.backend.repository.CaballoRepository;
import com.caballeriza.backend.repository.PlanAlimentacionRepository;
import com.caballeriza.backend.service.PlanAlimentacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanAlimentacionServiceImpl implements PlanAlimentacionService {

    private final PlanAlimentacionRepository planAlimentacionRepository;
    private final CaballoRepository caballoRepository;

    @Override
    public List<PlanAlimentacion> listar() {
        return planAlimentacionRepository.findAll();
    }

    @Override
    public List<PlanAlimentacion> listarPorCaballo(Long caballoId) {
        return planAlimentacionRepository.findByCaballoId(caballoId);
    }

    @Override
    public PlanAlimentacion obtenerPorId(Long id) {
        return planAlimentacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan de alimentación no encontrado"));
    }

    @Override
    public PlanAlimentacion guardar(PlanAlimentacion planAlimentacion) {
        Long caballoId = planAlimentacion.getCaballo().getId();

        Caballo caballo = caballoRepository.findById(caballoId)
                .orElseThrow(() -> new RuntimeException("Caballo no encontrado"));

        planAlimentacion.setCaballo(caballo);

        return planAlimentacionRepository.save(planAlimentacion);
    }

    @Override
    public PlanAlimentacion actualizar(Long id, PlanAlimentacion datos) {
        PlanAlimentacion plan = obtenerPorId(id);

        plan.setTipoAlimento(datos.getTipoAlimento());
        plan.setCantidad(datos.getCantidad());
        plan.setFrecuencia(datos.getFrecuencia());
        plan.setObservaciones(datos.getObservaciones());

        if (datos.getCaballo() != null && datos.getCaballo().getId() != null) {
            Caballo caballo = caballoRepository.findById(datos.getCaballo().getId())
                    .orElseThrow(() -> new RuntimeException("Caballo no encontrado"));

            plan.setCaballo(caballo);
        }

        return planAlimentacionRepository.save(plan);
    }

    @Override
    public void eliminar(Long id) {
        PlanAlimentacion plan = obtenerPorId(id);
        planAlimentacionRepository.delete(plan);
    }
}