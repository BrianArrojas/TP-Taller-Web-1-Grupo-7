package com.tallerwebi.dominio.repository;

import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;
import java.time.LocalDate;
import java.util.List;

public interface RepositorioReporteMascota {

    List<ReporteMascota> obtenerTodosLosReportes();

    List<ReporteMascota> buscarReportes(String busqueda);
    public void guardarReporte(DatosReporteMascotaDTO datosReporteMascota, Usuario usuario);

    List<ReporteMascota> buscarPorUsuario(Usuario usuario);

    ReporteMascota buscarPorId(Long id);

    void actualizarReporte(ReporteMascota reporteExistente);
    void eliminarReporte(ReporteMascota reporte);
    List<ReporteMascota> obtenerTodosLosReportesActivos();

    List<ReporteMascota> buscarReportesFiltradosYPaginados(
        String busqueda, String tipoDeReporte, String especie,
        LocalDate fechaDesde, LocalDate fechaHasta, int page, int pageSize);

    int contarReportesFiltrados(
        String busqueda, String tipoDeReporte, String especie,
        LocalDate fechaDesde, LocalDate fechaHasta);
}