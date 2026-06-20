package com.caballeriza.backend.controller;

import com.caballeriza.backend.dto.EmpleadoDTO;
import com.caballeriza.backend.mapper.CaballerizaMapper;
import com.caballeriza.backend.model.Empleado;
import com.caballeriza.backend.service.EmpleadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @GetMapping
    public List<EmpleadoDTO> listar() {
        return empleadoService.listar()
                .stream()
                .map(CaballerizaMapper::toEmpleadoDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public EmpleadoDTO obtenerPorId(@PathVariable Long id) {
        Empleado empleado = empleadoService.obtenerPorId(id);
        return CaballerizaMapper.toEmpleadoDTO(empleado);
    }

    @PostMapping
    public EmpleadoDTO guardar(@Valid @RequestBody EmpleadoDTO empleadoDTO) {
        Empleado empleado = CaballerizaMapper.toEmpleadoEntity(empleadoDTO);
        Empleado guardado = empleadoService.guardar(empleado);
        return CaballerizaMapper.toEmpleadoDTO(guardado);
    }

    @PutMapping("/{id}")
    public EmpleadoDTO actualizar(@PathVariable Long id, @Valid @RequestBody EmpleadoDTO empleadoDTO) {
        Empleado empleado = CaballerizaMapper.toEmpleadoEntity(empleadoDTO);
        Empleado actualizado = empleadoService.actualizar(id, empleado);
        return CaballerizaMapper.toEmpleadoDTO(actualizado);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        empleadoService.eliminar(id);
    }
}