package com.tallerwebi.presentacion.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class Mascota {

  private String nombre;
  private String tipo;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date fecha;

  private String estado; // perdido o encontrado
  private String imagen;

  // Constructor sin argumentos requerido por Jackson
  public Mascota() {}

  public Mascota(String nombre, String tipo, Date fecha, String estado, String imagen) {
    this.nombre = nombre;
    this.tipo = tipo;
    this.fecha = fecha;
    this.estado = estado;
    this.imagen = imagen;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public Date getFecha() {
    return fecha;
  }

  public void setFecha(Date fecha) {
    this.fecha = fecha;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getImagen() {
    return imagen;
  }

  public void setImagen(String imagen) {
    this.imagen = imagen;
  }
}
