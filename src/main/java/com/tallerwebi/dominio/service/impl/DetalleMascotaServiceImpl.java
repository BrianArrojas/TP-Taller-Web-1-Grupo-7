package com.tallerwebi.dominio.service.impl;

import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.repository.RepositorioReporteMascota;
import com.tallerwebi.dominio.service.DetalleMascotaService;
import com.tallerwebi.presentacion.dto.DatosDetalleMascotaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("detalleMascotaService")
@Transactional
public class DetalleMascotaServiceImpl implements DetalleMascotaService {

    private final RepositorioReporteMascota repositorioReporteMascota;

    @Autowired
    public DetalleMascotaServiceImpl(RepositorioReporteMascota repositorioReporteMascota) {
        this.repositorioReporteMascota = repositorioReporteMascota;
    }

    @Override
    public DatosDetalleMascotaDTO obtenerDetalle(Long id) {
        ReporteMascota reporte = repositorioReporteMascota.buscarPorId(id);
        if (reporte == null) {
            throw new RuntimeException("Reporte no encontrado");
        }
        return convertirADTO(reporte);
    }

    private DatosDetalleMascotaDTO convertirADTO(ReporteMascota reporte) {
        DatosDetalleMascotaDTO dto = new DatosDetalleMascotaDTO();
        dto.setId(reporte.getId());
        dto.setNombre(reporte.getNombre());
        dto.setEspecie(reporte.getEspecie());
        dto.setRaza(reporte.getRaza());
        dto.setColor(reporte.getColor());
        dto.setTamano(reporte.getTamano());
        dto.setFecha(reporte.getFecha());
        dto.setUbicacion(reporte.getUbicacion());
        dto.setDescripcion(reporte.getDescripcion());
        dto.setTipoDeReporte(reporte.getTipoDeReporte());

        if (reporte.getFotos() != null && !reporte.getFotos().isEmpty()) {
            dto.setFotoUrl("/img/" + reporte.getFotos().get(0).getImg());
        } else {
            dto.setFotoUrl("/img/default-pet.png");
        }

        if (reporte.getUsuario() != null) {
            dto.setNombreDuenio(reporte.getUsuario().getNombre() + " " + reporte.getUsuario().getApellido());
        } else {
            dto.setNombreDuenio("Desconocido");
        }
        return dto;
    }
}
