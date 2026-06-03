package com.tallerwebi.dominio.repository;

import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.presentacion.dto.DatosRegistroDTO;

public interface ServicioLogin {
  Usuario consultarUsuario(String email, String password);
 // void registrar(Usuario usuario) throws UsuarioExistente;

  Usuario registrar(DatosRegistroDTO datosRegistroDTO) throws UsuarioExistente;
}
