package com.tallerwebi.presentacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatosDetalleMascotaDTO {

  private Long id;
  private String nombreMascota;
  private String especie;
  private String raza;
  private String fotoPath;
  private String descripcion;
  private String nombreDuenio;
}
