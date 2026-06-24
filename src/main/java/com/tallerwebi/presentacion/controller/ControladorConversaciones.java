package com.tallerwebi.presentacion.controller;

import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.service.ServicioChatPrivado;
import com.tallerwebi.presentacion.dto.ConversacionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ControladorConversaciones {

    private final ServicioChatPrivado servicioChat;

    @Autowired
    public ControladorConversaciones(ServicioChatPrivado servicioChat) {
        this.servicioChat = servicioChat;
    }

    @RequestMapping(path = "/reporte/{idReporte}/conversaciones", method = RequestMethod.GET)
    public ModelAndView verConversaciones(@PathVariable Long idReporte, HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }

        List<ConversacionDTO> conversaciones = servicioChat.obtenerConversacionesPorReporte(idReporte);

        ModelMap model = new ModelMap();
        model.put("conversaciones", conversaciones);
        model.put("idReporte", idReporte);

        return new ModelAndView("conversaciones", model);
    }
}