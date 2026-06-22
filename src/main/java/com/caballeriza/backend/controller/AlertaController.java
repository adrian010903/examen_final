package com.caballeriza.backend.controller;

import com.caballeriza.backend.dto.AlertaDTO;
import com.caballeriza.backend.service.AlertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AlertaController {

    private final AlertaService alertaService;

    @GetMapping
    public List<AlertaDTO> listar(
            @RequestParam(required = false)
            Boolean leida
    ) {
        return alertaService.listar(leida);
    }

    @GetMapping("/no-leidas/count")
    public Map<String, Long> contarNoLeidas() {
        return Map.of(
                "cantidad",
                alertaService.contarNoLeidas()
        );
    }

    @PatchMapping("/{id}/marcar-leida")
    public AlertaDTO marcarLeida(
            @PathVariable Long id
    ) {
        return alertaService.marcarLeida(id);
    }

    @PatchMapping("/{id}/marcar-no-leida")
    public AlertaDTO marcarNoLeida(
            @PathVariable Long id
    ) {
        return alertaService.marcarNoLeida(id);
    }
}