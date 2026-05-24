package com.tallerwebi.dominio.service.impl;

import com.tallerwebi.presentacion.dto.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import javax.transaction.Transactional;

import com.tallerwebi.dominio.repository.RepositorioUsuario;
import com.tallerwebi.dominio.repository.ServicioLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

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

  @Override
  public void registrar(Usuario usuario) throws UsuarioExistente {
    logger.info("Intentando registrar usuario con el email: {}", usuario.getEmail());
    Usuario usuarioEncontrado = repositorioUsuario.buscarUsuario(
      usuario.getEmail(),
      usuario.getPassword()
    );
    if (usuarioEncontrado != null) {
      logger.warn("El usuario con el email {} ya existe", usuario.getEmail());
      throw new UsuarioExistente();
    }
    repositorioUsuario.guardar(usuario);
    logger.info("Usuario registrado exitosamente con el email: {}", usuario.getEmail());
  }
}
