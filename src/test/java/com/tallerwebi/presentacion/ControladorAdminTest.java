package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.tallerwebi.dominio.service.ServicioAdmin;
import com.tallerwebi.presentacion.controller.ControladorAdmin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

public class ControladorAdminTest {

  private ControladorAdmin controladorAdmin;
  private HttpServletRequest requestMock;
  private HttpSession sessionMock;
  private ServicioAdmin servicioAdminMock;

  @BeforeEach
  public void init() {
    requestMock = mock(HttpServletRequest.class);
    sessionMock = mock(HttpSession.class);
    servicioAdminMock = mock(ServicioAdmin.class);
    controladorAdmin = new ControladorAdmin(servicioAdminMock);
  }

  @Test
  public void dadoQueUnUsuarioTieneRolAdminCuandoIntentaAccederALaSeccionAdministracionEntoncesAccedeExitosamente() {
    //Preparo
    when(requestMock.getSession()).thenReturn(sessionMock);
    when(sessionMock.getAttribute("ROL")).thenReturn("ADMIN");
    //Ejecucin
    ModelAndView mav = controladorAdmin.irAAdministracion(requestMock, null);
    //Valido
    assertThat(mav.getViewName(), equalToIgnoringCase("Administracion"));
    verify(servicioAdminMock, times(1)).buscarUsuariosPorEmail(null);
    verify(servicioAdminMock, times(1)).obtenerPublicacionesActivas();
  }

  @Test
  public void dadoQueUnUsuarioNoEsAdminCuandoIntentaAccederALaSeccionAdministracionEntoncesEsRedirigidoAlHome(){
      when(requestMock.getSession()).thenReturn(sessionMock);
      when(sessionMock.getAttribute("ROL")).thenReturn("INVITADO");

      ModelAndView mavv = controladorAdmin.irAAdministracion(requestMock, null);

      assertThat(mavv.getViewName(),equalToIgnoringCase("redirect:/home"));
  }

  @Test
  public void dadoQueUnUsuarioTieneRolAdminCuandoIntentaModificarEstadoUsuarioEntoncesSeEjecutaYRedirigeAAdmin() {
      when(requestMock.getSession()).thenReturn(sessionMock);
      when(sessionMock.getAttribute("ROL")).thenReturn("ADMIN");

      ModelAndView mav = controladorAdmin.toggleUsuarioActivo(requestMock, 1L);

      assertThat(mav.getViewName(), equalToIgnoringCase("redirect:/admin"));
      verify(servicioAdminMock, times(1)).toggleUsuarioActivo(1L);
  }

  @Test
  public void dadoQueUnUsuarioNoEsAdminCuandoIntentaModificarEstadoUsuarioEntoncesNoSeEjecutaYRedirigeAHome() {
      when(requestMock.getSession()).thenReturn(sessionMock);
      when(sessionMock.getAttribute("ROL")).thenReturn("INVITADO");

      ModelAndView mav = controladorAdmin.toggleUsuarioActivo(requestMock, 1L);

      assertThat(mav.getViewName(), equalToIgnoringCase("redirect:/home"));
      verify(servicioAdminMock, times(0)).toggleUsuarioActivo(anyLong());
  }

  @Test
  public void dadoQueUnUsuarioTieneRolAdminCuandoIntentaEliminarPublicacionEntoncesSeEjecutaYRedirigeAAdmin() {
      when(requestMock.getSession()).thenReturn(sessionMock);
      when(sessionMock.getAttribute("ROL")).thenReturn("ADMIN");

      ModelAndView mav = controladorAdmin.eliminarPublicacion(requestMock, 2L);

      assertThat(mav.getViewName(), equalToIgnoringCase("redirect:/admin"));
      verify(servicioAdminMock, times(1)).eliminarPublicacion(2L);
  }

  @Test
  public void dadoQueUnUsuarioNoEsAdminCuandoIntentaEliminarPublicacionEntoncesNoSeEjecutaYRedirigeAHome() {
      when(requestMock.getSession()).thenReturn(sessionMock);
      when(sessionMock.getAttribute("ROL")).thenReturn("INVITADO");

      ModelAndView mav = controladorAdmin.eliminarPublicacion(requestMock, 2L);

      assertThat(mav.getViewName(), equalToIgnoringCase("redirect:/home"));
      verify(servicioAdminMock, times(0)).eliminarPublicacion(anyLong());
  }
}
