package com.tallerwebi.presentacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ComentarioDTO {

    private Long idReporte;

    private String nombreRemitente;

    private String texto;
    
}