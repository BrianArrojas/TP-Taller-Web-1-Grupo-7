package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.tallerwebi.dominio.service.ServicioChatPrivado;
import com.tallerwebi.dominio.service.ServicioDetalleMascota;
import com.tallerwebi.presentacion.controller.ControladorAPIHistorial;
import com.tallerwebi.presentacion.dto.MensajeDTO;

public class ControladorAPIHistorialTest {

    ServicioChatPrivado servicioChatMock = mock(ServicioChatPrivado.class);
    ServicioDetalleMascota servicioDetalleMock = mock(ServicioDetalleMascota.class);

    ControladorAPIHistorial controlador = new ControladorAPIHistorial(servicioChatMock, servicioDetalleMock);

    @Test
    public void obtenerHistorialDebeRetornarListaDeMensajesDTO() {
        // given
        String codigoChat = "uuid-123";
        List<MensajeDTO> historialMock = new ArrayList<>();
        historialMock.add(new MensajeDTO("Juan", "Hola", "2026-07-01T10:00:00"));
        when(servicioChatMock.obtenerHistorial(codigoChat)).thenReturn(historialMock);

        // when
        List<MensajeDTO> resultado = controlador.obtenerHistorial(codigoChat);

        // then
        assertThat(resultado, equalTo(historialMock));
        assertThat(resultado.get(0).getNombreRemitente(), equalTo("Juan"));
    }

    @Test
    public void obtenerComentariosPublicosDebeRetornarListaDeMensajesDTO() {
        // given
        Long idReporte = 1L;
        List<MensajeDTO> comentariosMock = new ArrayList<>();
        comentariosMock.add(new MensajeDTO("María", "Comentario", "2026-07-01T10:05:00"));
        when(servicioDetalleMock.obtenerComentariosPublicos(idReporte)).thenReturn(comentariosMock);

        // when
        List<MensajeDTO> resultado = controlador.obtenerComentariosPublicos(idReporte);

        // then
        assertThat(resultado, equalTo(comentariosMock));
        assertThat(resultado.get(0).getTexto(), equalTo("Comentario"));
    }
}