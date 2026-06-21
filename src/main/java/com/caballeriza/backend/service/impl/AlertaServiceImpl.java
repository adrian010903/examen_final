package com.caballeriza.backend.service.impl;

import com.caballeriza.backend.dto.AlertaDTO;
import com.caballeriza.backend.model.HistorialMedico;
import com.caballeriza.backend.model.Inventario;
import com.caballeriza.backend.repository.HistorialMedicoRepository;
import com.caballeriza.backend.repository.InventarioRepository;
import com.caballeriza.backend.service.AlertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertaServiceImpl implements AlertaService {

    private final InventarioRepository inventarioRepository;
    private final HistorialMedicoRepository historialMedicoRepository;

    @Override
    public List<AlertaDTO> listar() {
        List<AlertaDTO> alertas = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(30);

        for (Inventario item : inventarioRepository.findAll()) {
            if (Boolean.TRUE.equals(item.getStockBajo())) {
                alertas.add(AlertaDTO.builder()
                        .tipo("STOCK_BAJO")
                        .severidad("ALTA")
                        .titulo("Stock bajo: " + item.getNombre())
                        .descripcion("Cantidad disponible: " + item.getCantidad() + " " + item.getUnidad()
                                + ". Minimo requerido: " + item.getStockMinimo() + ".")
                        .referenciaId(item.getId())
                        .build());
            }
        }

        for (HistorialMedico historial : historialMedicoRepository.findAll()) {
            if (historial.getFechaProxima() == null) {
                continue;
            }

            boolean vencida = historial.getFechaProxima().isBefore(hoy);
            boolean proxima = !historial.getFechaProxima().isAfter(limite);

            if (vencida || proxima) {
                String tipo = historial.getTipo() != null ? historial.getTipo().toLowerCase() : "";
                boolean esVacuna = tipo.contains("vacuna");
                String nombreCaballo = historial.getCaballo() != null ? historial.getCaballo().getNombre() : "Caballo";

                alertas.add(AlertaDTO.builder()
                        .tipo(esVacuna ? "VACUNACION" : "TRATAMIENTO")
                        .severidad(vencida ? "ALTA" : "MEDIA")
                        .titulo((vencida ? "Vencido: " : "Proximo: ") + historial.getTipo())
                        .descripcion(nombreCaballo + " requiere seguimiento el " + historial.getFechaProxima() + ".")
                        .fecha(historial.getFechaProxima())
                        .referenciaId(historial.getId())
                        .build());
            }
        }

        return alertas.stream()
                .sorted(Comparator.comparing(AlertaDTO::getSeveridad).thenComparing(AlertaDTO::getFecha, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }
}
