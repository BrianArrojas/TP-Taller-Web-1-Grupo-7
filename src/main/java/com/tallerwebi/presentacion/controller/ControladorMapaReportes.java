package com.tallerwebi.presentacion.controller;

import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.service.ServicioReporteMascota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ControladorMapaReportes {

    private ServicioReporteMascota servicioReporteMascota;

    @Autowired
    public ControladorMapaReportes(ServicioReporteMascota servicioReporteMascota) {
        this.servicioReporteMascota = servicioReporteMascota;
    }

    @RequestMapping(path = "/mapa-reportes", method = RequestMethod.GET)
    public ModelAndView verMapa() {
        ModelAndView mav = new ModelAndView("mapa-reportes");
        List<ReporteMascota> reportes = servicioReporteMascota.obtenerTodosLosReportesActivos();
        mav.addObject("reportes", reportes);
        return mav;
    }

    @RequestMapping(path = "/api/reportes", method = RequestMethod.GET)
    @ResponseBody
    public List<ReporteMascota> obtenerReportesJson() {
        return servicioReporteMascota.obtenerTodosLosReportesActivos();
    }
}
