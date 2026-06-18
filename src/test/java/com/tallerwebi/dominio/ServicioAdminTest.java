package com.tallerwebi.dominio;

import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.repository.RepositorioReporteMascota;
import com.tallerwebi.dominio.repository.RepositorioUsuario;
import com.tallerwebi.dominio.service.ServicioAdmin;
import com.tallerwebi.dominio.service.impl.ServicioAdminImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class ServicioAdminTest {

    private RepositorioUsuario repositorioUsuarioMock;
    private RepositorioReporteMascota repositorioReporteMascotaMock;
    private ServicioAdmin servicioAdmin;

    @BeforeEach
    public void setUp() {
        repositorioUsuarioMock = mock(RepositorioUsuario.class);
        repositorioReporteMascotaMock = mock(RepositorioReporteMascota.class);
        servicioAdmin = new ServicioAdminImpl(repositorioUsuarioMock, repositorioReporteMascotaMock);
    }

    @Test
    public void alObtenerTodosLosUsuariosDebeDelegarAlRepositorio() {
        // Given
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario());
        when(repositorioUsuarioMock.obtenerTodos()).thenReturn(usuarios);

        // When
        List<Usuario> resultado = servicioAdmin.obtenerTodosLosUsuarios();

        // Then
        assertThat(resultado, hasSize(1));
        verify(repositorioUsuarioMock, times(1)).obtenerTodos();
    }

    @Test
    public void alBuscarUsuariosPorEmailDebeFiltrarPorEmailSiNoEstaVacio() {
        // Given
        String email = "test@unlam.edu.ar";
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario());
        when(repositorioUsuarioMock.buscarPorEmail(email)).thenReturn(usuarios);

        // When
        List<Usuario> resultado = servicioAdmin.buscarUsuariosPorEmail(email);

        // Then
        assertThat(resultado, hasSize(1));
        verify(repositorioUsuarioMock, times(1)).buscarPorEmail(email);
    }

    @Test
    public void alBuscarUsuariosPorEmailConEmailNuloOVacioDebeObtenerTodos() {
        // Given
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario());
        when(repositorioUsuarioMock.obtenerTodos()).thenReturn(usuarios);

        // When
        List<Usuario> resultadoNull = servicioAdmin.buscarUsuariosPorEmail(null);
        List<Usuario> resultadoVacio = servicioAdmin.buscarUsuariosPorEmail("   ");

        // Then
        assertThat(resultadoNull, hasSize(1));
        assertThat(resultadoVacio, hasSize(1));
        verify(repositorioUsuarioMock, times(2)).obtenerTodos();
        verify(repositorioUsuarioMock, times(0)).buscarPorEmail(anyString());
    }

    @Test
    public void alToggleUsuarioActivoDebeCambiarEstadoYModificar() {
        // Given
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setActivo(true);
        when(repositorioUsuarioMock.buscarPorId(id)).thenReturn(usuario);

        // When
        servicioAdmin.toggleUsuarioActivo(id);

        // Then
        assertThat(usuario.getActivo(), is(false));
        verify(repositorioUsuarioMock, times(1)).modificar(usuario);

        // When toggle again
        servicioAdmin.toggleUsuarioActivo(id);

        // Then
        assertThat(usuario.getActivo(), is(true));
    }

    @Test
    public void alObtenerPublicacionesActivasDebeFiltrarLasQueTienenRegistroActivoEnTrue() {
        // Given
        List<ReporteMascota> reportes = new ArrayList<>();
        
        ReporteMascota r1 = new ReporteMascota();
        r1.setRegistroActivo(true);
        
        ReporteMascota r2 = new ReporteMascota();
        r2.setRegistroActivo(false);
        
        ReporteMascota r3 = new ReporteMascota();
        r3.setRegistroActivo(null);

        reportes.add(r1);
        reportes.add(r2);
        reportes.add(r3);

        when(repositorioReporteMascotaMock.obtenerTodosLosReportes()).thenReturn(reportes);

        // When
        List<ReporteMascota> activas = servicioAdmin.obtenerPublicacionesActivas();

        // Then
        assertThat(activas, hasSize(1));
        assertThat(activas.get(0), is(r1));
    }

    @Test
    public void alEliminarPublicacionDebeEliminarDelUsuarioYDelRepositorio() {
        // Given
        Long id = 10L;
        Usuario owner = new Usuario();
        ReporteMascota reporte = new ReporteMascota();
        reporte.setId(id);
        reporte.setUsuario(owner);
        owner.getReportesMascotas().add(reporte);

        when(repositorioReporteMascotaMock.buscarPorId(id)).thenReturn(reporte);

        // When
        servicioAdmin.eliminarPublicacion(id);

        // Then
        assertThat(owner.getReportesMascotas(), not(contains(reporte)));
        verify(repositorioUsuarioMock, times(1)).modificar(owner);
        verify(repositorioReporteMascotaMock, times(1)).eliminarReporte(reporte);
    }
}
