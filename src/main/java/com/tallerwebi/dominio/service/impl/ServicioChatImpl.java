package com.tallerwebi.dominio.service.impl;

import com.tallerwebi.dominio.service.ServicioChat;
import com.tallerwebi.presentacion.dto.ChatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ServicioChatImpl implements ServicioChat {

    private final SimpMessagingTemplate mensajero;

    @Autowired
    public ServicioChatImpl(SimpMessagingTemplate mensajero) {
        this.mensajero = mensajero;
    }

    @Override
    public void enviarMensaje(ChatDTO mensaje) {
        String destino = "/sala/reporte/" + mensaje.getIdReporte();
        mensajero.convertAndSend(destino, mensaje);
    }
}