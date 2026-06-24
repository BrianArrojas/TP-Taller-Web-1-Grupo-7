package com.tallerwebi.dominio.service;

import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.presentacion.dto.ChatDTO;
import com.tallerwebi.presentacion.dto.ConversacionDTO;
import com.tallerwebi.presentacion.dto.MensajeDTO;

import java.util.List;

public interface ServicioChatPrivado {

    void enviarMensaje(ChatDTO mensaje);
    
    String iniciarChatPrivado(Long idReporte, Usuario interesado);

    boolean puedeAccederAlChat(String codigoChat, Long idReporte, Usuario usuario);

    List<MensajeDTO> obtenerHistorial(String codigoChat);

    List<ConversacionDTO> obtenerConversacionesPorReporte(Long idReporte);
}