package com.tallerwebi.presentacion.controller;

import com.tallerwebi.dominio.service.ServicioChatPrivado;
import com.tallerwebi.dominio.service.ServicioDetalleMascota;
import com.tallerwebi.presentacion.dto.ChatDTO;
import com.tallerwebi.presentacion.dto.ComentarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ControladorChatSTOMP {

    private final ServicioChatPrivado servicioChatPrivado;
    private final ServicioDetalleMascota servicioDetalleMascota;

    @Autowired
    public ControladorChatSTOMP(ServicioChatPrivado servicioChatPrivado,
                                ServicioDetalleMascota servicioDetalleMascota) {
        this.servicioChatPrivado = servicioChatPrivado;
        this.servicioDetalleMascota = servicioDetalleMascota;
    }

    @MessageMapping("/mensaje")
    public void recibirMensaje(ChatDTO mensaje) {
        servicioChatPrivado.enviarMensaje(mensaje);
    }

    @MessageMapping("/comentario")
    public void recibirComentario(ComentarioDTO dto) {
        servicioDetalleMascota.publicarComentario(dto);
    }
}