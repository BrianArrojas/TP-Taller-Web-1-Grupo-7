package com.tallerwebi.dominio.service;

import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;
import java.util.List;

public interface ServicioReporteMascota {

    public Boolean validarQueLaImagenCumplaConFormato(DatosReporteMascotaDTO datos);

    public Boolean validarQueFechaDeReporteNoSeaFutura(DatosReporteMascotaDTO datosReporteMascotaDTO);

    public Boolean guardarReporteMascota(DatosReporteMascotaDTO datosReporteMascotaDTO);

    List<ReporteMascota> obtenerTodosLosReportes();

    List<DatosReporteMascotaDTO> listarMascotas(String busqueda);
}
