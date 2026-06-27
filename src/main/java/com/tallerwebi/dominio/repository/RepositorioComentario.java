package com.tallerwebi.dominio.repository;

import com.tallerwebi.dominio.model.Comentario;
import java.util.List;

public interface RepositorioComentario {

    void guardar(Comentario comentario);

    Comentario buscarChatDelInteresado(Long reporteId, Long interesadoId);

    List<Comentario> obtenerTodosComentariosDelReporte(Long reporteId);

    List<Comentario> obtenerTodosMensajesDelReporte(Long reporteId);

    List<Comentario> obtenerMensajesPorCodigoChat(String codigoChat);

}