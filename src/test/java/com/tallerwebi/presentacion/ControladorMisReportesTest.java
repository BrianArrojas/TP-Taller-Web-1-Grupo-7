package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.service.ServicioReporteMascota;
import com.tallerwebi.presentacion.controller.ControladorMisReportes;;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;

public class ControladorMisReportesTest {

    ServicioReporteMascota servicioMock = mock(ServicioReporteMascota.class);
    ControladorMisReportes controladorMisReportes= new ControladorMisReportes(servicioMock);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    HttpSession sessionMock = mock(HttpSession.class);



    @Test
    public void alEntrarAMisReportesDebeMostrarLaVistaYLosDatos() {
        // given
        Usuario usuario = new Usuario();
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        when(servicioMock.buscarPorUsuario(usuario)).thenReturn(new ArrayList<ReporteMascota>());
        when(requestMock.getSession()).thenReturn(sessionMock);

        // when
        ModelAndView mav = controladorMisReportes.mostrarMisReportes(requestMock);

        // then
        assertThat(mav.getViewName(), equalTo("mis-reportes"));
        assertThat(mav.getModel().get("reportes"), is(notNullValue()));
    }

    @Test
    public void alCancelarReporteCorrectamenteSeRedirigeAMisReportes() {
        // Given
        Long idReporte = 1L;

        // When
        ModelAndView mav = controladorMisReportes.cancelarReporte(idReporte);

        // Then
        assertThat(mav.getViewName(), equalTo("redirect:/mis-reportes"));
    }
}


