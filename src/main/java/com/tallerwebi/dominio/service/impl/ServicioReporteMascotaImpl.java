package com.tallerwebi.dominio.service.impl;

import com.tallerwebi.dominio.excepcion.FechaInvalidaException;
import com.tallerwebi.dominio.excepcion.FormatoImagenInvalidaException;
import com.tallerwebi.dominio.repository.RepositorioReporteMascota;
import com.tallerwebi.dominio.service.ServicioReporteMascota;
import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@Transactional
public class ServicioReporteMascotaImpl implements ServicioReporteMascota {

    private  RepositorioReporteMascota repositorioReporteMascota;
    @Autowired
    public ServicioReporteMascotaImpl(RepositorioReporteMascota repositorioReporteMascota) {
        this.repositorioReporteMascota = repositorioReporteMascota;
    }

    public Boolean validarQueLaImagenCumplaConFormato(DatosReporteMascotaDTO datosReporteMascotaDTO) {
        String tipo = datosReporteMascotaDTO.getImagen().getContentType();

        if (!tipo.equals("image/png") && !tipo.equals("image/jpeg")) {
            throw new FormatoImagenInvalidaException();
        }

        return true;
    }

    public Boolean validarQueFechaDeReporteNoSeaFutura(DatosReporteMascotaDTO datosReporteMascotaDTO) {
        if (datosReporteMascotaDTO.getFecha() == null) {
            throw new FechaInvalidaException();
        }
        if (datosReporteMascotaDTO.getFecha().isAfter(LocalDate.now())) {
            throw new FechaInvalidaException();
        }

        return true;
    }

    public Boolean guardarReporteMascota(DatosReporteMascotaDTO datosReporteMascotaDTO) {

        if(datosReporteMascotaDTO.getFecha() != null) {
            repositorioReporteMascota.guardarReporte(datosReporteMascotaDTO);
            return true;
        }
        return false;
    }

}
