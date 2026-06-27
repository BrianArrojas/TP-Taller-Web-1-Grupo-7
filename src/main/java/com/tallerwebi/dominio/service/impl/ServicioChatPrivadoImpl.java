package com.tallerwebi.dominio.service.impl;

import com.tallerwebi.dominio.model.Comentario;
import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.repository.RepositorioComentario;
import com.tallerwebi.dominio.repository.RepositorioReporteMascota;
import com.tallerwebi.dominio.service.ServicioChatPrivado;
import com.tallerwebi.presentacion.dto.ChatDTO;
import com.tallerwebi.presentacion.dto.ConversacionDTO;
import com.tallerwebi.presentacion.dto.MensajeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("servicioChat")
@Transactional  // Permite guardar mensajes del chat privado en la base de datos
public class ServicioChatPrivadoImpl implements ServicioChatPrivado {

    private final SimpMessagingTemplate mensajero;
    private final RepositorioComentario repositorioComentario;
    private final RepositorioReporteMascota repositorioReporteMascota;

    @Autowired
    public ServicioChatPrivadoImpl(SimpMessagingTemplate mensajero,
                            RepositorioComentario repositorioComentario,
                            RepositorioReporteMascota repositorioReporteMascota) {
        this.mensajero = mensajero;
        this.repositorioComentario = repositorioComentario;
        this.repositorioReporteMascota = repositorioReporteMascota;
    }

    @Override
    public void enviarMensaje(ChatDTO mensaje) {
        String codigoChat = mensaje.getCodigoChat();
        Long idReporte = mensaje.getIdReporte();

        if (idReporte != null) {
            ReporteMascota reporte = repositorioReporteMascota.buscarPorId(idReporte);
            Comentario comentario = new Comentario();
            comentario.setReporteMascota(reporte);
            comentario.setNombreRemitente(mensaje.getRemitente());
            comentario.setTexto(mensaje.getContenido());
            comentario.setCodigoChat(codigoChat);
            repositorioComentario.guardar(comentario);
        }

        String destino = (codigoChat != null) ? "/chat/" + codigoChat : "/reporte/" + idReporte;
        mensajero.convertAndSend(destino, mensaje);
    }

    @Override
    public String iniciarChatPrivado(Long idReporte, Usuario interesado) {
        ReporteMascota reporte = repositorioReporteMascota.buscarPorId(idReporte);
        if (reporte == null) throw new RuntimeException("El reporte no existe");

        Comentario existente = repositorioComentario.buscarChatDelInteresado(idReporte, interesado.getId());
        if (existente != null) {
            return existente.getCodigoChat();
        }

        String codigoChat = UUID.randomUUID().toString();
        Comentario comentario = new Comentario();
        comentario.setReporteMascota(reporte);
        comentario.setNombreRemitente(interesado.getNombre());
        comentario.setTexto("Chat iniciado");
        comentario.setCodigoChat(codigoChat);
        comentario.setIdInteresado(interesado.getId());
        repositorioComentario.guardar(comentario);

        return codigoChat;
    }

    @Override
    public boolean puedeAccederAlChat(String codigoChat, Long idReporte, Usuario usuario) {
        if (usuario == null) return false;

        ReporteMascota reporte = repositorioReporteMascota.buscarPorId(idReporte);
        if (reporte != null && reporte.getUsuario().getId().equals(usuario.getId())) {
            return true;
        }

        List<Comentario> mensajes = repositorioComentario.obtenerMensajesPorCodigoChat(codigoChat);
        if (!mensajes.isEmpty()) {
            Comentario primerMensaje = mensajes.get(0);
            Long idInteresado = primerMensaje.getIdInteresado();
            if (idInteresado != null && idInteresado.equals(usuario.getId())) {
                return true;
            }
    }

    return false;
}

    @Override
    public List<MensajeDTO> obtenerHistorial(String codigoChat) {
        List<Comentario> comentarios = repositorioComentario.obtenerMensajesPorCodigoChat(codigoChat);
        List<MensajeDTO> resultado = new ArrayList<>();
        for (Comentario c : comentarios) {
            resultado.add(new MensajeDTO(c.getNombreRemitente(), c.getTexto()));
        }
        return resultado;
    }

    @Override
    public List<ConversacionDTO> obtenerConversacionesPorReporte(Long idReporte) {
        List<Comentario> mensajes = repositorioComentario.obtenerTodosMensajesDelReporte(idReporte);

        Map<String, List<Comentario>> porChat = new LinkedHashMap<>();
        for (Comentario c : mensajes) {
            if (!porChat.containsKey(c.getCodigoChat())) {
                porChat.put(c.getCodigoChat(), new ArrayList<>());
            }
            porChat.get(c.getCodigoChat()).add(c);
        }

        List<ConversacionDTO> resultado = new ArrayList<>();
        for (Map.Entry<String, List<Comentario>> entry : porChat.entrySet()) {
            List<Comentario> msgs = entry.getValue();
            Comentario primero = msgs.get(0);
            Comentario ultimo = msgs.get(msgs.size() - 1);

            ConversacionDTO dto = new ConversacionDTO();
            dto.setCodigoChat(entry.getKey());
            dto.setNombreInteresado(primero.getNombreRemitente());
            dto.setUltimoMensaje(ultimo.getTexto());
            dto.setFechaUltimoMensaje(ultimo.getFechaCreacion());
            resultado.add(dto);
        }
        return resultado;
    }
}