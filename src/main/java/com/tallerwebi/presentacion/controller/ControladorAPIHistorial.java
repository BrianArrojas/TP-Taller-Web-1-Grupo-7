package com.tallerwebi.presentacion.controller;

import com.tallerwebi.dominio.service.ServicioChatPrivado;
import com.tallerwebi.dominio.service.ServicioDetalleMascota;
import com.tallerwebi.presentacion.dto.MensajeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ControladorAPIHistorial {

    private final ServicioChatPrivado servicioChatPrivado;
    private final ServicioDetalleMascota servicioDetalleMascota;

    @Autowired
    public ControladorAPIHistorial(ServicioChatPrivado servicioChatPrivado,
                                   ServicioDetalleMascota servicioDetalleMascota) {
        this.servicioChatPrivado = servicioChatPrivado;
        this.servicioDetalleMascota = servicioDetalleMascota;
    }

    @RequestMapping(path = "/api/chats/{codigoChat}/historial", method = RequestMethod.GET)
    @ResponseBody
    public List<MensajeDTO> obtenerHistorial(@PathVariable String codigoChat) {
        return servicioChatPrivado.obtenerHistorial(codigoChat);
    }

    @RequestMapping(path = "/api/reportes/{idReporte}/comentarios", method = RequestMethod.GET)
    @ResponseBody
    public List<MensajeDTO> obtenerComentariosPublicos(@PathVariable Long idReporte) {
        return servicioDetalleMascota.obtenerComentariosPublicos(idReporte);
    }
}