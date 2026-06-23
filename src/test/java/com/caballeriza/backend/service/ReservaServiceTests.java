package com.caballeriza.backend.service;

import com.caballeriza.backend.model.*;
import com.caballeriza.backend.repository.CaballoRepository;
import com.caballeriza.backend.repository.EmpleadoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReservaServiceTests {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private CaballoRepository caballoRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Test
    void guardaYCancelaReserva() {
        Caballo caballo = caballoRepository.save(Caballo.builder()
                .nombre("Relampago")
                .identificador("TEST-RES-001")
                .edad(7)
                .raza("Cuarto de milla")
                .sexo("Macho")
                .peso(470.0)
                .build());

        Empleado empleado = empleadoRepository.save(Empleado.builder()
                .nombre("Ana Rojas")
                .rol(RolEmpleado.CUIDADOR)
                .contacto("ana@test.com")
                .build());

        Reserva reserva = reservaService.guardar(Reserva.builder()
                .tipo(TipoReserva.PASEO)
                .fecha(LocalDate.now().plusDays(1))
                .horaInicio(LocalTime.of(8, 0))
                .horaFin(LocalTime.of(9, 0))
                .cliente("Cliente prueba")
                .cantidadPersonas(1)
                .cupoMaximo(10)
                .caballo(Caballo.builder().id(caballo.getId()).build())
                .empleado(Empleado.builder().id(empleado.getId()).build())
                .build());

        assertThat(reserva.getId()).isNotNull();
        assertThat(reserva.getEstado()).isEqualTo(EstadoReserva.PROGRAMADA);

        Reserva cancelada = reservaService.cancelar(reserva.getId());

        assertThat(cancelada.getEstado()).isEqualTo(EstadoReserva.CANCELADA);
    }
}
