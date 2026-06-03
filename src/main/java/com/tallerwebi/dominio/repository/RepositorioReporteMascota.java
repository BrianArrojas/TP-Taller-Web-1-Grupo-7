package com.tallerwebi.dominio.repository;

import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;
import java.util.List;

public interface RepositorioReporteMascota {

    public void guardarReporte(DatosReporteMascotaDTO datosReporteMascota);

    List<ReporteMascota> obtenerTodosLosReportes();

    List<ReporteMascota> buscarReportes(String busqueda);
}
