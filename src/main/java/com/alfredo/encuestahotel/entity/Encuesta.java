package com.alfredo.encuestahotel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

//Anotaciones de LomBok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "encuestas")
public class Encuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty (message = "El nombre no puede estar vacío.")
    @Size (min = 2, message = "El nombre debe de tener al menos 2 caracteres.")
    private String nombre;

    @NotEmpty (message = "Los apellidos no pueden estar vacíos.")
    @Size (min = 2, message = "Los apellidos deben de tener al menos 2 caracteres.")
    private String apellidos;

    @NotEmpty (message = "El email no puede estar vacío.")
    @Email (message = "El formato del email es inválido.")
    private String email;

    @NotNull(message = "La edad no puede estar en blanco.")
    @Min(value = 18, message = "La edad debe ser al menos 18 años.")
    private Integer edad;

    @NotEmpty(message = "El teléfono no puede estar en blanco.")
    private String telefono;

    @NotNull(message = "La fecha no puede estar en blanco.")
    @PastOrPresent(message = "La fecha debe ser igual o anterior a la fecha de hoy.")
    private LocalDate fechaEstancia;

    @NotEmpty(message = "El motivo de visita es obligatorio.")
    private String motivoVisita;

    private boolean restaurante;
    private boolean gimnasio;
    private boolean spa;
    private boolean piscina;
    private boolean roomservice;

    @NotNull(message = "El nivel de satisfacción es obligatorio.")
    private Integer nivelSatisfaccion;

    private String otrosComentarios;

    @ManyToOne(targetEntity = Usuario.class, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
