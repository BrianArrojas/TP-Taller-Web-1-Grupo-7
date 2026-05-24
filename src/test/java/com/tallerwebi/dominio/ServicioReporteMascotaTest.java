package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.DatosReporteMascotaDTO;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertThrows;



public class ServicioReporteMascotaTest {

  ServicioReporteMascota servicioReporteMascota = new ServicioReporteMascotaImpl();

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
    datosReporteMascotaDTO.setFecha(LocalDate.now().minusDays(1));

    // Simulamos una foto real pasando: nombre del parámetro, nombre del archivo, tipo, y el contenido en bytes
    MockMultipartFile fotoSimulada = new MockMultipartFile("foto", "perrito.pdf", "image/pdf", "bytes-de-pdf".getBytes());
    datosReporteMascotaDTO.setImagen(fotoSimulada);
    assertThrows(FormatoImagenInvalidaException.class,()->servicioReporteMascota.validarQueLaImagenCumplaConFormato(datosReporteMascotaDTO));

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
    assertThrows(FechaInvalidaException.class,()->servicioReporteMascota.validarQueFechaDeReporteNoSeaFutura(datosReporteMascotaDTO));

  }

}
