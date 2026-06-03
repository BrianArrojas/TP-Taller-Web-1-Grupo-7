package com.tallerwebi.dominio.service;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;

public interface ServicioReporteMascota {

    public Boolean validarQueLaImagenCumplaConFormato(DatosReporteMascotaDTO datos);

    public Boolean validarQueFechaDeReporteNoSeaFutura(DatosReporteMascotaDTO datosReporteMascotaDTO);

    public Boolean guardarReporteMascota(DatosReporteMascotaDTO datosReporteMascotaDTO, String email) throws UsuarioExistente;
}
