package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.FechaInvalidaException;
import com.tallerwebi.dominio.excepcion.FormatoImagenInvalidaException;

import com.tallerwebi.dominio.repository.RepositorioReporteMascota;
import com.tallerwebi.dominio.service.ServicioReporteMascota;
import com.tallerwebi.dominio.service.impl.ServicioReporteMascotaImpl;
import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;


public class ServicioReporteMascotaTest {

  RepositorioReporteMascota repositorioReporteMascota = mock(RepositorioReporteMascota.class);
  ServicioReporteMascota servicioReporteMascota = new ServicioReporteMascotaImpl(repositorioReporteMascota);

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
    assertThrows(FormatoImagenInvalidaException.class,()->servicioReporteMascota.validarQueLaImagenCumplaConFormato(datosReporteMascotaDTO));

  }

  @Test
  public void siLaFechaDelReporteEsFuturaElMismoFalla(){

    DatosReporteMascotaDTO datosReporteMascotaDTO = new DatosReporteMascotaDTO();
    datosReporteMascotaDTO.setNombre("Brian");
    datosReporteMascotaDTO.setRaza("Dogo");
    datosReporteMascotaDTO.setColor("Blanco");
    datosReporteMascotaDTO.setDescripcion("Esta Lastimado");
    datosReporteMascotaDTO.setUbicacion("San Justo");
    datosReporteMascotaDTO.setTipoDeReporte("Perdido");
    datosReporteMascotaDTO.setTamano("Grande");
    datosReporteMascotaDTO.setEspecie("Perro");
    datosReporteMascotaDTO.setFecha(LocalDate.now().plusDays(1));
    MockMultipartFile fotoSimulada = new MockMultipartFile("foto", "perrito.png", "image/png", "bytes-de-imagen".getBytes());
    datosReporteMascotaDTO.setImagen(fotoSimulada);
    assertThrows(FechaInvalidaException.class,()->servicioReporteMascota.validarQueFechaDeReporteNoSeaFutura(datosReporteMascotaDTO));

  }

}
