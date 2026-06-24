package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.service.ServicioReporteMascota;
import com.tallerwebi.presentacion.controller.ControladorMapaReportes;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;


import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;


public class ControladorMapaReportesTest {

    private ServicioReporteMascota servicioMock = mock(ServicioReporteMascota.class);
    private ControladorMapaReportes controladorMapaReportes = new ControladorMapaReportes(servicioMock);

    @Test
    public void alIrAlMapaDeberiaRetornarLaVistaYLosDatos() {
        // Given
        List<ReporteMascota> reportes = List.of(new ReporteMascota());
        when(servicioMock.obtenerTodosLosReportesActivos()).thenReturn(reportes);

        // When
        ModelAndView mav = controladorMapaReportes.verMapa();

        // Then
        assertThat(mav.getViewName(), equalTo("mapa-reportes"));
        assertThat(mav.getModel().get("reportes"), is(notNullValue()));
        assertThat((List<?>)mav.getModel().get("reportes"), hasSize(1));
    }
}
