package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.excepcion.FechaInvalidaException;
import com.tallerwebi.dominio.excepcion.FormatoImagenInvalidaException;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.service.ServicioReporteMascota;
import com.tallerwebi.presentacion.controller.ControladorReporteMascota;
import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;

public class ControladorReporteMascotaTest {

  ServicioReporteMascota servicioReporteMascota = mock(ServicioReporteMascota.class);
  ControladorReporteMascota controladorReporteMascota = new ControladorReporteMascota(servicioReporteMascota);

  HttpServletRequest request = mock(HttpServletRequest.class);
  HttpSession session = mock(HttpSession.class);

  @Test
  public void siGeneroUnReporteCorrectamenteSeMuestraLaVistaConLaInformacionDelMismo() {
    DatosReporteMascotaDTO datosReporteMascotaDTO = new DatosReporteMascotaDTO();
    datosReporteMascotaDTO.setNombre("Brian");
    datosReporteMascotaDTO.setRaza("Dogo");
    datosReporteMascotaDTO.setColor("Blanco");
    datosReporteMascotaDTO.setDescripcion("Esta Lastimado");
    datosReporteMascotaDTO.setUbicacion("San Justo");
    datosReporteMascotaDTO.setTipoDeReporte("Perdido");
    datosReporteMascotaDTO.setTamano("Grande");
    datosReporteMascotaDTO.setEspecie("Perro");
    datosReporteMascotaDTO.setFecha(LocalDate.now().minusDays(1));
    MockMultipartFile fotoSimulada = new MockMultipartFile("foto", "perrito.png", "image/png", "bytes-de-imagen".getBytes());
    datosReporteMascotaDTO.setImagen(fotoSimulada);

    Usuario usuarioSimulado = new Usuario();
    usuarioSimulado.setEmail("brian@test.com");

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("usuario")).thenReturn(usuarioSimulado);


    when(servicioReporteMascota.validarQueLaImagenCumplaConFormato(datosReporteMascotaDTO)).thenReturn(true);
    when(servicioReporteMascota.validarQueFechaDeReporteNoSeaFutura(any(DatosReporteMascotaDTO.class))).thenReturn(true);

    ModelAndView mav = whenRealizarReporteMascota(datosReporteMascotaDTO,request);

    thenReporteExitoso(mav, "El reporte se realizo correctamente");
  }

  private void thenReporteExitoso(ModelAndView mav, String mensaje) {
    assertThat(mav.getViewName(), equalToIgnoringCase("lista-de-reportes"));
    assertThat(mav.getModel().get("mensaje").toString(), equalToIgnoringCase(mensaje));
  }

  private ModelAndView whenRealizarReporteMascota(DatosReporteMascotaDTO datosReporteMascotaDTO,HttpServletRequest request) {
    ModelAndView mav = controladorReporteMascota.realizarReporte(datosReporteMascotaDTO,request);
    return mav;
  }

  private void givenReporteNoExiste() {}

  @Test
  public void alEnviarUnReporteConCamposVaciosElMismoFalla() {
    givenReporteNoExiste();
    DatosReporteMascotaDTO datosReporteMascotaDTO = new DatosReporteMascotaDTO();
    datosReporteMascotaDTO.setNombre("");
    datosReporteMascotaDTO.setRaza("");
    datosReporteMascotaDTO.setColor("");
    datosReporteMascotaDTO.setDescripcion("");
    datosReporteMascotaDTO.setUbicacion("");
    datosReporteMascotaDTO.setTipoDeReporte("Perdido");
    datosReporteMascotaDTO.setTamano("Grande");
    datosReporteMascotaDTO.setEspecie("Perro");
    datosReporteMascotaDTO.setFecha(LocalDate.of(2025, 4, 25));
    MockMultipartFile fotoSimulada = new MockMultipartFile("foto", "perrito.png", "image/png", "bytes-de-imagen".getBytes());
    datosReporteMascotaDTO.setImagen(fotoSimulada);


    Usuario usuarioSimulado = new Usuario();
    usuarioSimulado.setEmail("brian@test.com");

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("usuario")).thenReturn(usuarioSimulado);


    when(servicioReporteMascota.validarQueLaImagenCumplaConFormato(datosReporteMascotaDTO)).thenReturn(true);
    when(servicioReporteMascota.validarQueFechaDeReporteNoSeaFutura(any(DatosReporteMascotaDTO.class))).thenReturn(true);

    ModelAndView mav = whenRealizarReporteMascota(datosReporteMascotaDTO,request);
    thenReporteNoSeRealiza(
      mav,
      datosReporteMascotaDTO,
      "Debe completar todos los campos para realizar el reporte"
    );
  }

  private void thenReporteNoSeRealiza(
    ModelAndView mav,
    DatosReporteMascotaDTO datosReporteMascotaDTO,
    String mensaje
  ) {
    assertThat(datosReporteMascotaDTO.getNombre(), is((emptyOrNullString())));
    assertThat(datosReporteMascotaDTO.getDescripcion(), is((emptyOrNullString())));
    assertThat(datosReporteMascotaDTO.getRaza(), is((emptyOrNullString())));
    assertThat(datosReporteMascotaDTO.getColor(), is((emptyOrNullString())));
    assertThat(datosReporteMascotaDTO.getUbicacion(), is((emptyOrNullString())));
    assertThat(mav.getViewName(), equalToIgnoringCase("realizar-reporte"));
    assertThat(mav.getModel().get("mensaje").toString(), equalToIgnoringCase(mensaje));
  }


  @Test
  public void siNoSeleccionoAlgunaDeLasOpcionesDesplegablesDelFormularioElMismoFalla() {
    givenReporteNoExiste();
    DatosReporteMascotaDTO datosReporteMascotaDTO = new DatosReporteMascotaDTO();
    datosReporteMascotaDTO.setNombre("Brian");
    datosReporteMascotaDTO.setRaza("Dogo");
    datosReporteMascotaDTO.setColor("Blanco");
    datosReporteMascotaDTO.setDescripcion("Esta Lastimado");
    datosReporteMascotaDTO.setUbicacion("San Justo");

    Usuario usuarioSimulado = new Usuario();
    usuarioSimulado.setEmail("brian@test.com");

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("usuario")).thenReturn(usuarioSimulado);


    when(servicioReporteMascota.validarQueLaImagenCumplaConFormato(datosReporteMascotaDTO)).thenReturn(true);
    when(servicioReporteMascota.validarQueFechaDeReporteNoSeaFutura(any(DatosReporteMascotaDTO.class))).thenReturn(true);

    ModelAndView mav = whenRealizarReporteMascota(datosReporteMascotaDTO,request);
    thenReporteNoSeRealizaSiNoSeleccionoOpcionDeDesplegable(mav,datosReporteMascotaDTO,"Debe seleccionar una opcion de todos los desplegables");

  }

  private void thenReporteNoSeRealizaSiNoSeleccionoOpcionDeDesplegable(ModelAndView mav, DatosReporteMascotaDTO datosReporteMascotaDTO, String mensaje) {

    assertThat(datosReporteMascotaDTO.getTipoDeReporte(), is((emptyOrNullString())));
    assertThat(datosReporteMascotaDTO.getTamano(), is((emptyOrNullString())));
    assertThat(datosReporteMascotaDTO.getEspecie(), is((emptyOrNullString())));
    assertThat(mav.getViewName(), equalToIgnoringCase("realizar-reporte"));
    assertThat(mav.getModel().get("mensaje").toString(), equalToIgnoringCase(mensaje));


  }

  @Test
  public void siNoSeAdjuntaUnaImagenElReporteFalla() {
    // Given
    DatosReporteMascotaDTO datosReporteMascotaDTO = new DatosReporteMascotaDTO();
    datosReporteMascotaDTO.setNombre("Brian");
    datosReporteMascotaDTO.setRaza("Dogo");
    datosReporteMascotaDTO.setColor("Blanco");
    datosReporteMascotaDTO.setDescripcion("Esta Lastimado");
    datosReporteMascotaDTO.setUbicacion("San Justo");
    datosReporteMascotaDTO.setTipoDeReporte("Perdido");
    datosReporteMascotaDTO.setTamano("Grande");
    datosReporteMascotaDTO.setEspecie("Perro");
    datosReporteMascotaDTO.setImagen(null);

    when(servicioReporteMascota.validarQueLaImagenCumplaConFormato(datosReporteMascotaDTO)).thenReturn(true);
    // When

    Usuario usuarioSimulado = new Usuario();
    usuarioSimulado.setEmail("brian@test.com");

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("usuario")).thenReturn(usuarioSimulado);


    when(servicioReporteMascota.validarQueLaImagenCumplaConFormato(datosReporteMascotaDTO)).thenReturn(true);
    when(servicioReporteMascota.validarQueFechaDeReporteNoSeaFutura(any(DatosReporteMascotaDTO.class))).thenReturn(true);

    ModelAndView mav = whenRealizarReporteMascota(datosReporteMascotaDTO,request);
    // Then
    thenReporteFalla(mav,"Debe adjuntar una imagen");

    }

  private void thenReporteFalla(ModelAndView mav, String mensaje) {
    assertThat(mav.getViewName(), equalToIgnoringCase("realizar-reporte"));
    assertThat(mav.getModel().get("mensaje").toString(), equalToIgnoringCase(mensaje));
  }

  @Test
  public void siNoSeRespetoFormatoDeImagenElReporteFalla() {
    // Given
    DatosReporteMascotaDTO datosReporteMascotaDTO = new DatosReporteMascotaDTO();
    datosReporteMascotaDTO.setNombre("Brian");
    datosReporteMascotaDTO.setRaza("Dogo");
    datosReporteMascotaDTO.setColor("Blanco");
    datosReporteMascotaDTO.setDescripcion("Esta Lastimado");
    datosReporteMascotaDTO.setUbicacion("San Justo");
    datosReporteMascotaDTO.setTipoDeReporte("Perdido");
    datosReporteMascotaDTO.setTamano("Grande");
    datosReporteMascotaDTO.setEspecie("Perro");
    datosReporteMascotaDTO.setFecha(LocalDate.now().minusDays(1));

    // Simulamos una foto real pasando: nombre del parámetro, nombre del archivo, tipo, y el contenido en bytes
    MockMultipartFile fotoSimulada = new MockMultipartFile("foto", "perrito.pdf", "image/pdf", "bytes-de-pdf".getBytes());
    datosReporteMascotaDTO.setImagen(fotoSimulada);
      when(servicioReporteMascota.validarQueLaImagenCumplaConFormato(org.mockito.Mockito.any())).thenThrow(new FormatoImagenInvalidaException());

    // When

    Usuario usuarioSimulado = new Usuario();
    usuarioSimulado.setEmail("brian@test.com");

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("usuario")).thenReturn(usuarioSimulado);

    when(servicioReporteMascota.validarQueFechaDeReporteNoSeaFutura(any(DatosReporteMascotaDTO.class))).thenReturn(true);

    ModelAndView mav = whenRealizarReporteMascota(datosReporteMascotaDTO,request);
    // Then
    thenReporteFalla(mav,"El formato de la foto debe ser JPG o PNG");

  }


  @Test
  public void siLaFechaDelReporteEsFuturaElMismoFalla(){
    // Given
    DatosReporteMascotaDTO datosReporteMascotaDTO = new DatosReporteMascotaDTO();
    datosReporteMascotaDTO.setNombre("Brian");
    datosReporteMascotaDTO.setRaza("Dogo");
    datosReporteMascotaDTO.setColor("Blanco");
    datosReporteMascotaDTO.setDescripcion("Esta Lastimado");
    datosReporteMascotaDTO.setUbicacion("San Justo");
    datosReporteMascotaDTO.setTipoDeReporte("Perdido");
    datosReporteMascotaDTO.setTamano("Grande");
    datosReporteMascotaDTO.setEspecie("Perro");
    datosReporteMascotaDTO.setFecha(LocalDate.now().plusDays(2));

    // Simulamos una foto real pasando: nombre del parámetro, nombre del archivo, tipo, y el contenido en bytes
    MockMultipartFile fotoSimulada = new MockMultipartFile("foto", "perrito.png", "image/png", "bytes-de-png".getBytes());
    datosReporteMascotaDTO.setImagen(fotoSimulada);
    when(servicioReporteMascota.validarQueLaImagenCumplaConFormato(datosReporteMascotaDTO)).thenReturn(true);
    when(servicioReporteMascota.validarQueFechaDeReporteNoSeaFutura(datosReporteMascotaDTO)).thenThrow(new FechaInvalidaException());
    // When

    Usuario usuarioSimulado = new Usuario();
    usuarioSimulado.setEmail("brian@test.com");

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("usuario")).thenReturn(usuarioSimulado);





    ModelAndView mav = whenRealizarReporteMascota(datosReporteMascotaDTO,request);
    // Then
    thenReporteFalla(mav, "La fecha ingresada no puede ser futura al dia de hoy");
  }

  @Test
  public void siElUsuarioNoEstaLogueadoSeRedirigeAlLogin() {
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    HttpSession sessionMock = mock(HttpSession.class);
    when(requestMock.getSession()).thenReturn(sessionMock);
    when(sessionMock.getAttribute("usuario")).thenReturn(null);

    ModelAndView mav = controladorReporteMascota.mostrarFormularioReporteMascota(requestMock);

    assertThat(mav.getViewName(), equalTo("redirect:/login"));
  }


}
