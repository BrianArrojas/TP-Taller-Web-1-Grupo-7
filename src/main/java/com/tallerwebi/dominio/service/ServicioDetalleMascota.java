package com.tallerwebi.dominio.service;

import com.tallerwebi.presentacion.dto.ComentarioDTO;
import com.tallerwebi.presentacion.dto.DatosDetalleMascotaDTO;
import com.tallerwebi.presentacion.dto.MensajeDTO;

import java.util.List;

public interface ServicioDetalleMascota {
    DatosDetalleMascotaDTO obtenerDetalle(Long id);
    void publicarComentario(ComentarioDTO dto);
    List<MensajeDTO> obtenerComentariosPublicos(Long idReporte);
}