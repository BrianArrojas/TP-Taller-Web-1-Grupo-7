package com.tallerwebi.presentacion;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.tallerwebi.dominio.service.ServicioChatPrivado;
import com.tallerwebi.dominio.service.ServicioDetalleMascota;
import com.tallerwebi.presentacion.controller.ControladorChatSTOMP;
import com.tallerwebi.presentacion.dto.ChatDTO;
import com.tallerwebi.presentacion.dto.ComentarioDTO;
import org.junit.jupiter.api.Test;

public class ControladorChatSTOMPTest {

    ServicioChatPrivado servicioChatMock = mock(ServicioChatPrivado.class);
    ServicioDetalleMascota servicioDetalleMascotaMock = mock(ServicioDetalleMascota.class);
    ControladorChatSTOMP controlador = new ControladorChatSTOMP(servicioChatMock, servicioDetalleMascotaMock);

    @Test
    public void alRecibirUnMensajeDebeLlamarAlServicio() {
        // given
        ChatDTO mensaje = new ChatDTO(1L, "uuid-123", "Juan", "Hola", null, null);

        // when
        controlador.recibirMensaje(mensaje);

        // then
        verify(servicioChatMock).enviarMensaje(mensaje);
    }

    @Test
    public void alRecibirUnComentarioDebeLlamarAlServicio() {
        // given
        ComentarioDTO comentario = new ComentarioDTO();
        comentario.setIdReporte(1L);
        comentario.setNombreRemitente("María");
        comentario.setTexto("Comentario de prueba");

        // when
        controlador.recibirComentario(comentario);

        // then
        verify(servicioDetalleMascotaMock).publicarComentario(comentario);
    }
}