package com.tallerwebi.presentacion.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversacionDTO {

    private String codigoChat;

    private String nombreInteresado;

    private String ultimoMensaje;

    private LocalDateTime fechaUltimoMensaje;
    
}