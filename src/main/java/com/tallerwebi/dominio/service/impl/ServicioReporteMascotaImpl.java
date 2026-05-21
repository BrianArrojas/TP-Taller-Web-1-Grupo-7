package com.tallerwebi.dominio.service.impl;

import com.tallerwebi.dominio.service.ServicioReporteMascota;
import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@Transactional
public class ServicioReporteMascotaImpl implements ServicioReporteMascota {

    public Boolean validarQueLaImagenCumplaConFormato(DatosReporteMascotaDTO datosReporteMascotaDTO) {
        MultipartFile archivo = datosReporteMascotaDTO.getImagen();
        String tipoArchivo = archivo.getContentType();
        if (archivo == null) {
            return false;
        }

        if (tipoArchivo != null && (tipoArchivo.equals("image/jpeg") || tipoArchivo.equals("image/png"))) {
            return true;
        }

        return false;
    }

    public Boolean validarQueFechaDeReporteNoSeaFutura(DatosReporteMascotaDTO datosReporteMascotaDTO) {
        if (datosReporteMascotaDTO.getFecha() == null) {
            return false;
        }
        if (datosReporteMascotaDTO.getFecha().compareTo(LocalDate.now()) > 0) {
            return false;
        }

        return true;
    }

}
