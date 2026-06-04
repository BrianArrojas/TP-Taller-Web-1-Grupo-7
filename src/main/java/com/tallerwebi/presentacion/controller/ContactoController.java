package com.tallerwebi.presentacion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ContactoController {

    @RequestMapping(path = "/contacto/{id}", method = RequestMethod.GET)
    public ModelAndView mostrarFormulario(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("contacto");
        mav.addObject("id", id);
        return mav;
    }

    @RequestMapping(path = "/contacto/{id}", method = RequestMethod.POST)
    public ModelAndView enviarMensaje(@PathVariable Long id,@RequestParam("nombre") String nombre,@RequestParam("email") String email,@RequestParam("mensaje") String mensaje) {
        ModelAndView mav = new ModelAndView("contacto-exitoso");
        mav.addObject("id", id);
        mav.addObject("nombre", nombre);
        return mav;
    }
}