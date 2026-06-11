package com.tallerwebi.presentacion.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.model.Usuario;

@Controller
public class ControladorChat {

    @RequestMapping(path = "/chat/{id}", method = RequestMethod.GET)
    public ModelAndView mostrarChat(@PathVariable Long id, HttpServletRequest request) {
        ModelMap modelo = new ModelMap();
        modelo.put("idReporte", id);

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        String remitente = (usuario != null) ? usuario.getNombre() : "Visitante";
        modelo.put("remitente", remitente);

        return new ModelAndView("chat", modelo);
    }
}