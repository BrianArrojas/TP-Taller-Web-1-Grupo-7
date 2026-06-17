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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioReporteMascotaTest {

  RepositorioReporteMascota repositorioReporteMascota = mock(RepositorioReporteMascota.class);
  RepositorioUsuario repositorioUsuario = mock(RepositorioUsuario.class);

  ServicioReporteMascota servicioReporteMascota = new ServicioReporteMascotaImpl(repositorioReporteMascota, repositorioUsuario);

  @Test
  public void siNoSeRespetoFormatoDeImagenElReporteFalla() {
    // Given
    DatosReporteMascotaDTO datosReporteMascotaDTO = new DatosReporteMascotaDTO();
    datosReporteMascotaDTO.setNombre("Brian");
    datosReporteMascotaDTO.setFecha(LocalDate.now().minusDays(1));

    MultipartFile fotoSimuladaMock = mock(MultipartFile.class);

    when(fotoSimuladaMock.getOriginalFilename()).thenReturn("perrito.pdf");
    when(fotoSimuladaMock.getContentType()).thenReturn("application/pdf");

    datosReporteMascotaDTO.setImagen(fotoSimuladaMock);

    // When
    FormatoImagenInvalidaException excepcion = assertThrows(FormatoImagenInvalidaException.class, () -> {
      servicioReporteMascota.validarQueLaImagenCumplaConFormato(datosReporteMascotaDTO);
    });

    //Then
    assertThat(excepcion.getMessage(), equalTo("El formato de la imagen debe ser PNG o JPG."));
  }

  @Test
  public void siLaFechaDelReporteEsFuturaElMismoFalla() {
    // Given
    DatosReporteMascotaDTO datosReporteMascotaDTO = new DatosReporteMascotaDTO();
    datosReporteMascotaDTO.setNombre("Brian");
    datosReporteMascotaDTO.setFecha(LocalDate.now().plusDays(1)); // Fecha futura

    MultipartFile fotoSimuladaMock = mock(MultipartFile.class);
    when(fotoSimuladaMock.getOriginalFilename()).thenReturn("perrito.png");
    datosReporteMascotaDTO.setImagen(fotoSimuladaMock);

    // When
    FechaInvalidaException excepcion = assertThrows(FechaInvalidaException.class, () -> {
      servicioReporteMascota.validarQueFechaDeReporteNoSeaFutura(datosReporteMascotaDTO);
    });
    //Then
    assertThat(excepcion.getMessage(), equalTo("La fecha ingresada no puede ser futura al dia de hoy."));
  }

  @Test
  public void siLaImagenDelReporteSuperaPesoMaximoElMismoFalla() {
    // Given
    DatosReporteMascotaDTO datosReporteMascotaDTO = new DatosReporteMascotaDTO();
    datosReporteMascotaDTO.setNombre("Brian");
    datosReporteMascotaDTO.setFecha(LocalDate.now().minusDays(1));

    MultipartFile fotoSimuladaMock = mock(MultipartFile.class);
    when(fotoSimuladaMock.getOriginalFilename()).thenReturn("perrito.png");

    when(fotoSimuladaMock.getSize()).thenReturn(20L * 1024 * 1024);

    datosReporteMascotaDTO.setImagen(fotoSimuladaMock);

    // When
    ImagenExcedeTamanoException excepcion = assertThrows(ImagenExcedeTamanoException.class, () -> {
      servicioReporteMascota.validarQueLaImagenNoExcedaTamano(datosReporteMascotaDTO);
    });

    // Then
    assertThat(excepcion.getMessage(), equalTo("La foto es demasiado pesada. El tamaño máximo permitido es 20 MB"));
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
}

