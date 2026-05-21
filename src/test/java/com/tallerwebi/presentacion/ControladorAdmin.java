package com.tallerwebi.presentacion;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorAdmin {

  @RequestMapping(path = "/admin", method = RequestMethod.GET)
  public ModelAndView irAAdministracion(HttpServletRequest request) {
    ModelMap modelo = new ModelMap();
    String rol = (String) request.getSession().getAttribute("ROL");

    if (rol != null && rol.equals("ADMIN")) {
      return new ModelAndView("administracion");
    } else {
      modelo.put("error", "Acceso Denegado");
      return new ModelAndView("redirect:/home", modelo);
    }
  }
}
