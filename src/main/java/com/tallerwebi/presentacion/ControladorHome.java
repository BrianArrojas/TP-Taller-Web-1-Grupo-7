package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioMascota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorHome {

  private final ServicioMascota servicioMascota;

  @Autowired
  public ControladorHome(ServicioMascota servicioMascota) {
    this.servicioMascota = servicioMascota;
  }

  @RequestMapping(path = "/home", method = RequestMethod.GET)
  public ModelAndView irAHome(@RequestParam(value = "busqueda", required = false) String busqueda) {
    ModelMap modelo = new ModelMap();
    modelo.put("mascotas", servicioMascota.listarMascotas(busqueda));
    return new ModelAndView("home", modelo);
  }
}
