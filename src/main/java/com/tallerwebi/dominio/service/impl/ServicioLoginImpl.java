package com.tallerwebi.dominio.service.impl;

import com.tallerwebi.dominio.excepcion.PasswordInvalidaException;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import javax.transaction.Transactional;

import com.tallerwebi.dominio.repository.RepositorioUsuario;
import com.tallerwebi.dominio.repository.ServicioLogin;
import com.tallerwebi.presentacion.dto.DatosRegistroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

  private static final int CANTIDAD_MAXIMA_CARACTERES = 6;

  private static final Logger logger = LoggerFactory.getLogger(ServicioLoginImpl.class);

  private RepositorioUsuario repositorioUsuario;

  @Autowired
  public ServicioLoginImpl(RepositorioUsuario repositorioUsuario) {
    this.repositorioUsuario = repositorioUsuario;
  }

  @Override
  public Usuario consultarUsuario(String email, String password) {
    logger.info("Intentando iniciar sesión con el email: {}", email);
    Usuario usuario = repositorioUsuario.buscarUsuario(email, password);
    if (usuario != null) {
        logger.info("Usuario encontrado con el email: {}", email);
    } else {
        logger.warn("Usuario no encontrado o contraseña incorrecta para el email: {}", email);
    }
    return usuario;
  }

//  @Override
//  public void registrar(Usuario usuario) throws UsuarioExistente {
//    logger.info("Intentando registrar usuario con el email: {}", usuario.getEmail());
//    Usuario usuarioEncontrado = repositorioUsuario.buscarUsuario(
//      usuario.getEmail(),
//      usuario.getPassword()
//    );
//    if (usuarioEncontrado != null) {
//      logger.warn("El usuario con el email {} ya existe", usuario.getEmail());
//      throw new UsuarioExistente();
//    }
//    repositorioUsuario.guardar(usuario);
//    logger.info("Usuario registrado exitosamente con el email: {}", usuario.getEmail());
//  }

  @Override
  public Usuario registrar(DatosRegistroDTO datosRegistroDTO) throws UsuarioExistente {

    if (datosRegistroDTO.getPassword().length() < CANTIDAD_MAXIMA_CARACTERES) {
      throw new PasswordInvalidaException("La password debe tener al menos 6 caracteres");
    }

    logger.info("Intentando registrar usuario con el email: {}", datosRegistroDTO.getMail());

    Usuario usuarioEncontrado = repositorioUsuario.buscar(
            datosRegistroDTO.getMail()
    );

    if (usuarioEncontrado != null) {
      logger.warn("El usuario con el email {} ya existe", datosRegistroDTO.getMail());
      throw new UsuarioExistente();
    }

    Usuario nuevoUsuario = new Usuario();
    nuevoUsuario.setEmail(datosRegistroDTO.getMail());
    nuevoUsuario.setPassword(datosRegistroDTO.getPassword());
    nuevoUsuario.setNombre(datosRegistroDTO.getNombre());
    nuevoUsuario.setApellido(datosRegistroDTO.getApellido());
    nuevoUsuario.setTelefono(datosRegistroDTO.getTelefono());
    if ("test@unlam.edu.ar".equalsIgnoreCase(datosRegistroDTO.getMail())) {
      nuevoUsuario.setRol("ADMIN");
    } else {
      nuevoUsuario.setRol("USUARIO");
    }
    nuevoUsuario.activar();

    repositorioUsuario.guardar(nuevoUsuario);

    logger.info("Usuario registrado exitosamente con el email: {}", nuevoUsuario.getEmail());

    return nuevoUsuario;
  }
}
