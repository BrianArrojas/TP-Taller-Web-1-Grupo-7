package com.tallerwebi.dominio.service;

import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;
import java.util.List;

public interface ServicioReporteMascota {

    public Boolean validarQueLaImagenCumplaConFormato(DatosReporteMascotaDTO datos);

    public Boolean validarQueFechaDeReporteNoSeaFutura(DatosReporteMascotaDTO datosReporteMascotaDTO);

    List<ReporteMascota> obtenerTodosLosReportes();

    List<ReporteMascota> buscarPorUsuario(Usuario usuario);

    List<DatosReporteMascotaDTO> listarMascotas(String busqueda);
    public Boolean guardarReporteMascota(DatosReporteMascotaDTO datosReporteMascotaDTO, String email) throws UsuarioExistente;

    void cancelarReporte(Long id);
    ReporteMascota buscarReporte(Long id);
    void actualizarReporte(DatosReporteMascotaDTO datosReporteMascotaDTO);
}
