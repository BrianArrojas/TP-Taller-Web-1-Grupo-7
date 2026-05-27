package com.tallerwebi.dominio.model;

import com.tallerwebi.presentacion.dto.Usuario;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class ReporteMascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoDeReporte;
    private String nombre;
    private String especie;
    private String raza;
    private String color;
    private String tamano;
    private LocalDate fecha; // Cuando se perdio/encontro
    private String ubicacion;
    private String descripcion;
    private LocalDateTime fechaCreacionReporte;
    private Boolean registroActivo;
    //FALTA LA IMAGEN

    @ManyToOne
    private Usuario usuario;


    public ReporteMascota() {
        this.fechaCreacionReporte = LocalDateTime.now();
        this.registroActivo = true;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getRegistroActivo() {
        return registroActivo;
    }

    public void setRegistroActivo(Boolean registroActivo) {
        this.registroActivo = registroActivo;
    }

    public LocalDateTime getFechaCreacionReporte() {
        return fechaCreacionReporte;
    }

    public void setFechaCreacionReporte(LocalDateTime fechaCreacionReporte) {
        this.fechaCreacionReporte = fechaCreacionReporte;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoDeReporte() {
        return tipoDeReporte;
    }

    public void setTipoDeReporte(String tipoDeReporte) {
        this.tipoDeReporte = tipoDeReporte;
    }
}
