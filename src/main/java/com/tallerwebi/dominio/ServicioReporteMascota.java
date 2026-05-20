package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.DatosReporteMascotaDTO;

public interface ServicioReporteMascota {

    public Boolean validarQueLaImagenCumplaConFormato(DatosReporteMascotaDTO datos);

    public Boolean validarQueFechaDeReporteNoSeaFutura(DatosReporteMascotaDTO datosReporteMascotaDTO);
}
