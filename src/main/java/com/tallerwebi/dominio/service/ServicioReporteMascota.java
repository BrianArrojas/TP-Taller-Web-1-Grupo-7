package com.tallerwebi.dominio.service;

import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;

public interface ServicioReporteMascota {

    public Boolean validarQueLaImagenCumplaConFormato(DatosReporteMascotaDTO datos);

    public Boolean validarQueFechaDeReporteNoSeaFutura(DatosReporteMascotaDTO datosReporteMascotaDTO);
}
