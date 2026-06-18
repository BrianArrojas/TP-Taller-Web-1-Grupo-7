package com.tallerwebi.presentacion.controller;

import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.service.ServicioAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ControladorAdmin {

    private final ServicioAdmin servicioAdmin;

    @Autowired
    public ControladorAdmin(ServicioAdmin servicioAdmin) {
        this.servicioAdmin = servicioAdmin;
    }

    @RequestMapping(path = "/admin", method = RequestMethod.GET)
    public ModelAndView irAAdministracion(
            HttpServletRequest request,
            @RequestParam(value = "email", required = false) String email
    ) {
        ModelMap modelo = new ModelMap();
        String rol = (String) request.getSession().getAttribute("ROL");

        if (rol != null && rol.equalsIgnoreCase("ADMIN")) {
            List<Usuario> usuarios = servicioAdmin.buscarUsuariosPorEmail(email);
            List<ReporteMascota> publicaciones = servicioAdmin.obtenerPublicacionesActivas();

            modelo.put("usuarios", usuarios);
            modelo.put("publicaciones", publicaciones);
            modelo.put("emailBusqueda", email);

            return new ModelAndView("administracion", modelo);
        } else {
            modelo.put("error", "Acceso Denegado");
            return new ModelAndView("redirect:/home", modelo);
        }
    }

    @RequestMapping(path = "/admin/usuarios/toggle-activo", method = RequestMethod.POST)
    public ModelAndView toggleUsuarioActivo(
            HttpServletRequest request,
            @RequestParam("id") Long id
    ) {
        String rol = (String) request.getSession().getAttribute("ROL");

        if (rol != null && rol.equalsIgnoreCase("ADMIN")) {
            servicioAdmin.toggleUsuarioActivo(id);
            return new ModelAndView("redirect:/admin");
        } else {
            ModelMap modelo = new ModelMap();
            modelo.put("error", "Acceso Denegado");
            return new ModelAndView("redirect:/home", modelo);
        }
    }

    @RequestMapping(path = "/admin/publicaciones/eliminar", method = RequestMethod.POST)
    public ModelAndView eliminarPublicacion(
            HttpServletRequest request,
            @RequestParam("id") Long id
    ) {
        String rol = (String) request.getSession().getAttribute("ROL");

        if (rol != null && rol.equalsIgnoreCase("ADMIN")) {
            servicioAdmin.eliminarPublicacion(id);
            return new ModelAndView("redirect:/admin");
        } else {
            ModelMap modelo = new ModelMap();
            modelo.put("error", "Acceso Denegado");
            return new ModelAndView("redirect:/home", modelo);
        }
    }
}
