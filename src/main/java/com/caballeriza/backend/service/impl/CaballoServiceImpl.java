package com.caballeriza.backend.service.impl;

import com.caballeriza.backend.model.Caballo;
import com.caballeriza.backend.model.User;
import com.caballeriza.backend.repository.CaballoRepository;
import com.caballeriza.backend.repository.UserRepository;
import com.caballeriza.backend.service.CaballoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CaballoServiceImpl implements CaballoService {

    private final CaballoRepository caballoRepository;
    private final UserRepository userRepository;

    @Override
    public List<Caballo> listar() {
        return caballoRepository.findAll();
    }

    @Override
    public List<Caballo> listarPorCliente(String email) {
        return caballoRepository.findByClienteEmail(email);
    }

    @Override
    public Caballo obtenerPorId(Long id) {
        return caballoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Caballo no encontrado"));
    }

    @Override
    public Caballo obtenerPorIdParaCliente(Long id, String email) {
        Caballo caballo = obtenerPorId(id);
        validarDueno(caballo, email);
        return caballo;
    }

    @Override
    public Caballo guardar(Caballo caballo) {
        if (caballoRepository.existsByIdentificador(caballo.getIdentificador())) {
            throw new RuntimeException("Ya existe un caballo con ese identificador");
        }
        return caballoRepository.save(caballo);
    }

    @Override
    public Caballo guardarParaCliente(Caballo caballo, String email) {
        User cliente = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        caballo.setCliente(cliente);
        return guardar(caballo);
    }

    @Override
    public Caballo actualizar(Long id, Caballo datos) {
        Caballo caballo = obtenerPorId(id);

        if (caballoRepository.existsByIdentificadorAndIdNot(datos.getIdentificador(), id)) {
            throw new RuntimeException("Ya existe un caballo con ese identificador");
        }

        caballo.setNombre(datos.getNombre());
        caballo.setIdentificador(datos.getIdentificador());
        caballo.setEdad(datos.getEdad());
        caballo.setRaza(datos.getRaza());
        caballo.setSexo(datos.getSexo());
        caballo.setPeso(datos.getPeso());
        caballo.setFotoUrl(datos.getFotoUrl());

        return caballoRepository.save(caballo);
    }

    @Override
    public Caballo actualizarParaCliente(Long id, Caballo datos, String email) {
        obtenerPorIdParaCliente(id, email);
        return actualizar(id, datos);
    }

    @Override
    public void eliminar(Long id) {
        Caballo caballo = obtenerPorId(id);
        caballoRepository.delete(caballo);
    }

    @Override
    public void eliminarParaCliente(Long id, String email) {
        Caballo caballo = obtenerPorIdParaCliente(id, email);
        caballoRepository.delete(caballo);
    }

    @Override
    public void verificarAccesoCliente(Long id, String email) {
        obtenerPorIdParaCliente(id, email);
    }

    private void validarDueno(Caballo caballo, String email) {
        String emailCliente = caballo.getCliente() != null
                ? caballo.getCliente().getEmail()
                : null;

        if (!Objects.equals(emailCliente, email)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No puede acceder a un caballo de otro cliente"
            );
        }
    }
}
