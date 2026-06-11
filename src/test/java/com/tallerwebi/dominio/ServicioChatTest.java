package com.tallerwebi.dominio;

import com.tallerwebi.dominio.service.impl.ServicioChatImpl;
import com.tallerwebi.presentacion.dto.ChatDTO;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ServicioChatTest {

    SimpMessagingTemplate mensajeroMock = mock(SimpMessagingTemplate.class);
    ServicioChatImpl servicioChat = new ServicioChatImpl(mensajeroMock);

    @Test
    public void alEnviarUnMensajeSePublicaEnLaSalaDelReporte() {
        Long idReporte = 1L;
        ChatDTO mensaje = new ChatDTO(idReporte, "Juan", "Hola!");

        servicioChat.enviarMensaje(mensaje);

        verify(mensajeroMock).convertAndSend("/sala/reporte/" + idReporte, mensaje);
    } 
}