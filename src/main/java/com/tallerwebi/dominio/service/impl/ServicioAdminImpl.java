package com.tallerwebi.dominio.service.impl;

import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.repository.RepositorioReporteMascota;
import com.tallerwebi.dominio.repository.RepositorioUsuario;
import com.tallerwebi.dominio.service.ServicioAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service("servicioAdmin")
@Transactional
public class ServicioAdminImpl implements ServicioAdmin {

    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioReporteMascota repositorioReporteMascota;

    @Autowired
    public ServicioAdminImpl(RepositorioUsuario repositorioUsuario, RepositorioReporteMascota repositorioReporteMascota) {
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioReporteMascota = repositorioReporteMascota;
    }

    @Override
    public List<Usuario> obtenerTodosLosUsuarios() {
        return repositorioUsuario.obtenerTodos();
    }

    @Override
    public List<Usuario> buscarUsuariosPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return repositorioUsuario.obtenerTodos();
        }
        return repositorioUsuario.buscarPorEmail(email.trim());
    }

    @Override
    public void toggleUsuarioActivo(Long id) {
        Usuario usuario = repositorioUsuario.buscarPorId(id);
        if (usuario != null) {
            Boolean estadoActual = usuario.getActivo();
            usuario.setActivo(estadoActual == null ? true : !estadoActual);
            repositorioUsuario.modificar(usuario);
        }
    }

    @Override
    public List<ReporteMascota> obtenerPublicacionesActivas() {
        List<ReporteMascota> todos = repositorioReporteMascota.obtenerTodosLosReportes();
        return todos.stream()
                .filter(reporte -> reporte.getRegistroActivo() != null && reporte.getRegistroActivo())
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarPublicacion(Long id) {
        ReporteMascota reporte = repositorioReporteMascota.buscarPorId(id);
        if (reporte != null) {
            // Remove report from user's list if present to avoid cascade/merge issues
            Usuario owner = reporte.getUsuario();
            if (owner != null) {
                owner.getReportesMascotas().remove(reporte);
                repositorioUsuario.modificar(owner);
            }
            repositorioReporteMascota.eliminarReporte(reporte);
        }
    }
}
