package com.alfredo.encuestahotel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
@Table(name = "encuestas")
public class Encuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEncuesta;

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

    @NotNull(message = "El motivo de visita es obligatorio.")
    private String motivoVisita;

    private boolean isRestaurante;
    private boolean isGimnasio;
    private boolean isSpa;
    private boolean isPiscina;
    private boolean isRoomservice;

    @NotNull(message = "El nivel de satisfacción es obligatorio.")
    private String nivelSatisfaccion;
    private String otrosComentarios;

    public Encuesta() {
    }

    public Encuesta(Long idEncuesta, String nombre, String apellidos, String email, Integer edad, String telefono, LocalDate fechaEstancia, String motivoVisita, Boolean isRestaurante, Boolean isGimnasio, Boolean isSpa, Boolean isPiscina, Boolean isRoomservice, String nivelSatisfaccion, String otrosComentarios) {
        this.idEncuesta = idEncuesta;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.edad = edad;
        this.telefono = telefono;
        this.fechaEstancia = fechaEstancia;
        this.motivoVisita = motivoVisita;
        this.isRestaurante = isRestaurante;
        this.isGimnasio = isGimnasio;
        this.isSpa = isSpa;
        this.isPiscina = isPiscina;
        this.isRoomservice = isRoomservice;
        this.nivelSatisfaccion = nivelSatisfaccion;
        this.otrosComentarios = otrosComentarios;
    }

    public Long getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(Long idEncuesta) {
        this.idEncuesta = idEncuesta;
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

    public @NotEmpty(message = "La fecha no puede estar en blanco.") @PastOrPresent(message = "La fecha debe ser igual o anterior a la fecha de hoy.") LocalDate getFechaEstancia() {
        return fechaEstancia;
    }

    public void setFechaEstancia(@NotEmpty(message = "La fecha no puede estar en blanco.") @PastOrPresent(message = "La fecha debe ser igual o anterior a la fecha de hoy.") LocalDate fechaEstancia) {
        this.fechaEstancia = fechaEstancia;
    }

    public String getMotivoVisita() {
        return motivoVisita;
    }

    public void setMotivoVisita(String motivoVisita) {
        this.motivoVisita = motivoVisita;
    }

    public boolean isRestaurante() {
        return isRestaurante;
    }

    public void setRestaurante(boolean restaurante) {
        isRestaurante = restaurante;
    }

    public boolean isGimnasio() {
        return isGimnasio;
    }

    public void setGimnasio(boolean gimnasio) {
        isGimnasio = gimnasio;
    }

    public boolean isSpa() {
        return isSpa;
    }

    public void setSpa(boolean spa) {
        isSpa = spa;
    }

    public boolean isPiscina() {
        return isPiscina;
    }

    public void setPiscina(boolean piscina) {
        isPiscina = piscina;
    }

    public boolean isRoomservice() {
        return isRoomservice;
    }

    public void setRoomservice(boolean roomservice) {
        isRoomservice = roomservice;
    }

    public String getNivelSatisfaccion() {
        return nivelSatisfaccion;
    }

    public void setNivelSatisfaccion(String nivelSatisfaccion) {
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
                "idEncuesta=" + idEncuesta +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", email='" + email + '\'' +
                ", edad=" + edad +
                ", telefono='" + telefono + '\'' +
                ", fechaEstancia='" + fechaEstancia + '\'' +
                ", motivoVisita='" + motivoVisita + '\'' +
                ", isRestaurante=" + isRestaurante +
                ", isGimnasio=" + isGimnasio +
                ", isSpa=" + isSpa +
                ", isPiscina=" + isPiscina +
                ", isRoomservice=" + isRoomservice +
                ", nivelSatisfaccion='" + nivelSatisfaccion + '\'' +
                ", otrosComentarios='" + otrosComentarios + '\'' +
                '}';
    }
}
