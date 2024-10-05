package com.alfredo.encuestahotel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

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

    public Encuesta() {
    }

    public Encuesta(Long id, String nombre, String apellidos, String email, Integer edad, String telefono, LocalDate fechaEstancia, String motivoVisita, boolean restaurante, boolean gimnasio, boolean spa, boolean piscina, boolean roomservice, Integer nivelSatisfaccion, String otrosComentarios) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.edad = edad;
        this.telefono = telefono;
        this.fechaEstancia = fechaEstancia;
        this.motivoVisita = motivoVisita;
        this.restaurante = restaurante;
        this.gimnasio = gimnasio;
        this.spa = spa;
        this.piscina = piscina;
        this.roomservice = roomservice;
        this.nivelSatisfaccion = nivelSatisfaccion;
        this.otrosComentarios = otrosComentarios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public @NotNull(message = "La fecha no puede estar en blanco.") @PastOrPresent(message = "La fecha debe ser igual o anterior a la fecha de hoy.") LocalDate getFechaEstancia() {
        return fechaEstancia;
    }

    public void setFechaEstancia(@NotNull(message = "La fecha no puede estar en blanco.") @PastOrPresent(message = "La fecha debe ser igual o anterior a la fecha de hoy.") LocalDate fechaEstancia) {
        this.fechaEstancia = fechaEstancia;
    }

    public String getMotivoVisita() {
        return motivoVisita;
    }

    public void setMotivoVisita(String motivoVisita) {
        this.motivoVisita = motivoVisita;
    }

    public boolean isRestaurante() {
        return restaurante;
    }

    public void setRestaurante(boolean restaurante) {
        this.restaurante = restaurante;
    }

    public boolean isGimnasio() {
        return gimnasio;
    }

    public void setGimnasio(boolean gimnasio) {
        this.gimnasio = gimnasio;
    }

    public boolean isSpa() {
        return spa;
    }

    public void setSpa(boolean spa) {
        this.spa = spa;
    }

    public boolean isPiscina() {
        return piscina;
    }

    public void setPiscina(boolean piscina) {
        this.piscina = piscina;
    }

    public boolean isRoomservice() {
        return roomservice;
    }

    public void setRoomservice(boolean roomservice) {
        this.roomservice = roomservice;
    }

    public @NotNull(message = "El nivel de satisfacción es obligatorio.") Integer getNivelSatisfaccion() {
        return nivelSatisfaccion;
    }

    public void setNivelSatisfaccion(@NotNull(message = "El nivel de satisfacción es obligatorio.") Integer nivelSatisfaccion) {
        this.nivelSatisfaccion = nivelSatisfaccion;
    }

    public String getOtrosComentarios() {
        return otrosComentarios;
    }

    public void setOtrosComentarios(String otrosComentarios) {
        this.otrosComentarios = otrosComentarios;
    }

    @Override
    public String toString() {
        return "Encuesta{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", email='" + email + '\'' +
                ", edad=" + edad +
                ", telefono='" + telefono + '\'' +
                ", fechaEstancia=" + fechaEstancia +
                ", motivoVisita='" + motivoVisita + '\'' +
                ", restaurante=" + restaurante +
                ", gimnasio=" + gimnasio +
                ", spa=" + spa +
                ", piscina=" + piscina +
                ", roomservice=" + roomservice +
                ", nivelSatisfaccion='" + nivelSatisfaccion + '\'' +
                ", otrosComentarios='" + otrosComentarios + '\'' +
                '}';
    }
}
