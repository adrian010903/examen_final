package com.caballeriza.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "caballos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Caballo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    @Column(unique = true)
    private String identificador;

    @Min(0)
    private Integer edad;

    @NotBlank
    private String raza;

    @NotBlank
    private String sexo;

    @Positive
    private Double peso;

    private String fotoUrl;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private User cliente;
}
