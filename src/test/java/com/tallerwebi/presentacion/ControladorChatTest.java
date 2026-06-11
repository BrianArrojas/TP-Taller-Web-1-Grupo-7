package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.service.ServicioChat;
import com.tallerwebi.presentacion.controller.ControladorChatSTOMP;
import com.tallerwebi.presentacion.dto.ChatDTO;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ControladorChatTest {

    ServicioChat servicioChatMock = mock(ServicioChat.class);
    ControladorChatSTOMP controladorChat = new ControladorChatSTOMP(servicioChatMock);

    @Test
    public void alRecibirUnMensajeDebeLlamarAlServicio() {
        ChatDTO mensaje = new ChatDTO(1L, "Juan", "Hola");

        controladorChat.recibirMensaje(mensaje);

        verify(servicioChatMock).enviarMensaje(mensaje);
    }
}