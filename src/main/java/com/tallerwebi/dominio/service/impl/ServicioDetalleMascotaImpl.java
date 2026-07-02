package com.tallerwebi.dominio.service.impl;

import com.tallerwebi.dominio.model.Comentario;
import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.repository.RepositorioComentario;
import com.tallerwebi.dominio.repository.RepositorioReporteMascota;
import com.tallerwebi.dominio.service.ServicioDetalleMascota;
import com.tallerwebi.presentacion.dto.ComentarioDTO;
import com.tallerwebi.presentacion.dto.DatosDetalleMascotaDTO;
import com.tallerwebi.presentacion.dto.MensajeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("detalleMascotaService")
@Transactional
public class ServicioDetalleMascotaImpl implements ServicioDetalleMascota {

    private final RepositorioReporteMascota repositorioReporteMascota;
    private final RepositorioComentario repositorioComentario;
    private final SimpMessagingTemplate mensajero;

    @Autowired
    public ServicioDetalleMascotaImpl(RepositorioReporteMascota repositorioReporteMascota,
                                      RepositorioComentario repositorioComentario,
                                      SimpMessagingTemplate mensajero) {
        this.repositorioReporteMascota = repositorioReporteMascota;
        this.repositorioComentario = repositorioComentario;
        this.mensajero = mensajero;
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
    public void publicarComentario(ComentarioDTO dto) {
        ReporteMascota reporte = repositorioReporteMascota.buscarPorId(dto.getIdReporte());
        if (reporte == null) {
            throw new RuntimeException("El reporte no existe");
        }

        Comentario comentario = new Comentario();
        comentario.setReporteMascota(reporte);
        comentario.setNombreRemitente(dto.getNombreRemitente());
        comentario.setTexto(dto.getTexto());
        repositorioComentario.guardar(comentario);

        MensajeDTO mensaje = new MensajeDTO(
                comentario.getNombreRemitente(),
                comentario.getTexto(),
                comentario.getFechaCreacion() != null ? comentario.getFechaCreacion().toString() : ""
        );
        mensajero.convertAndSend("/reporte/" + dto.getIdReporte() + "/foro", mensaje);
    }

    @Override
    public List<MensajeDTO> obtenerComentariosPublicos(Long idReporte) {
        List<Comentario> comentarios = repositorioComentario.obtenerTodosComentariosDelReporte(idReporte);
        List<MensajeDTO> resultado = new ArrayList<>();
        for (Comentario c : comentarios) {
            String fecha = c.getFechaCreacion() != null ? c.getFechaCreacion().toString() : "";
            resultado.add(new MensajeDTO(c.getNombreRemitente(), c.getTexto(), fecha));
        }
        return resultado;
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

        List<String> urls = new ArrayList<>();
        if (reporte.getFotos() != null && !reporte.getFotos().isEmpty()) {
            String imgPath = reporte.getFotos().get(0).getImg();
            if (imgPath.startsWith("img/")) {
                dto.setFotoUrl("/" + imgPath);
            } else if (imgPath.startsWith("/img/")) {
                dto.setFotoUrl(imgPath);
            } else {
                dto.setFotoUrl("/img/" + imgPath);
            }

            for (com.tallerwebi.dominio.model.Foto foto : reporte.getFotos()) {
                String fPath = foto.getImg();
                if (fPath.startsWith("img/")) {
                    urls.add("/" + fPath);
                } else if (fPath.startsWith("/img/")) {
                    urls.add(fPath);
                } else {
                    urls.add("/img/" + fPath);
                }
            }
        } else {
            dto.setFotoUrl("/img/default-pet.png");
            urls.add("/img/default-pet.png");
        }
        dto.setFotosUrls(urls);

        if (reporte.getUsuario() != null) {
            dto.setNombreDuenio(reporte.getUsuario().getNombre() + " " + reporte.getUsuario().getApellido());
            dto.setIdDuenio(reporte.getUsuario().getId());
        } else {
            dto.setNombreDuenio("Desconocido");
        }
        return dto;
    }
}