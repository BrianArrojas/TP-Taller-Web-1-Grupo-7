package com.tallerwebi.presentacion.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mascota {

  private String nombre;
  private String tipo;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date fecha;

  private String estado; // perdido o encontrado
  private String imagen;
}
