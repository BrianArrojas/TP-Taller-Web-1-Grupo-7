package com.tallerwebi.presentacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDTO {

    private Long idReporte;
    private String remitente;
    private String contenido;
}