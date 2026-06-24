package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.model.Comentario;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.service.ServicioDetalleMascota;
import com.tallerwebi.presentacion.controller.ControladorDetalleMascota;
import com.tallerwebi.presentacion.dto.DatosDetalleMascotaDTO;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

public class ControladorDetalleMascotaTest {

    ServicioDetalleMascota servicioMock = mock(ServicioDetalleMascota.class);
    ControladorDetalleMascota controlador = new ControladorDetalleMascota(servicioMock);

    @Test
    public void verDetalleDebeAgregarMascotaYComentarios() {
        Long id = 1L;
        DatosDetalleMascotaDTO dto = new DatosDetalleMascotaDTO();
        List<Comentario> comentarios = new ArrayList<>();
        when(servicioMock.obtenerDetalle(id)).thenReturn(dto);
        when(servicioMock.obtenerComentariosPublicos(id)).thenReturn(comentarios);

        ModelAndView mav = controlador.verDetalle(id);

        assertThat(mav.getViewName(), equalTo("detalle-mascota"));
        assertThat(mav.getModel().get("mascota"), equalTo(dto));
        assertThat(mav.getModel().get("comentariosPublicos"), equalTo(comentarios));
    }

    @Test
    public void publicarComentarioRedirigeADetalle() {
        Long idReporte = 1L;
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.getSession().setAttribute("usuario", new Usuario());

        ModelAndView mav = controlador.publicarComentario(idReporte, "Hola", request);

        assertThat(mav.getViewName(), equalTo("redirect:/detalle/" + idReporte));
    }

    @Test
    public void publicarComentarioRedirigeALoginSiNoHaySesion() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        ModelAndView mav = controlador.publicarComentario(1L, "Hola", request);

        assertThat(mav.getViewName(), equalTo("redirect:/login"));
    }
}