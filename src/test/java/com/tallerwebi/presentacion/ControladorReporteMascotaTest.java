package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.ServicioReporteMascota;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

public class ControladorReporteMascotaTest {

  ServicioReporteMascota servicioReporteMascota = mock(ServicioReporteMascota.class);
  ControladorReporteMascota controladorReporteMascota = new ControladorReporteMascota(servicioReporteMascota);

  @Test
  public void siGeneroUnReporteCorrectamenteSeMuestraLaVistaConLaInformacionDelMismo() {
    DatosReporteMascotaDTO datosReporteMascotaDTO = new DatosReporteMascotaDTO(
      "Brian",
      "Dogo",
      "Blanco",
      "Esta Lastimado",
      "San Justo"
    );
    datosReporteMascotaDTO.setTipoDeReporte("Perdido");
    datosReporteMascotaDTO.setTamano("Grande");
    datosReporteMascotaDTO.setEspecie("Perro");
    datosReporteMascotaDTO.setFecha(LocalDate.now().minusDays(1));
    MockMultipartFile fotoSimulada = new MockMultipartFile("foto", "perrito.png", "image/png", "bytes-de-imagen".getBytes());
    datosReporteMascotaDTO.setImagen(fotoSimulada);
    when(servicioReporteMascota.validarQueLaImagenCumplaConFormato(datosReporteMascotaDTO)).thenReturn(true);
    when(servicioReporteMascota.validarQueFechaDeReporteNoSeaFutura(any(DatosReporteMascotaDTO.class))).thenReturn(true);

    ModelAndView mav = whenRealizarReporteMascota(datosReporteMascotaDTO);

    thenReporteExitoso(mav, "El reporte se realizo correctamente");
  }

  private void thenReporteExitoso(ModelAndView mav, String mensaje) {
    assertThat(mav.getViewName(), equalToIgnoringCase("lista-de-reportes"));
    assertThat(mav.getModel().get("mensaje").toString(), equalToIgnoringCase(mensaje));
  }

  private ModelAndView whenRealizarReporteMascota(DatosReporteMascotaDTO datosReporteMascotaDTO) {
    ModelAndView mav = controladorReporteMascota.realizarReporte(datosReporteMascotaDTO);
    return mav;
  }

  private void givenReporteNoExiste() {}

  @Test
  public void alEnviarUnReporteConCamposVaciosElMismoFalla() {
    givenReporteNoExiste();
    DatosReporteMascotaDTO datosReporteMascotaDTO = new DatosReporteMascotaDTO(
            "", "", "", "", "");
    datosReporteMascotaDTO.setTipoDeReporte("Perdido");
    datosReporteMascotaDTO.setTamano("Grande");
    datosReporteMascotaDTO.setEspecie("Perro");
    datosReporteMascotaDTO.setFecha(LocalDate.of(2025, 4, 25));
    MockMultipartFile fotoSimulada = new MockMultipartFile("foto", "perrito.png", "image/png", "bytes-de-imagen".getBytes());
    datosReporteMascotaDTO.setImagen(fotoSimulada);
    ModelAndView mav = whenRealizarReporteMascota(datosReporteMascotaDTO);
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
    DatosReporteMascotaDTO datosReporteMascotaDTO = new DatosReporteMascotaDTO(
            "Brian",
            "Dogo",
            "Blanco",
            "Esta Lastimado",
            "San Justo"
    );
    ModelAndView mav = whenRealizarReporteMascota(datosReporteMascotaDTO);
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
    DatosReporteMascotaDTO datosReporteMascotaDTO = new DatosReporteMascotaDTO(
            "Brian",
            "Dogo",
            "Blanco",
            "Esta Lastimado",
            "San Justo"
    );
    datosReporteMascotaDTO.setTipoDeReporte("Perdido");
    datosReporteMascotaDTO.setTamano("Grande");
    datosReporteMascotaDTO.setEspecie("Perro");
    datosReporteMascotaDTO.setImagen(null);

    when(servicioReporteMascota.validarQueLaImagenCumplaConFormato(datosReporteMascotaDTO)).thenReturn(true);
    // When
    ModelAndView mav = whenRealizarReporteMascota(datosReporteMascotaDTO);
    // Then
    thenReporteFalla(mav,"Debe adjuntar una imagen");

    }

  private void thenReporteFalla(ModelAndView mav, String mensaje) {
    assertThat(mav.getViewName(), equalToIgnoringCase("realizar-reporte"));
    assertThat(mav.getModel().get("mensaje").toString(), equalToIgnoringCase(mensaje));
  }

}


