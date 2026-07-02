package com.tallerwebi.dominio.repository;

import com.tallerwebi.dominio.model.Comentario;
import java.util.List;

public interface RepositorioComentario {

    void guardar(Comentario comentario);
    String obtenerCodigoChatExistente(Long reporteId, Long interesadoId);
    Comentario buscarPrimerMensajePorCodigoChat(String codigoChat);

    List<Comentario> obtenerMensajesPorCodigoChat(String codigoChat);
    List<Comentario> obtenerTodosComentariosDelReporte(Long reporteId);
    List<Comentario> obtenerTodosMensajesDelReporte(Long reporteId);

}