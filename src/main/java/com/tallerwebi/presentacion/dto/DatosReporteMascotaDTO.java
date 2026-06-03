package com.tallerwebi.presentacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
  private String rutaImagen;

  public String getFechaFormateada() {
    if (this.fecha == null) {
      return "";
    }
    return this.fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
  }
}
