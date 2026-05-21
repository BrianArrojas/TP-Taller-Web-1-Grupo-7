package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ContactoController {

  @RequestMapping(path = "/contacto/{id}", method = RequestMethod.GET)
  public ModelAndView formularioContacto(@PathVariable Long id) {
    ModelAndView mav = new ModelAndView("contacto");
    mav.addObject("id", id);
    return mav;
  }
}
