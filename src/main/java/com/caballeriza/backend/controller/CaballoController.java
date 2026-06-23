package com.caballeriza.backend.controller;

import com.caballeriza.backend.dto.CaballoDTO;
import com.caballeriza.backend.mapper.CaballerizaMapper;
import com.caballeriza.backend.model.Caballo;
import com.caballeriza.backend.service.CaballoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/caballos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CaballoController {

    private final CaballoService caballoService;

    @GetMapping
    public List<CaballoDTO> listar(Authentication authentication) {
        List<Caballo> caballos = esCliente(authentication)
                ? caballoService.listarPorCliente(authentication.getName())
                : caballoService.listar();

        return caballos
                .stream()
                .map(CaballerizaMapper::toCaballoDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public CaballoDTO obtenerPorId(@PathVariable Long id, Authentication authentication) {
        Caballo caballo = esCliente(authentication)
                ? caballoService.obtenerPorIdParaCliente(id, authentication.getName())
                : caballoService.obtenerPorId(id);
        return CaballerizaMapper.toCaballoDTO(caballo);
    }

    @PostMapping
    public CaballoDTO guardar(@Valid @RequestBody CaballoDTO caballoDTO, Authentication authentication) {
        Caballo caballo = CaballerizaMapper.toCaballoEntity(caballoDTO);
        Caballo guardado = esCliente(authentication)
                ? caballoService.guardarParaCliente(caballo, authentication.getName())
                : caballoService.guardar(caballo);
        return CaballerizaMapper.toCaballoDTO(guardado);
    }

    @PutMapping("/{id}")
    public CaballoDTO actualizar(@PathVariable Long id, @Valid @RequestBody CaballoDTO caballoDTO, Authentication authentication) {
        Caballo caballo = CaballerizaMapper.toCaballoEntity(caballoDTO);
        Caballo actualizado = esCliente(authentication)
                ? caballoService.actualizarParaCliente(id, caballo, authentication.getName())
                : caballoService.actualizar(id, caballo);
        return CaballerizaMapper.toCaballoDTO(actualizado);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id, Authentication authentication) {
        if (esCliente(authentication)) {
            caballoService.eliminarParaCliente(id, authentication.getName());
            return;
        }

        caballoService.eliminar(id);
    }

    private boolean esCliente(Authentication authentication) {
        return authentication != null
                && authentication.getAuthorities()
                .stream()
                .anyMatch(authority -> "ROLE_CLIENTE".equals(authority.getAuthority()));
    }
}
