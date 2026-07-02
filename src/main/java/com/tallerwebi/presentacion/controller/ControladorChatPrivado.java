package com.tallerwebi.presentacion.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.service.ServicioChatPrivado;
import com.tallerwebi.presentacion.dto.ChatDTO;

@Controller
public class ControladorChatPrivado {

    private final ServicioChatPrivado servicioChatPrivado;

    @Autowired
    public ControladorChatPrivado(ServicioChatPrivado servicioChatPrivado) {
        this.servicioChatPrivado = servicioChatPrivado;
    }

    @RequestMapping(path = "/iniciar-chat/{idReporte}", method = RequestMethod.GET)
    public ModelAndView iniciarChat(@PathVariable Long idReporte, HttpServletRequest request) {
        Usuario interesado = (Usuario) request.getSession().getAttribute("usuario");
        if (interesado == null) {
            return new ModelAndView("redirect:/login");
        }

        ChatDTO chat = crearChatDTO(request, idReporte);
        String codigoChat = servicioChatPrivado.iniciarChatPrivado(chat);
        return new ModelAndView("redirect:/chat-privado?codigoChat=" + codigoChat + "&idReporte=" + idReporte);
    }

    @RequestMapping(path = "/chat-privado", method = RequestMethod.GET)
    public ModelAndView mostrarChatPrivado(@RequestParam("codigoChat") String codigoChat,
                                        @RequestParam("idReporte") Long idReporte,
                                        HttpServletRequest request) {

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }

        ChatDTO chat = crearChatDTO(request, idReporte);
        chat.setCodigoChat(codigoChat);

        if (!servicioChatPrivado.puedeAccederAlChat(chat, usuario)) {
            return new ModelAndView("redirect:/home");
        }

        ModelMap modelo = new ModelMap();
        modelo.put("codigoChat", codigoChat);
    modelo.put("idReporte", idReporte);
    modelo.put("remitente", chat.getRemitente());
        modelo.put("chatDTO", chat);               

        return new ModelAndView("chat-privado", modelo);
    }

    private ChatDTO crearChatDTO(HttpServletRequest request, Long idReporte) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ChatDTO dto = new ChatDTO();
        dto.setIdReporte(idReporte);
        dto.setIdInteresado(usuario != null ? usuario.getId() : null);
        dto.setRemitente(usuario != null ? usuario.getNombre() : "Visitante");
        return dto;
    }
}