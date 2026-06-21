package com.tallerwebi.presentacion.controller;

import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.service.ServicioReporteMascota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ControladorHome {

  private final ServicioReporteMascota servicioReporteMascota;

  @Autowired
  public ControladorHome(ServicioReporteMascota servicioReporteMascota) {
    this.servicioReporteMascota = servicioReporteMascota;
  }

  @RequestMapping(path = "/home", method = RequestMethod.GET)
  public ModelAndView irAHome(@RequestParam(value = "busqueda", required = false) String busqueda) {
    ModelMap modelo = new ModelMap();
    modelo.put("mascotas", servicioReporteMascota.listarReportes(busqueda));
    return new ModelAndView("home", modelo);
  }
}
