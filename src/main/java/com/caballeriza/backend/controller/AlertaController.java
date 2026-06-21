package com.caballeriza.backend.controller;

import com.caballeriza.backend.dto.AlertaDTO;
import com.caballeriza.backend.service.AlertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AlertaController {

    private final AlertaService alertaService;

    @GetMapping
    public List<AlertaDTO> listar() {
        return alertaService.listar();
    }
}
