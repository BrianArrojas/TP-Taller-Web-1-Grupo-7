package com.tallerwebi.presentacion.controller;

import com.tallerwebi.dominio.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorPerfilUsuario {


    @RequestMapping(path = "/mi-perfil")
    public ModelAndView verPerfil(HttpServletRequest request) {
        ModelMap modelo = new ModelMap();

        Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuario");

        if (usuarioLogueado == null) {
            return new ModelAndView("redirect:/login");
        }

        modelo.put("usuario", usuarioLogueado);
        return new ModelAndView("perfil", modelo);
    }


    @RequestMapping(path = "/actualizar-perfil", method = RequestMethod.POST)
    public ModelAndView actualizarPerfil(@ModelAttribute("usuario") Usuario usuarioModificado, HttpServletRequest request) {

        request.getSession().setAttribute("usuario", usuarioModificado);

        return new ModelAndView("redirect:/mi-perfil");
    }
}
