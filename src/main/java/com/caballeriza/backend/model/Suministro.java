package com.caballeriza.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "suministros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Suministro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate fecha;

    @NotBlank
    private String tipo;

    @NotBlank
    private String cantidad;

    @ManyToOne
    @JoinColumn(name = "caballo_id", nullable = false)
    private Caballo caballo;
}