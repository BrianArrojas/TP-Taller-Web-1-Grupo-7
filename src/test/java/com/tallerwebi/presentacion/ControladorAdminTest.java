package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

public class ControladorAdminTest {

  private ControladorAdmin controladorAdmin;
  private HttpServletRequest requestMock;
  private HttpSession sessionMock;

  @BeforeEach
  public void init() {
    requestMock = mock(HttpServletRequest.class);
    sessionMock = mock(HttpSession.class);
    controladorAdmin = new ControladorAdmin();
  }

  @Test
  public void dadoQueUnUsuarioTieneRolAdminCuandoIntentaAccederALaSeccionAdministracionEntoncesAccedeExitosamente() {
    //Preparo
    when(requestMock.getSession()).thenReturn(sessionMock);
    when(sessionMock.getAttribute("ROL")).thenReturn("ADMIN");
    //Ejecucin
    ModelAndView mav = controladorAdmin.irAAdministracion(requestMock);
    //Valido
    assertThat(mav.getViewName(), equalToIgnoringCase("Administracion"));
  }

  @Test
    public void dadoQueUnUsuarioNoEsAdminCuandoIntentaAccederALaSeccionAdministracionEntoncesEsRedirigidoAlHome(){
      when(requestMock.getSession()).thenReturn(sessionMock);
      when(sessionMock.getAttribute("ROL")).thenReturn("INVITADO");

      ModelAndView mavv = controladorAdmin.irAAdministracion(requestMock);

      assertThat(mavv.getViewName(),equalToIgnoringCase("redirect:/home"));

  }
}
