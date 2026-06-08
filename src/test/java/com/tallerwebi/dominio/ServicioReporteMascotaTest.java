package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.FechaInvalidaException;
import com.tallerwebi.dominio.excepcion.FormatoImagenInvalidaException;
import com.tallerwebi.dominio.model.ReporteMascota;

import com.tallerwebi.dominio.excepcion.ImagenExcedeTamanoException;
import com.tallerwebi.dominio.repository.RepositorioReporteMascota;
import com.tallerwebi.dominio.repository.RepositorioUsuario;
import com.tallerwebi.dominio.service.ServicioReporteMascota;
import com.tallerwebi.dominio.service.impl.ServicioReporteMascotaImpl;
import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ServicioReporteMascotaTest {

  RepositorioReporteMascota repositorioReporteMascota = mock(RepositorioReporteMascota.class);
  RepositorioUsuario repositorioUsuario = mock(RepositorioUsuario.class);

  ServicioReporteMascota servicioReporteMascota = new ServicioReporteMascotaImpl(repositorioReporteMascota,repositorioUsuario);

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

  @Test
  public void siLaImagenDelReporteSuperaPesoMaximoElMismoFalla(){

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
    MockMultipartFile fotoSimulada = new MockMultipartFile("foto", "perrito.png", "image/png", new byte[20 * 1024 * 1024]);
    datosReporteMascotaDTO.setImagen(fotoSimulada);
    assertThrows(ImagenExcedeTamanoException.class,()->servicioReporteMascota.validarQueLaImagenCumplaConFormato(datosReporteMascotaDTO));


  }

  @Test
  public void deberiaRetornarTodosLosReportesMapeadosADTOAlListarMascotas() {
    // Given
    List<ReporteMascota> reportesSimulados = new ArrayList<>();
    ReporteMascota r1 = new ReporteMascota();
    r1.setNombre("Milo");
    r1.setEspecie("Gato");
    r1.setTipoDeReporte("Perdido");
    reportesSimulados.add(r1);

    when(repositorioReporteMascota.buscarReportes(null)).thenReturn(reportesSimulados);

    // When
    List<DatosReporteMascotaDTO> result = servicioReporteMascota.listarMascotas(null);

    // Then
    assertThat(result, hasSize(1));
    assertThat(result.get(0).getNombre(), equalTo("Milo"));
    assertThat(result.get(0).getEspecie(), equalTo("Gato"));
    assertThat(result.get(0).getTipoDeReporte(), equalTo("Perdido"));
  }

  @Test
  public void alCancelarReporteDebeCambiarEstadoAFalse() {
    // given
    Long id = 1L;
    ReporteMascota reporte = new ReporteMascota();
    reporte.setRegistroActivo(true);

    when(repositorioReporteMascota.buscarPorId(id)).thenReturn(reporte);

    // when
    servicioReporteMascota.cancelarReporte(id);

    // then
    assertThat(reporte.getRegistroActivo(), is(false));
    verify(repositorioReporteMascota, times(1)).buscarPorId(id);
  }

}
