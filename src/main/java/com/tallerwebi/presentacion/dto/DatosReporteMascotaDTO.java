package com.tallerwebi.presentacion.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class DatosReporteMascotaDTO {

  private String tipoDeReporte;
  private String nombre;
  private String especie;
  private String raza;
  private String color;
  private String tamano;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate fecha;
  private String ubicacion;
  private String descripcion;
  private MultipartFile imagen;


  public DatosReporteMascotaDTO() {}

  public DatosReporteMascotaDTO(
    String nombre,
    String raza,
    String color,
    String descripcion,
    String ubicacion
  ) {
    this.nombre = nombre;
    this.raza = raza;
    this.color = color;
    this.descripcion = descripcion;
    this.ubicacion = ubicacion;
  }


  public MultipartFile getImagen() {
    return imagen;
  }

  public void setImagen(MultipartFile imagen) {
    this.imagen = imagen;
  }

  public String getTipoDeReporte() {
    return tipoDeReporte;
  }

  public void setTipoDeReporte(String tipoDeReporte) {
    this.tipoDeReporte = tipoDeReporte;
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

  public LocalDate getFecha() {
    return fecha;
  }

  public void setFecha(LocalDate fecha) {
    this.fecha = fecha;
  }
}
