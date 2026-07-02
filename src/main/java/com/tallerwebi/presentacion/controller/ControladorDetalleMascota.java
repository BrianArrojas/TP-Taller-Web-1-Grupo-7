package com.tallerwebi.presentacion.controller;

import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.service.ServicioDetalleMascota;
import com.tallerwebi.presentacion.dto.ComentarioDTO;
import com.tallerwebi.presentacion.dto.DatosDetalleMascotaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorDetalleMascota {

    private final ServicioDetalleMascota servicioDetalleMascota;

    @Autowired
    public ControladorDetalleMascota(ServicioDetalleMascota servicioDetalleMascota) {
        this.servicioDetalleMascota = servicioDetalleMascota;
    }

    @RequestMapping(path = "/detalle/{id}", method = RequestMethod.GET)
    public ModelAndView verDetalle(@PathVariable Long id) {
        ModelMap model = new ModelMap();

        DatosDetalleMascotaDTO dto = servicioDetalleMascota.obtenerDetalle(id);
        model.put("mascota", dto);

        return new ModelAndView("detalle-mascota", model);
    }

    @RequestMapping(path = "/reporte/{idReporte}/comentario", method = RequestMethod.POST)
    public ModelAndView publicarComentario(@PathVariable Long idReporte,
                                           @RequestParam String texto,
                                           HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }

        ComentarioDTO dto = new ComentarioDTO();
        dto.setIdReporte(idReporte);
        dto.setNombreRemitente(usuario.getNombre());
        dto.setTexto(texto);

        servicioDetalleMascota.publicarComentario(dto);
        return new ModelAndView("redirect:/detalle/" + idReporte);
    }
}