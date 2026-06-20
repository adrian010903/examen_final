package com.caballeriza.backend.mapper;

import com.caballeriza.backend.dto.*;
import com.caballeriza.backend.model.*;

public class CaballerizaMapper {

    public static CaballoDTO toCaballoDTO(Caballo caballo) {
        return CaballoDTO.builder()
                .id(caballo.getId())
                .nombre(caballo.getNombre())
                .identificador(caballo.getIdentificador())
                .edad(caballo.getEdad())
                .raza(caballo.getRaza())
                .sexo(caballo.getSexo())
                .peso(caballo.getPeso())
                .fotoUrl(caballo.getFotoUrl())
                .build();
    }

    public static Caballo toCaballoEntity(CaballoDTO dto) {
        return Caballo.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .identificador(dto.getIdentificador())
                .edad(dto.getEdad())
                .raza(dto.getRaza())
                .sexo(dto.getSexo())
                .peso(dto.getPeso())
                .fotoUrl(dto.getFotoUrl())
                .build();
    }

    public static EmpleadoDTO toEmpleadoDTO(Empleado empleado) {
        return EmpleadoDTO.builder()
                .id(empleado.getId())
                .nombre(empleado.getNombre())
                .rol(empleado.getRol())
                .contacto(empleado.getContacto())
                .build();
    }

    public static Empleado toEmpleadoEntity(EmpleadoDTO dto) {
        return Empleado.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .rol(dto.getRol())
                .contacto(dto.getContacto())
                .build();
    }

    public static HistorialMedicoDTO toHistorialMedicoDTO(HistorialMedico historial) {
        return HistorialMedicoDTO.builder()
                .id(historial.getId())
                .tipo(historial.getTipo())
                .descripcion(historial.getDescripcion())
                .alergias(historial.getAlergias())
                .observaciones(historial.getObservaciones())
                .fecha(historial.getFecha())
                .responsable(historial.getResponsable())
                .caballoId(historial.getCaballo() != null ? historial.getCaballo().getId() : null)
                .nombreCaballo(historial.getCaballo() != null ? historial.getCaballo().getNombre() : null)
                .build();
    }

    public static TurnoDTO toTurnoDTO(Turno turno) {
        return TurnoDTO.builder()
                .id(turno.getId())
                .fecha(turno.getFecha())
                .horaInicio(turno.getHoraInicio())
                .horaFin(turno.getHoraFin())
                .empleadoId(turno.getEmpleado() != null ? turno.getEmpleado().getId() : null)
                .nombreEmpleado(turno.getEmpleado() != null ? turno.getEmpleado().getNombre() : null)
                .build();
    }

    public static TareaDTO toTareaDTO(Tarea tarea) {
        return TareaDTO.builder()
                .id(tarea.getId())
                .descripcion(tarea.getDescripcion())
                .fecha(tarea.getFecha())
                .completada(tarea.getCompletada())
                .empleadoId(tarea.getEmpleado() != null ? tarea.getEmpleado().getId() : null)
                .nombreEmpleado(tarea.getEmpleado() != null ? tarea.getEmpleado().getNombre() : null)
                .caballoId(tarea.getCaballo() != null ? tarea.getCaballo().getId() : null)
                .nombreCaballo(tarea.getCaballo() != null ? tarea.getCaballo().getNombre() : null)
                .build();
    }

    public static PlanAlimentacionDTO toPlanAlimentacionDTO(PlanAlimentacion plan) {
        return PlanAlimentacionDTO.builder()
                .id(plan.getId())
                .tipoAlimento(plan.getTipoAlimento())
                .cantidad(plan.getCantidad())
                .frecuencia(plan.getFrecuencia())
                .observaciones(plan.getObservaciones())
                .caballoId(plan.getCaballo() != null ? plan.getCaballo().getId() : null)
                .nombreCaballo(plan.getCaballo() != null ? plan.getCaballo().getNombre() : null)
                .build();
    }

    public static SuministroDTO toSuministroDTO(Suministro suministro) {
        return SuministroDTO.builder()
                .id(suministro.getId())
                .fecha(suministro.getFecha())
                .tipo(suministro.getTipo())
                .cantidad(suministro.getCantidad())
                .caballoId(suministro.getCaballo() != null ? suministro.getCaballo().getId() : null)
                .nombreCaballo(suministro.getCaballo() != null ? suministro.getCaballo().getNombre() : null)
                .build();
    }

    public static InventarioDTO toInventarioDTO(Inventario inventario) {
        return InventarioDTO.builder()
                .id(inventario.getId())
                .nombre(inventario.getNombre())
                .tipo(inventario.getTipo())
                .cantidad(inventario.getCantidad())
                .stockMinimo(inventario.getStockMinimo())
                .unidad(inventario.getUnidad())
                .stockBajo(inventario.getStockBajo())
                .build();
    }

    public static Inventario toInventarioEntity(InventarioDTO dto) {
        return Inventario.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .tipo(dto.getTipo())
                .cantidad(dto.getCantidad())
                .stockMinimo(dto.getStockMinimo())
                .unidad(dto.getUnidad())
                .build();
    }
}