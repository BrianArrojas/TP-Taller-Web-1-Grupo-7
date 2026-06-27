package com.tallerwebi.presentacion;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.tallerwebi.dominio.service.ServicioChatPrivado;
import com.tallerwebi.presentacion.controller.ControladorChatSTOMP;
import com.tallerwebi.presentacion.dto.ChatDTO;
import org.junit.jupiter.api.Test;

public class ControladorChatSTOMPTest {

    ServicioChatPrivado servicioChatMock = mock(ServicioChatPrivado.class);
    ControladorChatSTOMP controlador = new ControladorChatSTOMP(servicioChatMock);

    @Test
    public void alRecibirUnMensajeDebeLlamarAlServicio() {
        ChatDTO mensaje = new ChatDTO(1L, "uuid-123", "Juan", "Hola");

        controlador.recibirMensaje(mensaje);

        verify(servicioChatMock).enviarMensaje(mensaje);
    }
}