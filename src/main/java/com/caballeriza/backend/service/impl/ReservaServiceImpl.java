package com.caballeriza.backend.service.impl;

import com.caballeriza.backend.model.*;
import com.caballeriza.backend.repository.CaballoRepository;
import com.caballeriza.backend.repository.EmpleadoRepository;
import com.caballeriza.backend.repository.ReservaRepository;
import com.caballeriza.backend.repository.UserRepository;
import com.caballeriza.backend.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.Comparator;
import java.util.Objects;


import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final CaballoRepository caballoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final UserRepository userRepository;

    @Override
    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }

    @Override
    public List<Reserva> listarPorCliente(String email) {
        return reservaRepository.findByClienteUsuarioEmail(email)
                .stream()
                .sorted(
                        Comparator.comparing(Reserva::getFecha)
                                .thenComparing(Reserva::getHoraInicio)
                )
                .toList();
    }

    @Override
    public List<Reserva> listarPorRango(LocalDate inicio, LocalDate fin) {
        return reservaRepository.findByFechaBetween(inicio, fin);
    }
    @Override
    public List<Reserva> filtrar(
            TipoReserva tipo,
            LocalDate fecha,
            EstadoReserva estado,
            LocalDate inicio,
            LocalDate fin
    ) {
        return reservaRepository.findAll()
                .stream()
                .filter(reserva ->
                        tipo == null || reserva.getTipo() == tipo
                )
                .filter(reserva ->
                        fecha == null || reserva.getFecha().equals(fecha)
                )
                .filter(reserva ->
                        estado == null || reserva.getEstado() == estado
                )
                .filter(reserva ->
                        inicio == null || !reserva.getFecha().isBefore(inicio)
                )
                .filter(reserva ->
                        fin == null || !reserva.getFecha().isAfter(fin)
                )
                .sorted(
                        Comparator.comparing(Reserva::getFecha)
                                .thenComparing(Reserva::getHoraInicio)
                )
                .toList();
    }

    @Override
    public Reserva obtenerPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    @Override
    public Reserva obtenerPorIdParaCliente(Long id, String email) {
        Reserva reserva = obtenerPorId(id);
        validarReservaDeCliente(reserva, email);
        return reserva;
    }

    @Override
    public Reserva guardar(Reserva reserva) {
        asignarRelaciones(reserva);

        if (reserva.getEstado() == null) {
            reserva.setEstado(EstadoReserva.PROGRAMADA);
        }

        validarReserva(reserva, null);

        return reservaRepository.save(reserva);
    }

    @Override
    public Reserva guardarParaCliente(Reserva reserva, String email) {
        User cliente = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        reserva.setClienteUsuario(cliente);
        reserva.setCliente(cliente.getNombre());
        reserva.setEmpleado(null);

        return guardar(reserva);
    }

    @Override
    public Reserva actualizar(Long id, Reserva datos) {
        Reserva reserva = obtenerPorId(id);

        reserva.setTipo(datos.getTipo());
        reserva.setEstado(
                datos.getEstado() != null
                        ? datos.getEstado()
                        : reserva.getEstado()
        );
        reserva.setFecha(datos.getFecha());
        reserva.setHoraInicio(datos.getHoraInicio());
        reserva.setHoraFin(datos.getHoraFin());
        reserva.setCliente(datos.getCliente());
        reserva.setObservaciones(datos.getObservaciones());

        reserva.setCantidadPersonas(datos.getCantidadPersonas());
        reserva.setCupoMaximo(datos.getCupoMaximo());

        reserva.setCaballo(datos.getCaballo());
        reserva.setEmpleado(datos.getEmpleado());

        asignarRelaciones(reserva);
        validarReserva(reserva, id);

        return reservaRepository.save(reserva);
    }

    @Override
    public Reserva actualizarParaCliente(Long id, Reserva datos, String email) {
        Reserva reserva = obtenerPorIdParaCliente(id, email);
        datos.setClienteUsuario(reserva.getClienteUsuario());
        datos.setCliente(reserva.getCliente());
        datos.setEmpleado(null);
        return actualizar(id, datos);
    }

    @Override
    public Reserva cancelar(Long id) {
        Reserva reserva = obtenerPorId(id);
        reserva.setEstado(EstadoReserva.CANCELADA);
        return reservaRepository.save(reserva);
    }

    @Override
    public Reserva cancelarParaCliente(Long id, String email) {
        Reserva reserva = obtenerPorIdParaCliente(id, email);
        reserva.setEstado(EstadoReserva.CANCELADA);
        return reservaRepository.save(reserva);
    }

    @Override
    public void eliminar(Long id) {
        reservaRepository.delete(obtenerPorId(id));
    }

    private void validarReserva(
            Reserva reserva,
            Long idReservaActual
    ) {
        if (!reserva.getHoraFin().isAfter(reserva.getHoraInicio())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La hora final debe ser posterior a la hora inicial"
            );
        }

        if (reserva.getCantidadPersonas() == null) {
            reserva.setCantidadPersonas(1);
        }

        if (reserva.getTipo() != TipoReserva.PASEO) {
            reserva.setCantidadPersonas(1);
            reserva.setCupoMaximo(null);
            return;
        }

        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            return;
        }

        if (
                reserva.getCupoMaximo() == null
                        || reserva.getCupoMaximo() < 1
        ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe indicar el cupo máximo del paseo"
            );
        }

        List<Reserva> reservasDelMismoPaseo =
                reservaRepository
                        .findByTipoAndFechaAndHoraInicioAndHoraFinAndEstadoNot(
                                TipoReserva.PASEO,
                                reserva.getFecha(),
                                reserva.getHoraInicio(),
                                reserva.getHoraFin(),
                                EstadoReserva.CANCELADA
                        )
                        .stream()
                        .filter(actual ->
                                idReservaActual == null
                                        || !Objects.equals(
                                        actual.getId(),
                                        idReservaActual
                                )
                        )
                        .toList();

        Integer cupoExistente = reservasDelMismoPaseo
                .stream()
                .map(Reserva::getCupoMaximo)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        if (cupoExistente != null) {
            reserva.setCupoMaximo(cupoExistente);
        }

        int personasReservadas = reservasDelMismoPaseo
                .stream()
                .mapToInt(actual ->
                        actual.getCantidadPersonas() == null
                                ? 1
                                : actual.getCantidadPersonas()
                )
                .sum();

        int totalNuevo =
                personasReservadas + reserva.getCantidadPersonas();

        if (totalNuevo > reserva.getCupoMaximo()) {
            int disponibles =
                    reserva.getCupoMaximo() - personasReservadas;

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No hay cupos suficientes. Disponibles: "
                            + Math.max(disponibles, 0)
            );
        }
    }

    private void asignarRelaciones(Reserva reserva) {
        Long caballoId = reserva.getCaballo() != null ? reserva.getCaballo().getId() : null;
        if (caballoId == null) {
            throw new RuntimeException("Caballo no encontrado");
        }

        Caballo caballo = caballoRepository.findById(caballoId)
                .orElseThrow(() -> new RuntimeException("Caballo no encontrado"));
        reserva.setCaballo(caballo);

        Long empleadoId = reserva.getEmpleado() != null ? reserva.getEmpleado().getId() : null;
        if (empleadoId != null) {
            Empleado empleado = empleadoRepository.findById(empleadoId)
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
            reserva.setEmpleado(empleado);
        } else {
            reserva.setEmpleado(null);
        }
    }

    private void validarReservaDeCliente(Reserva reserva, String email) {
        String emailCliente = reserva.getClienteUsuario() != null
                ? reserva.getClienteUsuario().getEmail()
                : null;

        if (!Objects.equals(emailCliente, email)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No puede acceder a una reserva de otro cliente"
            );
        }
    }
}
