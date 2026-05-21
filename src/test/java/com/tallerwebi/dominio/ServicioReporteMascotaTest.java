package com.tallerwebi.dominio;

import com.tallerwebi.dominio.service.ServicioReporteMascota;
import com.tallerwebi.presentacion.controller.ControladorReporteMascota;
import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ServicioReporteMascotaTest {

  ServicioReporteMascota servicioReporteMascota = mock(ServicioReporteMascota.class);
  ControladorReporteMascota controladorReporteMascota = new ControladorReporteMascota(servicioReporteMascota);;

  @Test
  public void siNoSeRespetoFormatoDeImagenElReporteFalla() {
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


    // Simulamos una foto real pasando: nombre del parámetro, nombre del archivo, tipo, y el contenido en bytes
    MockMultipartFile fotoSimulada = new MockMultipartFile("foto", "perrito.pdf", "documento.pdf", "bytes-de-pdf".getBytes());
    datosReporteMascotaDTO.setImagen(fotoSimulada);


    // When
    ModelAndView mav = whenRealizarReporteMascota(datosReporteMascotaDTO);

    // Then
    thenReporteFalla(mav,"El formato de la foto debe ser JPG o PNG");
  }

  private ModelAndView whenRealizarReporteMascota(DatosReporteMascotaDTO datosReporteMascotaDTO) {
    ModelAndView mav = controladorReporteMascota.realizarReporte(datosReporteMascotaDTO);
    return mav;
  }

  private void thenReporteFalla(ModelAndView mav, String mensaje) {
    assertThat(mav.getViewName(), equalToIgnoringCase("realizar-reporte"));
    assertThat(mav.getModel().get("mensaje").toString(), equalToIgnoringCase(mensaje));
  }


  @Test
  public void siLaFechaDelReporteEsFuturaElMismoFalla(){

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
    datosReporteMascotaDTO.setFecha(LocalDate.now().plusDays(1));
    MockMultipartFile fotoSimulada = new MockMultipartFile("foto", "perrito.png", "image/png", "bytes-de-imagen".getBytes());
    datosReporteMascotaDTO.setImagen(fotoSimulada);
    when(servicioReporteMascota.validarQueLaImagenCumplaConFormato(datosReporteMascotaDTO)).thenReturn(true);
    when(servicioReporteMascota.validarQueFechaDeReporteNoSeaFutura(any(DatosReporteMascotaDTO.class))).thenReturn(false);

    ModelAndView mav = whenRealizarReporteMascota(datosReporteMascotaDTO);

    thenReporteFalla(mav,"La fecha ingresada no puede ser futura al dia de hoy");
  }

}
