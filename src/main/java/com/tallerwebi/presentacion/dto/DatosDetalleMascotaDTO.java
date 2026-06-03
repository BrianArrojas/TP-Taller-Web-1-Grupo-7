package com.tallerwebi.presentacion.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatosDetalleMascotaDTO {
    private Long id;
    private String nombre;
    private String especie;
    private String raza;
    private String color;
    private String tamano;
    private LocalDate fecha;
    private String ubicacion;
    private String descripcion;
    private String fotoUrl;          
    private String nombreDuenio;     
    private String tipoDeReporte;    
    
}
