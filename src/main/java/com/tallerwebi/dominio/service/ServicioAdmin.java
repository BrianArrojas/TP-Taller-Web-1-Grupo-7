package com.tallerwebi.dominio.service;

import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.model.Usuario;
import java.util.List;

public interface ServicioAdmin {

    List<Usuario> obtenerTodosLosUsuarios();

    List<Usuario> buscarUsuariosPorEmail(String email);

    void toggleUsuarioActivo(Long id);

    List<ReporteMascota> obtenerPublicacionesActivas();

    void eliminarPublicacion(Long id);
}
