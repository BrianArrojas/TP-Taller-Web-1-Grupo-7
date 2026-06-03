package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.presentacion.controller.ControladorPerfilUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorPerfilUsuarioTest {

    ControladorPerfilUsuario controladorPerfil = new ControladorPerfilUsuario();
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

    }

