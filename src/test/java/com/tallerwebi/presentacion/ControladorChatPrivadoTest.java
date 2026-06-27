package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.service.ServicioChatPrivado;
import com.tallerwebi.presentacion.controller.ControladorChatPrivado;
import com.tallerwebi.presentacion.dto.MensajeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public class ControladorChatPrivadoTest {

    ServicioChatPrivado servicioChatPrivadoMock = mock(ServicioChatPrivado.class);
    ControladorChatPrivado controlador = new ControladorChatPrivado(servicioChatPrivadoMock);

    @Test
    public void iniciarChatDebeRedirigirAlChatPrivadoConCodigoChat() {
        Long idReporte = 1L;
        String codigoChat = "uuid-123";
        MockHttpServletRequest request = new MockHttpServletRequest();
        Usuario interesado = new Usuario();
        request.getSession().setAttribute("usuario", interesado);
        when(servicioChatPrivadoMock.iniciarChatPrivado(idReporte, interesado)).thenReturn(codigoChat);

        ModelAndView mav = controlador.iniciarChat(idReporte, request);

        assertThat(mav.getViewName(),
                equalTo("redirect:/chat-privado?codigoChat=" + codigoChat + "&idReporte=" + idReporte));
    }

    @Test
    public void iniciarChatDebeRedirigirAlLoginSiNoHaySesion() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ModelAndView mav = controlador.iniciarChat(1L, request);
        assertThat(mav.getViewName(), equalTo("redirect:/login"));
    }

    @Test
    public void debeMostrarChatPrivadoConHistorialYRemitente() {
        String codigoChat = "uuid-123";
        Long idReporte = 1L;
        MockHttpServletRequest request = new MockHttpServletRequest();
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan");
        request.getSession().setAttribute("usuario", usuario);

        when(servicioChatPrivadoMock.puedeAccederAlChat(codigoChat, idReporte, usuario)).thenReturn(true);
        List<MensajeDTO> historial = new ArrayList<>();
        when(servicioChatPrivadoMock.obtenerHistorial(codigoChat)).thenReturn(historial);

        ModelAndView mav = controlador.mostrarChatPrivado(codigoChat, idReporte, request);

        assertThat(mav.getViewName(), equalTo("chat-privado"));
        assertThat(mav.getModel().get("codigoChat"), equalTo(codigoChat));
        assertThat(mav.getModel().get("idReporte"), equalTo(idReporte));
        assertThat(mav.getModel().get("remitente"), equalTo("Juan"));
        assertThat(mav.getModel().get("historial"), equalTo(historial));
    }

    @Test
    public void debeRedirigirAlLoginSiNoHaySesionAlMostrarChat() {
        String codigoChat = "uuid-123";
        Long idReporte = 1L;
        MockHttpServletRequest request = new MockHttpServletRequest();

        ModelAndView mav = controlador.mostrarChatPrivado(codigoChat, idReporte, request);

        assertThat(mav.getViewName(), equalTo("redirect:/login"));
    }

    @Test
    public void debeRedirigirAlHomeSiUsuarioNoPerteneceAlChat() {
        String codigoChat = "uuid-123";
        Long idReporte = 1L;
        MockHttpServletRequest request = new MockHttpServletRequest();
        Usuario usuario = new Usuario();
        request.getSession().setAttribute("usuario", usuario);

        when(servicioChatPrivadoMock.puedeAccederAlChat(codigoChat, idReporte, usuario)).thenReturn(false);

        ModelAndView mav = controlador.mostrarChatPrivado(codigoChat, idReporte, request);

        assertThat(mav.getViewName(), equalTo("redirect:/home"));
    }
}