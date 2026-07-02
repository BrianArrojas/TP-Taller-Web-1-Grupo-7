package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
        Long idReporte = 1L;
        String codigoChat = UUID.randomUUID().toString();
        ChatDTO mensaje = new ChatDTO(idReporte, codigoChat, "Juan", "Hola", null, null);
        ReporteMascota reporte = new ReporteMascota();
        when(repositorioReporteMascotaMock.buscarPorId(idReporte)).thenReturn(reporte);

        servicioChat.enviarMensaje(mensaje);

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
        Long idReporte = 1L;
        ChatDTO chat = new ChatDTO();
        chat.setIdReporte(idReporte);
        chat.setIdInteresado(10L);
        chat.setRemitente("Juan");

        ReporteMascota reporte = new ReporteMascota();
        when(repositorioReporteMascotaMock.buscarPorId(idReporte)).thenReturn(reporte);
        when(repositorioComentarioMock.obtenerCodigoChatExistente(idReporte, 10L)).thenReturn(null);

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
        Long idReporte = 1L;
        ChatDTO chat = new ChatDTO();
        chat.setIdReporte(idReporte);
        chat.setIdInteresado(10L);

        ReporteMascota reporte = new ReporteMascota();
        String codigoExistente = UUID.randomUUID().toString();
        when(repositorioReporteMascotaMock.buscarPorId(idReporte)).thenReturn(reporte);
        when(repositorioComentarioMock.obtenerCodigoChatExistente(idReporte, 10L)).thenReturn(codigoExistente);

        String codigoChat = servicioChat.iniciarChatPrivado(chat);

        verify(repositorioComentarioMock, never()).guardar(any());
        assertThat(codigoChat, equalTo(codigoExistente));
    }

    @Test
    public void obtenerHistorialDebeRetornarListaDeMensajesDTO() {
        String codigoChat = UUID.randomUUID().toString();
        List<Comentario> comentarios = new ArrayList<>();
        Comentario c = new Comentario();
        c.setNombreRemitente("Juan");
        c.setTexto("Mensaje");
        comentarios.add(c);
        when(repositorioComentarioMock.obtenerMensajesPorCodigoChat(codigoChat)).thenReturn(comentarios);

        List<MensajeDTO> resultado = servicioChat.obtenerHistorial(codigoChat);

        assertThat(resultado, hasSize(1));
        assertThat(resultado.get(0).getNombreRemitente(), equalTo("Juan"));
        assertThat(resultado.get(0).getTexto(), equalTo("Mensaje"));
    }

    @Test
    public void obtenerConversacionesPorReporteDebeAgruparPorCodigoChat() {
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

        List<ConversacionDTO> resultado = servicioChat.obtenerConversacionesPorReporte(idReporte);

        assertThat(resultado, hasSize(2));
        assertThat(resultado.get(0).getCodigoChat(), equalTo(codigoChat1));
        assertThat(resultado.get(0).getUltimoMensaje(), equalTo("Último mensaje 1"));
        assertThat(resultado.get(1).getCodigoChat(), equalTo(codigoChat2));
    }

    @Test
    public void puedeAccederAlChatSiEsDuenioDelReporte() {
        Long idReporte = 1L;
        ChatDTO chat = new ChatDTO();
        chat.setIdReporte(idReporte);
        Usuario duenio = new Usuario();
        duenio.setId(10L);
        ReporteMascota reporte = new ReporteMascota();
        reporte.setUsuario(duenio);
        when(repositorioReporteMascotaMock.buscarPorId(idReporte)).thenReturn(reporte);

        boolean resultado = servicioChat.puedeAccederAlChat(chat, duenio);

        assertThat(resultado, is(true));
        verify(repositorioComentarioMock, never()).obtenerCodigoChatExistente(anyLong(), anyLong());
    }

    @Test
    public void puedeAccederAlChatSiEsElInteresadoQueInicioElChat() {
        Long idReporte = 1L;
        ChatDTO chat = new ChatDTO();
        chat.setIdReporte(idReporte);
        chat.setIdInteresado(20L);
        Usuario duenio = new Usuario();
        duenio.setId(10L);
        Usuario interesado = new Usuario();
        interesado.setId(20L);
        ReporteMascota reporte = new ReporteMascota();
        reporte.setUsuario(duenio);
        when(repositorioReporteMascotaMock.buscarPorId(idReporte)).thenReturn(reporte);
        when(repositorioComentarioMock.obtenerCodigoChatExistente(idReporte, 20L)).thenReturn("uuid-123");

        boolean resultado = servicioChat.puedeAccederAlChat(chat, interesado);

        assertThat(resultado, is(true));
    }

    @Test
    public void noPuedeAccederAlChatUnUsuarioDesconocido() {
        Long idReporte = 1L;
        ChatDTO chat = new ChatDTO();
        chat.setIdReporte(idReporte);
        chat.setIdInteresado(20L);
        Usuario duenio = new Usuario();
        duenio.setId(10L);
        Usuario intruso = new Usuario();
        intruso.setId(30L);
        ReporteMascota reporte = new ReporteMascota();
        reporte.setUsuario(duenio);
        when(repositorioReporteMascotaMock.buscarPorId(idReporte)).thenReturn(reporte);
        when(repositorioComentarioMock.obtenerCodigoChatExistente(idReporte, 20L)).thenReturn(null);

        boolean resultado = servicioChat.puedeAccederAlChat(chat, intruso);

        assertThat(resultado, is(false));
    }
}