package com.tallerwebi.presentacion.controller;

import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.service.ServicioReporteMascota;
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
public class ControladorMisReportes {

    private ServicioReporteMascota servicioReporteMascota;

    @Autowired
    public ControladorMisReportes(ServicioReporteMascota servicioReporteMascota) {
        this.servicioReporteMascota = servicioReporteMascota;
    }

    @RequestMapping(path = "/mis-reportes")
    public ModelAndView mostrarMisReportes (HttpServletRequest request){
        ModelMap modelo = new ModelMap();

        Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuario");

        if (usuarioLogueado != null) {
            List<ReporteMascota> misReportes = servicioReporteMascota.buscarPorUsuario(usuarioLogueado);
            modelo.put("reportes", misReportes);
            return new ModelAndView("mis-reportes", modelo);
        } else {
            return new ModelAndView("redirect:/login");
        }
    }

    @RequestMapping(path = "/cancelar-reporte", method = RequestMethod.POST)
    public ModelAndView cancelarReporte(@RequestParam("id") Long id) {
        servicioReporteMascota.cancelarReporte(id);
        return new ModelAndView("redirect:/mis-reportes");
    }
}
