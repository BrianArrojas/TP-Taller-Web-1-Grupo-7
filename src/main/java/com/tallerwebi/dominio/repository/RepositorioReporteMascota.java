package com.tallerwebi.dominio.repository;

import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;

import java.util.List;

public interface RepositorioReporteMascota {

    public void guardarReporte(DatosReporteMascotaDTO datosReporteMascota, Usuario usuario);

    List<ReporteMascota> buscarPorUsuario(Usuario usuario);

    ReporteMascota buscarPorId(Long id);
}