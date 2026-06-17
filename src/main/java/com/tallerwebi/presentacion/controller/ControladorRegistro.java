package com.tallerwebi.presentacion.controller;

import com.tallerwebi.dominio.excepcion.PasswordInvalidaException;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.repository.ServicioLogin;
import com.tallerwebi.presentacion.dto.DatosRegistroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorRegistro {

    private ServicioLogin servicioLogin ;

    @Autowired
    public ControladorRegistro(ServicioLogin servicioLogin) {
        this.servicioLogin = servicioLogin;
    }

    @RequestMapping("/nuevo-usuario")
    public ModelAndView nuevoUsuario() {
        ModelMap model = new ModelMap();
        model.put("usuario", new DatosRegistroDTO());
        return new ModelAndView("nuevo-usuario",model);
    }


    @RequestMapping(path = "/registrarme", method = RequestMethod.POST)
    public ModelAndView registrar(@ModelAttribute("usuario") DatosRegistroDTO datosRegistroDTO) {
        ModelMap model = new ModelMap();
        String mensajeError = validarDatosRegistro(datosRegistroDTO);

        if (mensajeError != null) {
            model.put("mensaje", mensajeError);
            model.put("usuario", datosRegistroDTO);
            return new ModelAndView("nuevo-usuario", model);
        }

        try {
            servicioLogin.registrar(datosRegistroDTO);
        } catch (PasswordInvalidaException ex) {
            model.put("mensaje", "Las contraseñas debe tener al menos 6 caracteres");
            model.put("usuario", datosRegistroDTO);
            return new ModelAndView("nuevo-usuario", model);
        }catch (UsuarioExistente ex) {
            model.put("mensaje", "El correo electrónico ya se encuentra registrado");
            model.put("usuario", datosRegistroDTO);
            return new ModelAndView("nuevo-usuario", model);
        }

        model.put("mensaje", "El registro fue exitoso");
        return new ModelAndView("redirect:/login", model);
    }

    public String validarDatosRegistro(DatosRegistroDTO datosRegistroDTO) {

        if (datosRegistroDTO.getNombre() == null || datosRegistroDTO.getNombre().isEmpty()) {
            return "El nombre es obligatorio";
        }
        if (datosRegistroDTO.getApellido() == null || datosRegistroDTO.getApellido().isEmpty()) {
            return "El apellido es obligatorio";
        }
        if (datosRegistroDTO.getTelefono() == null || datosRegistroDTO.getTelefono().isEmpty()) {
            return "El teléfono es obligatorio";
        }

        if (datosRegistroDTO.getTelefono().length() != 10) {
            return "El teléfono debe tener 10 números";
        }

        if (datosRegistroDTO.getMail() == null || datosRegistroDTO.getMail().isEmpty()) {
            return "El email es obligatorio";
        }

        if (datosRegistroDTO.getPassword() == null || datosRegistroDTO.getPassword().isEmpty()) {
            return "La password es obligatoria";
        }

        if (!datosRegistroDTO.getPassword().equals(datosRegistroDTO.getConfirmarPassword())) {
            return "Las contraseñas no coinciden";
        }

        if (
                !datosRegistroDTO.getMail().contains("@") || !datosRegistroDTO.getMail().endsWith(".com.ar")
        ) {
            return "El email debe tener el formato correcto, Ej : usuario@gmail.com.ar";
        }

        return null;
    }
}
