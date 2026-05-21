package com.tallerwebi.presentacion;

public class DatosDetalleMascotaDTO {

  private Long id;
  private String nombreMascota;
  private String especie;
  private String raza;
  private String fotoPath;
  private String descripcion;
  private String nombreDuenio;

  public DatosDetalleMascotaDTO() {}

  public DatosDetalleMascotaDTO(
    Long id,
    String nombreMascota,
    String especie,
    String raza,
    String fotoPath,
    String descripcion,
    String nombreDuenio
  ) {
    this.id = id;
    this.nombreMascota = nombreMascota;
    this.especie = especie;
    this.raza = raza;
    this.fotoPath = fotoPath;
    this.descripcion = descripcion;
    this.nombreDuenio = nombreDuenio;
  }

  // Getters y Setters (agregalos todos)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombreMascota() {
    return nombreMascota;
  }

  public void setNombreMascota(String nombreMascota) {
    this.nombreMascota = nombreMascota;
  }

  public String getEspecie() {
    return especie;
  }

  public void setEspecie(String especie) {
    this.especie = especie;
  }

  public String getRaza() {
    return raza;
  }

  public void setRaza(String raza) {
    this.raza = raza;
  }

  public String getFotoPath() {
    return fotoPath;
  }

  public void setFotoPath(String fotoPath) {
    this.fotoPath = fotoPath;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getNombreDuenio() {
    return nombreDuenio;
  }

  public void setNombreDuenio(String nombreDuenio) {
    this.nombreDuenio = nombreDuenio;
  }
}
