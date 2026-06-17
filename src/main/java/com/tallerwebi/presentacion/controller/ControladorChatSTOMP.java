package com.tallerwebi.presentacion.controller;

import com.tallerwebi.dominio.service.ServicioChat;
import com.tallerwebi.presentacion.dto.ChatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ControladorChatSTOMP {

    private ServicioChat servicioChat;

    @Autowired
    public ControladorChatSTOMP(ServicioChat servicioChat) {
        this.servicioChat = servicioChat;
    }

    @MessageMapping("/mensaje")
    public void recibirMensaje(ChatDTO mensaje) {
        servicioChat.enviarMensaje(mensaje);
    }
}