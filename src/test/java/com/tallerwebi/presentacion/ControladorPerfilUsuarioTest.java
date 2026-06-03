package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.service.ServicioPerfilUsuario;
import com.tallerwebi.presentacion.controller.ControladorPerfilUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;

public class ControladorPerfilUsuarioTest {

    ServicioPerfilUsuario servicioMock = mock(ServicioPerfilUsuario.class);
    ControladorPerfilUsuario controladorPerfil = new ControladorPerfilUsuario(servicioMock);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    HttpSession sessionMock = mock(HttpSession.class);

    @Test
    public void siElUsuarioNoEstaLogueadoAlIntentarVerElPerfilDebeRedirigirAlLogin() {
        // given
        when(requestMock.getSession()).thenReturn(sessionMock);

        when(sessionMock.getAttribute("usuario")).thenReturn(null);

        // when
        ModelAndView mav = controladorPerfil.verPerfil(requestMock);

        // then
        assertThat(mav.getViewName(), equalTo("redirect:/login"));
    }

    @Test
    public void siElUsuarioEstaLogueadoPuedeVerLaVistaDeSuPerfilConSusDatos() {

        when(requestMock.getSession()).thenReturn(sessionMock);

        Usuario usuarioFalso = new Usuario();
        usuarioFalso.setEmail("brian@unlam.com.ar");
        usuarioFalso.setNombre("Brian");

        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioFalso);

        // when
        ModelAndView mav = controladorPerfil.verPerfil(requestMock);

        // then
        assertThat(mav.getViewName(), equalTo("perfil"));
        assertThat(mav.getModel().get("usuario"), notNullValue());
        assertThat(mav.getModel().get("usuario"), equalTo(usuarioFalso));
    }

    @Test
    public void siLosDatosSonValidosAlModificarlosCorrectamenteDebeRedirigirAlPerfil() {
        // given
        when(requestMock.getSession()).thenReturn(sessionMock);
        Usuario usuario = new Usuario();
        usuario.setEmail("test@unlam.com.ar");
        usuario.setPassword("test");
        usuario.setNombre("Brian");
        usuario.setApellido("Arrojas");
        usuario.setTelefono("1234567890");

        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        // when
        ModelAndView mav = controladorPerfil.actualizarPerfil(usuario, requestMock);

        // then
        assertThat(mav.getViewName(), equalTo("redirect:/mi-perfil"));
    }
}
