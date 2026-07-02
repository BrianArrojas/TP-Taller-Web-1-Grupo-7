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
            Comentario comentario = crearComentario(reporte, mensaje);
            repositorioComentario.guardar(comentario);

            if (comentario.getFechaCreacion() != null) {
                mensaje.setFechaFormateada(comentario.getFechaCreacion().toString());
            }

            String nombreMascota = reporte.getNombre() != null ? reporte.getNombre() : "una mascota";
            String notificacion = String.format(
                "{\"action\":\"new_message\", \"codigoChat\":\"%s\", \"nombreMascota\":\"%s\", \"idReporte\":%d}",
                codigoChat, nombreMascota, idReporte
            );

            Usuario duenio = reporte.getUsuario();
            if (duenio != null) {
                mensajero.convertAndSend("/usuario/" + duenio.getId() + "/chats", notificacion);
            }

            Comentario primerMensaje = repositorioComentario.buscarPrimerMensajePorCodigoChat(codigoChat);
            if (primerMensaje != null && primerMensaje.getIdInteresado() != null) {
                mensajero.convertAndSend("/usuario/" + primerMensaje.getIdInteresado() + "/chats", notificacion);
            }
        }

        String destino = (codigoChat != null) ? "/chat/" + codigoChat : "/reporte/" + idReporte;
        mensajero.convertAndSend(destino, mensaje);
    }

    @Override
    public String iniciarChatPrivado(ChatDTO chat) {
        ReporteMascota reporte = repositorioReporteMascota.buscarPorId(chat.getIdReporte());
        if (reporte == null) throw new RuntimeException("El reporte no existe");

        String codigoExistente = repositorioComentario.obtenerCodigoChatExistente(
                chat.getIdReporte(), chat.getIdInteresado());
        if (codigoExistente != null) {
            return codigoExistente;
        }

        String codigoChat = UUID.randomUUID().toString();
        chat.setCodigoChat(codigoChat);
        chat.setContenido("Chat iniciado");

        Comentario comentario = crearComentario(reporte, chat);
        repositorioComentario.guardar(comentario);

        return codigoChat;
    }

    @Override
    public boolean puedeAccederAlChat(ChatDTO chat, Usuario usuario) {
        if (usuario == null) return false;

        ReporteMascota reporte = repositorioReporteMascota.buscarPorId(chat.getIdReporte());
        if (reporte != null && reporte.getUsuario().getId().equals(usuario.getId())) {
            return true;
        }

        if (chat.getIdInteresado() != null) {
            return repositorioComentario.obtenerCodigoChatExistente(
                    chat.getIdReporte(), chat.getIdInteresado()) != null;
        }

        return false;
    }

    @Override
    public List<MensajeDTO> obtenerHistorial(String codigoChat) {
        List<Comentario> comentarios = repositorioComentario.obtenerMensajesPorCodigoChat(codigoChat);
        List<MensajeDTO> resultado = new ArrayList<>();
        for (Comentario c : comentarios) {
        String fecha = c.getFechaCreacion() != null ? c.getFechaCreacion().toString() : "";
        resultado.add(new MensajeDTO(c.getNombreRemitente(), c.getTexto(), fecha));
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

    private Comentario crearComentario(ReporteMascota reporte, ChatDTO chat) {
        Comentario comentario = new Comentario();
        comentario.setReporteMascota(reporte);
        comentario.setNombreRemitente(chat.getRemitente());
        comentario.setTexto(chat.getContenido());
        comentario.setCodigoChat(chat.getCodigoChat());

        if (chat.getIdInteresado() != null) {
            comentario.setIdInteresado(chat.getIdInteresado());
        }

        return comentario;
    }
}