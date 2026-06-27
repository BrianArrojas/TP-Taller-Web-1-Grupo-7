package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.tallerwebi.dominio.model.Comentario;
import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.repository.RepositorioComentario;
import com.tallerwebi.dominio.repository.RepositorioReporteMascota;
import com.tallerwebi.dominio.service.ServicioChatPrivado;
import com.tallerwebi.dominio.service.impl.ServicioChatPrivadoImpl;
import com.tallerwebi.presentacion.dto.ChatDTO;
import com.tallerwebi.presentacion.dto.ConversacionDTO;
import com.tallerwebi.presentacion.dto.MensajeDTO;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServicioChatPrivadoTest {

    SimpMessagingTemplate mensajeroMock = mock(SimpMessagingTemplate.class);
    RepositorioComentario repositorioComentarioMock = mock(RepositorioComentario.class);
    RepositorioReporteMascota repositorioReporteMascotaMock = mock(RepositorioReporteMascota.class);

    ServicioChatPrivado servicioChat = new ServicioChatPrivadoImpl(
            mensajeroMock,
            repositorioComentarioMock,
            repositorioReporteMascotaMock
    );

    @Test
    public void enviarMensajeDebePersistirYPublicarEnChat() {
        // given
        Long idReporte = 1L;
        String codigoChat = UUID.randomUUID().toString();
        ChatDTO mensaje = new ChatDTO(idReporte, codigoChat, "Juan", "Hola");
        ReporteMascota reporte = new ReporteMascota();
        when(repositorioReporteMascotaMock.buscarPorId(idReporte)).thenReturn(reporte);

        // when
        servicioChat.enviarMensaje(mensaje);

        // then
        ArgumentCaptor<Comentario> captor = ArgumentCaptor.forClass(Comentario.class);
        verify(repositorioComentarioMock).guardar(captor.capture());
        Comentario guardado = captor.getValue();
        assertThat(guardado.getNombreRemitente(), equalTo("Juan"));
        assertThat(guardado.getTexto(), equalTo("Hola"));
        assertThat(guardado.getCodigoChat(), equalTo(codigoChat));

        verify(mensajeroMock).convertAndSend(eq("/chat/" + codigoChat), eq(mensaje));
    }

    @Test
    public void iniciarChatPrivadoCuandoNoExisteDebeCrearUno() {
        // given
        Long idReporte = 1L;
        Usuario interesado = new Usuario();
        interesado.setId(10L);
        interesado.setNombre("Juan");
        ReporteMascota reporte = new ReporteMascota();
        when(repositorioReporteMascotaMock.buscarPorId(idReporte)).thenReturn(reporte);
        when(repositorioComentarioMock.buscarChatDelInteresado(idReporte, 10L)).thenReturn(null);

        // when
        String codigoChat = servicioChat.iniciarChatPrivado(idReporte, interesado);

        // then
        ArgumentCaptor<Comentario> captor = ArgumentCaptor.forClass(Comentario.class);
        verify(repositorioComentarioMock).guardar(captor.capture());
        Comentario guardado = captor.getValue();
        assertThat(guardado.getNombreRemitente(), equalTo("Juan"));
        assertThat(guardado.getTexto(), equalTo("Chat iniciado"));
        assertThat(guardado.getCodigoChat(), notNullValue());
        assertThat(guardado.getIdInteresado(), equalTo(10L));
    }

    @Test
    public void iniciarChatPrivadoCuandoExisteDebeRetornarElCodigoExistente() {
        // given
        Long idReporte = 1L;
        Usuario interesado = new Usuario();
        interesado.setId(10L);
        ReporteMascota reporte = new ReporteMascota();
        Comentario existente = new Comentario();
        existente.setCodigoChat(UUID.randomUUID().toString());
        when(repositorioReporteMascotaMock.buscarPorId(idReporte)).thenReturn(reporte);
        when(repositorioComentarioMock.buscarChatDelInteresado(idReporte, 10L)).thenReturn(existente);

        // when
        String codigoChat = servicioChat.iniciarChatPrivado(idReporte, interesado);

        // then
        verify(repositorioComentarioMock, never()).guardar(any());
        assertThat(codigoChat, equalTo(existente.getCodigoChat()));
    }

    @Test
    public void obtenerHistorialDebeRetornarListaDeMensajesDTO() {
        // given
        String codigoChat = UUID.randomUUID().toString();
        List<Comentario> comentarios = new ArrayList<>();
        Comentario c = new Comentario();
        c.setNombreRemitente("Juan");
        c.setTexto("Mensaje");
        comentarios.add(c);
        when(repositorioComentarioMock.obtenerMensajesPorCodigoChat(codigoChat)).thenReturn(comentarios);

        // when
        List<MensajeDTO> resultado = servicioChat.obtenerHistorial(codigoChat);

        // then
        assertThat(resultado, hasSize(1));
        assertThat(resultado.get(0).getNombreRemitente(), equalTo("Juan"));
        assertThat(resultado.get(0).getTexto(), equalTo("Mensaje"));
    }

    @Test
    public void obtenerConversacionesPorReporteDebeAgruparPorCodigoChat() {
        // given
        Long idReporte = 1L;
        String codigoChat1 = UUID.randomUUID().toString();
        String codigoChat2 = UUID.randomUUID().toString();

        Comentario c1 = new Comentario();
        c1.setCodigoChat(codigoChat1);
        c1.setNombreRemitente("Juan");
        c1.setTexto("Chat iniciado");
        Comentario c2 = new Comentario();
        c2.setCodigoChat(codigoChat1);
        c2.setNombreRemitente("Juan");
        c2.setTexto("Último mensaje 1");
        Comentario c3 = new Comentario();
        c3.setCodigoChat(codigoChat2);
        c3.setNombreRemitente("María");
        c3.setTexto("Chat iniciado");
        Comentario c4 = new Comentario();
        c4.setCodigoChat(codigoChat2);
        c4.setNombreRemitente("María");
        c4.setTexto("Último mensaje 2");

        List<Comentario> mensajes = List.of(c1, c2, c3, c4);
        when(repositorioComentarioMock.obtenerTodosMensajesDelReporte(idReporte)).thenReturn(mensajes);

        // when
        List<ConversacionDTO> resultado = servicioChat.obtenerConversacionesPorReporte(idReporte);

        // then
        assertThat(resultado, hasSize(2));
        assertThat(resultado.get(0).getCodigoChat(), equalTo(codigoChat1));
        assertThat(resultado.get(0).getUltimoMensaje(), equalTo("Último mensaje 1"));
        assertThat(resultado.get(1).getCodigoChat(), equalTo(codigoChat2));
    }

    @Test
    public void puedeAccederAlChatSiEsDuenioDelReporte() {
        Long idReporte = 1L;
        String codigoChat = "uuid-123";
        Usuario duenio = new Usuario();
        duenio.setId(10L);
        ReporteMascota reporte = new ReporteMascota();
        reporte.setUsuario(duenio);
        when(repositorioReporteMascotaMock.buscarPorId(idReporte)).thenReturn(reporte);

        boolean resultado = servicioChat.puedeAccederAlChat(codigoChat, idReporte, duenio);

        assertThat(resultado, is(true));
        verify(repositorioComentarioMock, never()).obtenerMensajesPorCodigoChat(anyString());
    }

    @Test
    public void puedeAccederAlChatSiEsElInteresadoQueInicioElChat() {
        Long idReporte = 1L;
        String codigoChat = "uuid-123";
        Usuario duenio = new Usuario();
        duenio.setId(10L);
        Usuario interesado = new Usuario();
        interesado.setId(20L);
        ReporteMascota reporte = new ReporteMascota();
        reporte.setUsuario(duenio);
        when(repositorioReporteMascotaMock.buscarPorId(idReporte)).thenReturn(reporte);

        Comentario primerMensaje = new Comentario();
        primerMensaje.setIdInteresado(20L);
        List<Comentario> lista = new ArrayList<>();
        lista.add(primerMensaje);
        when(repositorioComentarioMock.obtenerMensajesPorCodigoChat(codigoChat)).thenReturn(lista);

        boolean resultado = servicioChat.puedeAccederAlChat(codigoChat, idReporte, interesado);

        assertThat(resultado, is(true));
    }

    @Test
    public void noPuedeAccederAlChatUnUsuarioDesconocido() {
        Long idReporte = 1L;
        String codigoChat = "uuid-123";
        Usuario duenio = new Usuario();
        duenio.setId(10L);
        Usuario intruso = new Usuario();
        intruso.setId(30L);
        ReporteMascota reporte = new ReporteMascota();
        reporte.setUsuario(duenio);
        when(repositorioReporteMascotaMock.buscarPorId(idReporte)).thenReturn(reporte);

        Comentario primerMensaje = new Comentario();
        primerMensaje.setIdInteresado(20L);
        List<Comentario> lista = new ArrayList<>();
        lista.add(primerMensaje);
        when(repositorioComentarioMock.obtenerMensajesPorCodigoChat(codigoChat)).thenReturn(lista);

        boolean resultado = servicioChat.puedeAccederAlChat(codigoChat, idReporte, intruso);

        assertThat(resultado, is(false));
    }
}