package com.tallerwebi.dominio.service.impl;

import com.tallerwebi.dominio.model.Comentario;
import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.repository.RepositorioComentario;
import com.tallerwebi.dominio.repository.RepositorioReporteMascota;
import com.tallerwebi.dominio.service.ServicioDetalleMascota;
import com.tallerwebi.presentacion.dto.DatosDetalleMascotaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.transaction.Transactional;

@Service("detalleMascotaService")
@Transactional  // Permite consultar la base de datos
public class ServicioDetalleMascotaImpl implements ServicioDetalleMascota {

    private final RepositorioReporteMascota repositorioReporteMascota;
    private final RepositorioComentario repositorioComentario;

    @Autowired
    public ServicioDetalleMascotaImpl(RepositorioReporteMascota repositorioReporteMascota,
                                      RepositorioComentario repositorioComentario) {
        this.repositorioReporteMascota = repositorioReporteMascota;
        this.repositorioComentario = repositorioComentario;
    }

    @Override
    public DatosDetalleMascotaDTO obtenerDetalle(Long id) {
        ReporteMascota reporte = repositorioReporteMascota.buscarPorId(id);
        if (reporte == null) {
            throw new RuntimeException("Reporte no encontrado");
        }
        return convertirADTO(reporte);
    }

    @Override
    public void publicarComentarioPublico(Long idReporte, String texto, Usuario usuario) {
        ReporteMascota reporte = repositorioReporteMascota.buscarPorId(idReporte);
        if (reporte == null) {
            throw new RuntimeException("El reporte no existe");
        }
        Comentario comentario = new Comentario();
        comentario.setReporteMascota(reporte);
        comentario.setNombreRemitente(usuario.getNombre());
        comentario.setTexto(texto);
        repositorioComentario.guardar(comentario);
    }

    @Override
    public List<Comentario> obtenerComentariosPublicos(Long idReporte) {
        return repositorioComentario.obtenerTodosComentariosDelReporte(idReporte);
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
            String imgPath = reporte.getFotos().get(0).getImg();
            if (imgPath.startsWith("img/")) {
                dto.setFotoUrl("/" + imgPath);
            } else if (imgPath.startsWith("/img/")) {
                dto.setFotoUrl(imgPath);
            } else {
                dto.setFotoUrl("/img/" + imgPath);
            }
        } else {
            dto.setFotoUrl("/img/default-pet.png");
        }

        if (reporte.getUsuario() != null) {
            dto.setNombreDuenio(reporte.getUsuario().getNombre() + " " + reporte.getUsuario().getApellido());
            dto.setIdDuenio(reporte.getUsuario().getId());
        } else {
            dto.setNombreDuenio("Desconocido");
        }
        return dto;
    }
}
