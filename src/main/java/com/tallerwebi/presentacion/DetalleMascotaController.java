package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.DetalleMascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DetalleMascotaController {

  private final DetalleMascotaService service;

  @Autowired
  public DetalleMascotaController(DetalleMascotaService service) {
    this.service = service;
  }

  @RequestMapping(path = "/reporte/{id}", method = RequestMethod.GET)
  public ModelAndView verDetalle(@PathVariable Long id) {
    DatosDetalleMascotaDTO dto = service.obtenerDetalle(id);
    ModelAndView mav = new ModelAndView("detalle-mascota");
    mav.addObject("mascota", dto);
    return mav;
  }
}
