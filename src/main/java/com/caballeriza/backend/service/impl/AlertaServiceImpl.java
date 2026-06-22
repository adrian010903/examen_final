package com.caballeriza.backend.service.impl;

import com.caballeriza.backend.dto.AlertaDTO;
import com.caballeriza.backend.model.Alerta;
import com.caballeriza.backend.model.HistorialMedico;
import com.caballeriza.backend.model.Inventario;
import com.caballeriza.backend.repository.AlertaRepository;
import com.caballeriza.backend.repository.HistorialMedicoRepository;
import com.caballeriza.backend.repository.InventarioRepository;
import com.caballeriza.backend.service.AlertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertaServiceImpl implements AlertaService {

    private final AlertaRepository alertaRepository;
    private final InventarioRepository inventarioRepository;
    private final HistorialMedicoRepository historialMedicoRepository;

    @Override
    public List<AlertaDTO> listar(Boolean leida) {
        sincronizarAlertas();

        List<Alerta> alertas;

        if (leida == null) {
            alertas = alertaRepository
                    .findAllByOrderByLeidaAscFechaCreacionDesc();
        } else {
            alertas = alertaRepository
                    .findByLeidaOrderByFechaCreacionDesc(leida);
        }

        return alertas.stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    public AlertaDTO marcarLeida(Long id) {
        Alerta alerta = obtenerPorId(id);
        alerta.setLeida(true);

        return convertirADTO(
                alertaRepository.save(alerta)
        );
    }

    @Override
    public AlertaDTO marcarNoLeida(Long id) {
        Alerta alerta = obtenerPorId(id);
        alerta.setLeida(false);

        return convertirADTO(
                alertaRepository.save(alerta)
        );
    }

    @Override
    public long contarNoLeidas() {
        sincronizarAlertas();
        return alertaRepository.countByLeidaFalse();
    }

    private Alerta obtenerPorId(Long id) {
        return alertaRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Alerta no encontrada"
                        )
                );
    }

    private void sincronizarAlertas() {
        List<AlertaDTO> alertasActuales =
                generarAlertasActuales();

        Set<String> clavesActivas = new HashSet<>();

        for (AlertaDTO alertaDTO : alertasActuales) {
            String clave = crearClave(alertaDTO);
            clavesActivas.add(clave);

            Alerta alerta = alertaRepository
                    .findByClave(clave)
                    .orElseGet(() ->
                            Alerta.builder()
                                    .clave(clave)
                                    .leida(false)
                                    .build()
                    );

            alerta.setTipo(alertaDTO.getTipo());
            alerta.setSeveridad(alertaDTO.getSeveridad());
            alerta.setTitulo(alertaDTO.getTitulo());
            alerta.setDescripcion(alertaDTO.getDescripcion());
            alerta.setFecha(alertaDTO.getFecha());
            alerta.setReferenciaId(
                    alertaDTO.getReferenciaId()
            );

            alertaRepository.save(alerta);
        }

        List<Alerta> alertasGuardadas =
                alertaRepository.findAll();

        for (Alerta alerta : alertasGuardadas) {
            if (!clavesActivas.contains(alerta.getClave())) {
                alertaRepository.delete(alerta);
            }
        }
    }

    private List<AlertaDTO> generarAlertasActuales() {
        List<AlertaDTO> alertas = new ArrayList<>();

        generarAlertasDeInventario(alertas);
        generarAlertasMedicas(alertas);

        return alertas;
    }

    private void generarAlertasDeInventario(
            List<AlertaDTO> alertas
    ) {
        for (Inventario item : inventarioRepository.findAll()) {

            if (Boolean.TRUE.equals(item.getStockBajo())) {

                alertas.add(
                        AlertaDTO.builder()
                                .tipo("STOCK_BAJO")
                                .severidad("ALTA")
                                .titulo(
                                        "Stock bajo: "
                                                + item.getNombre()
                                )
                                .descripcion(
                                        "Cantidad disponible: "
                                                + item.getCantidad()
                                                + " "
                                                + item.getUnidad()
                                                + ". Mínimo requerido: "
                                                + item.getStockMinimo()
                                                + "."
                                )
                                .referenciaId(item.getId())
                                .leida(false)
                                .build()
                );
            }
        }
    }

    private void generarAlertasMedicas(
            List<AlertaDTO> alertas
    ) {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(30);

        for (
                HistorialMedico historial
                : historialMedicoRepository.findAll()
        ) {
            if (historial.getFechaProxima() == null) {
                continue;
            }

            boolean vencida =
                    historial.getFechaProxima().isBefore(hoy);

            boolean proxima =
                    !historial.getFechaProxima()
                            .isAfter(limite);

            if (!vencida && !proxima) {
                continue;
            }

            String tipoHistorial =
                    historial.getTipo() != null
                            ? historial.getTipo().toLowerCase()
                            : "";

            boolean esVacuna =
                    tipoHistorial.contains("vacuna");

            String nombreCaballo =
                    historial.getCaballo() != null
                            ? historial.getCaballo().getNombre()
                            : "Caballo";

            alertas.add(
                    AlertaDTO.builder()
                            .tipo(
                                    esVacuna
                                            ? "VACUNACION"
                                            : "TRATAMIENTO"
                            )
                            .severidad(
                                    vencida
                                            ? "ALTA"
                                            : "MEDIA"
                            )
                            .titulo(
                                    (vencida
                                            ? "Vencido: "
                                            : "Próximo: ")
                                            + historial.getTipo()
                            )
                            .descripcion(
                                    nombreCaballo
                                            + " requiere seguimiento el "
                                            + historial.getFechaProxima()
                                            + "."
                            )
                            .fecha(
                                    historial.getFechaProxima()
                            )
                            .referenciaId(historial.getId())
                            .leida(false)
                            .build()
            );
        }
    }

    private String crearClave(AlertaDTO alerta) {
        return alerta.getTipo()
                + "-"
                + alerta.getReferenciaId();
    }

    private AlertaDTO convertirADTO(Alerta alerta) {
        return AlertaDTO.builder()
                .id(alerta.getId())
                .tipo(alerta.getTipo())
                .severidad(alerta.getSeveridad())
                .titulo(alerta.getTitulo())
                .descripcion(alerta.getDescripcion())
                .fecha(alerta.getFecha())
                .referenciaId(alerta.getReferenciaId())
                .leida(alerta.getLeida())
                .fechaCreacion(alerta.getFechaCreacion())
                .build();
    }
}