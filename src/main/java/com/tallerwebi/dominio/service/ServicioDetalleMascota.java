package com.tallerwebi.dominio.service;

import com.tallerwebi.dominio.model.Comentario;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.presentacion.dto.DatosDetalleMascotaDTO;

import java.util.List;

public interface ServicioDetalleMascota {

    DatosDetalleMascotaDTO obtenerDetalle(Long id);

    void publicarComentarioPublico(Long idReporte, String texto, Usuario usuario);

    List<Comentario> obtenerComentariosPublicos(Long idReporte);
}