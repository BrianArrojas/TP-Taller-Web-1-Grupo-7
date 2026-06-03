package com.tallerwebi.dominio.service.impl;

import com.tallerwebi.dominio.excepcion.FechaInvalidaException;
import com.tallerwebi.dominio.excepcion.FormatoImagenInvalidaException;
import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.repository.RepositorioReporteMascota;
import com.tallerwebi.dominio.service.ServicioReporteMascota;
import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

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

    @Override
    public List<ReporteMascota> obtenerTodosLosReportes() {
        return repositorioReporteMascota.obtenerTodosLosReportes();
    }

    @Override
    public List<DatosReporteMascotaDTO> listarMascotas(String busqueda) {
        List<ReporteMascota> reportes = repositorioReporteMascota.buscarReportes(busqueda);
        return reportes.stream().map(reporte -> {
            DatosReporteMascotaDTO dto = new DatosReporteMascotaDTO();
            dto.setNombre(reporte.getNombre());
            dto.setEspecie(reporte.getEspecie());
            dto.setFecha(reporte.getFecha());
            dto.setTipoDeReporte(reporte.getTipoDeReporte());
            dto.setRaza(reporte.getRaza());
            dto.setColor(reporte.getColor());
            dto.setTamano(reporte.getTamano());
            dto.setUbicacion(reporte.getUbicacion());
            dto.setDescripcion(reporte.getDescripcion());
            if (reporte.getFotos() != null && !reporte.getFotos().isEmpty()) {
                dto.setRutaImagen(reporte.getFotos().get(0).getImg());
            }
            return dto;
        }).collect(java.util.stream.Collectors.toList());
    }

}
