package com.tallerwebi.presentacion.controller;

import java.util.List;

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
import com.tallerwebi.presentacion.dto.MensajeDTO;

@Controller
public class ControladorChatPrivado {

    private final ServicioChatPrivado servicioChat;

    @Autowired
    public ControladorChatPrivado(ServicioChatPrivado servicioChat) {
        this.servicioChat = servicioChat;
    }

    @RequestMapping(path = "/iniciar-chat/{idReporte}", method = RequestMethod.GET)
    public ModelAndView iniciarChat(@PathVariable Long idReporte, HttpServletRequest request) {
        Usuario interesado = (Usuario) request.getSession().getAttribute("usuario");
        if (interesado == null) {
            return new ModelAndView("redirect:/login");
        }

        String codigoChat = servicioChat.iniciarChatPrivado(idReporte, interesado);
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
        if (!servicioChat.puedeAccederAlChat(codigoChat, idReporte, usuario)) {
            return new ModelAndView("redirect:/home");
        }

        ModelMap modelo = new ModelMap();
        modelo.put("codigoChat", codigoChat);
        modelo.put("idReporte", idReporte);

        String remitente = (usuario.getNombre() != null) ? usuario.getNombre() : "Visitante";
        modelo.put("remitente", remitente);

        List<MensajeDTO> historial = servicioChat.obtenerHistorial(codigoChat);
        modelo.put("historial", historial);

        return new ModelAndView("chat-privado", modelo);
    }
}