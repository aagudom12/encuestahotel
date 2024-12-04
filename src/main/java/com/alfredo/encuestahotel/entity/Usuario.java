package com.alfredo.encuestahotel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellidos;
    @NotNull(message = "El email es obligatorio")
    @Column(unique = true, nullable = false)
    private String email;
    @Column(length = 500)
    private String password;
    private String rol;

    @OneToMany(targetEntity = Encuesta.class, cascade = CascadeType.ALL, mappedBy = "usuario")
    private List<Encuesta> encuestas = new ArrayList<>();
}
