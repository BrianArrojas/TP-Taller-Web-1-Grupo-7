package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import com.tallerwebi.dominio.model.Comentario;
import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.repository.RepositorioComentario;
import com.tallerwebi.dominio.repository.RepositorioReporteMascota;
import com.tallerwebi.dominio.service.ServicioDetalleMascota;
import com.tallerwebi.dominio.service.impl.ServicioDetalleMascotaImpl;
import com.tallerwebi.presentacion.dto.DatosDetalleMascotaDTO;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

public class ServicioDetalleMascotaTest {

    RepositorioReporteMascota repositorioReporteMascotaMock = mock(RepositorioReporteMascota.class);
    RepositorioComentario repositorioComentarioMock = mock(RepositorioComentario.class);

    ServicioDetalleMascota servicio = new ServicioDetalleMascotaImpl(
            repositorioReporteMascotaMock,
            repositorioComentarioMock
    );

    @Test
    public void obtenerDetalleDebeRetornarDTO() {
        Long id = 1L;
        Usuario duenio = new Usuario();
        duenio.setNombre("Juan");
        duenio.setApellido("Pérez");
        duenio.setId(10L);
        ReporteMascota reporte = new ReporteMascota();
        reporte.setId(id);
        reporte.setNombre("Firulais");
        reporte.setUsuario(duenio);
        when(repositorioReporteMascotaMock.buscarPorId(id)).thenReturn(reporte);

        DatosDetalleMascotaDTO dto = servicio.obtenerDetalle(id);

        assertThat(dto.getNombre(), equalTo("Firulais"));
        assertThat(dto.getIdDuenio(), equalTo(10L));
    }

    @Test
    public void obtenerDetalleDebeRetornarDTOConMultiplesFotos() {
        Long id = 1L;
        ReporteMascota reporte = new ReporteMascota();
        reporte.setId(id);
        reporte.setNombre("Firulais");

        com.tallerwebi.dominio.model.Foto foto1 = new com.tallerwebi.dominio.model.Foto();
        foto1.setImg("img1.png");
        foto1.setReporteMascota(reporte);

        com.tallerwebi.dominio.model.Foto foto2 = new com.tallerwebi.dominio.model.Foto();
        foto2.setImg("img2.png");
        foto2.setReporteMascota(reporte);

        reporte.getFotos().add(foto1);
        reporte.getFotos().add(foto2);

        when(repositorioReporteMascotaMock.buscarPorId(id)).thenReturn(reporte);

        DatosDetalleMascotaDTO dto = servicio.obtenerDetalle(id);

        assertThat(dto.getNombre(), equalTo("Firulais"));
        assertThat(dto.getFotosUrls(), hasSize(2));
        assertThat(dto.getFotosUrls(), contains("/img/img1.png", "/img/img2.png"));
    }


    @Test
    public void publicarComentarioPublicoDebeGuardarComentario() {
        Long idReporte = 1L;
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan");
        ReporteMascota reporte = new ReporteMascota();
        when(repositorioReporteMascotaMock.buscarPorId(idReporte)).thenReturn(reporte);

        servicio.publicarComentarioPublico(idReporte, "Hola", usuario);

        ArgumentCaptor<Comentario> captor = ArgumentCaptor.forClass(Comentario.class);
        verify(repositorioComentarioMock).guardar(captor.capture());
        Comentario guardado = captor.getValue();
        assertThat(guardado.getNombreRemitente(), equalTo("Juan"));
        assertThat(guardado.getTexto(), equalTo("Hola"));
        assertThat(guardado.getCodigoChat(), nullValue());
    }

    @Test
    public void obtenerComentariosPublicosDebeDelegarEnRepositorio() {
        Long idReporte = 1L;
        List<Comentario> lista = new ArrayList<>();
        when(repositorioComentarioMock.obtenerTodosComentariosDelReporte(idReporte)).thenReturn(lista);

        List<Comentario> resultado = servicio.obtenerComentariosPublicos(idReporte);

        assertThat(resultado, equalTo(lista));
    }
}