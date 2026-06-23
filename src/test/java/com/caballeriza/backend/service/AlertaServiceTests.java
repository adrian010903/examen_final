package com.caballeriza.backend.service;

import com.caballeriza.backend.dto.AlertaDTO;
import com.caballeriza.backend.model.Caballo;
import com.caballeriza.backend.model.HistorialMedico;
import com.caballeriza.backend.model.Inventario;
import com.caballeriza.backend.repository.CaballoRepository;
import com.caballeriza.backend.repository.HistorialMedicoRepository;
import com.caballeriza.backend.repository.InventarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AlertaServiceTests {

    @Autowired
    private AlertaService alertaService;

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private CaballoRepository caballoRepository;

    @Autowired
    private HistorialMedicoRepository historialMedicoRepository;

    @Test
    void generaAlertasDeStockYControlMedico() {
        inventarioRepository.save(Inventario.builder()
                .nombre("Desinfectante")
                .tipo("Limpieza")
                .cantidad(2)
                .stockMinimo(5)
                .unidad("litros")
                .build());

        Caballo caballo = caballoRepository.save(Caballo.builder()
                .nombre("Aurora")
                .identificador("TEST-ALERT-001")
                .edad(5)
                .raza("Cuarto de milla")
                .sexo("Hembra")
                .peso(430.0)
                .build());

        historialMedicoRepository.save(HistorialMedico.builder()
                .tipo("Vacunacion")
                .descripcion("Refuerzo anual")
                .fecha(LocalDate.now())
                .fechaProxima(LocalDate.now().plusDays(10))
                .responsable("Dra. Solis")
                .caballo(caballo)
                .build());

        List<AlertaDTO> alertas = alertaService.listar(null);

        assertThat(alertas).anyMatch(alerta -> "STOCK_BAJO".equals(alerta.getTipo()));
        assertThat(alertas).anyMatch(alerta -> "VACUNACION".equals(alerta.getTipo()));
    }
}
