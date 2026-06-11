package com.tallerwebi.dominio.service;

import com.tallerwebi.presentacion.dto.ChatDTO;

public interface ServicioChat {
    void enviarMensaje(ChatDTO mensaje);
}