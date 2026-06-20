package com.caballeriza.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "inventario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String tipo;

    @PositiveOrZero
    private Integer cantidad;

    @PositiveOrZero
    private Integer stockMinimo;

    private String unidad;

    public Boolean getStockBajo() {
        return cantidad != null && stockMinimo != null && cantidad <= stockMinimo;
    }
}
