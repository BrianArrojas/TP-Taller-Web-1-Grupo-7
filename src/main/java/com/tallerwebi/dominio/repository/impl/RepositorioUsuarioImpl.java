package com.tallerwebi.dominio.repository.impl;

import com.tallerwebi.dominio.repository.RepositorioUsuario;
import com.tallerwebi.dominio.model.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository("repositorioUsuario")
public class RepositorioUsuarioImpl implements RepositorioUsuario {

  private static final Logger logger = LoggerFactory.getLogger(RepositorioUsuarioImpl.class);

  private SessionFactory sessionFactory;

  @Autowired
  public RepositorioUsuarioImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public Usuario buscarUsuario(String email, String password) {
    logger.debug("Buscando usuario en BD por email y password: {}", email);
    /* Se utiliza sessionFactory.getCurrentSession() directamente para que el recurso sea gestionado por Spring y PMD no exija cerrarlo manualmente */
    return (Usuario) sessionFactory
      .getCurrentSession()
      .createCriteria(Usuario.class)
      .add(Restrictions.eq("email", email))
      .add(Restrictions.eq("password", password))
      .uniqueResult();
  }

  @Override
  public void guardar(Usuario usuario) {
    logger.debug("Guardando nuevo usuario en BD: {}", usuario.getEmail());
    sessionFactory.getCurrentSession().save(usuario);
  }

  @Override
  public Usuario buscar(String email) {
    logger.debug("Buscando usuario en BD por email: {}", email);
    return (Usuario) sessionFactory
      .getCurrentSession()
      .createCriteria(Usuario.class)
      .add(Restrictions.eq("email", email))
      .uniqueResult();
  }

  @Override
  public void modificar(Usuario usuario) {
    logger.debug("Modificando usuario en BD: {}", usuario.getEmail());
    Session session = sessionFactory.getCurrentSession();
    Usuario usuarioExistente = session.get(Usuario.class, usuario.getId());
//    sessionFactory.getCurrentSession().update(usuario);
    if (usuarioExistente!= null) {
      session.merge(usuario);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public java.util.List<Usuario> obtenerTodos() {
    logger.debug("Obteniendo todos los usuarios de la BD");
    return sessionFactory.getCurrentSession()
      .createCriteria(Usuario.class)
      .list();
  }

  @Override
  @SuppressWarnings("unchecked")
  public java.util.List<Usuario> buscarPorEmail(String email) {
    logger.debug("Buscando usuarios por email similar a: {}", email);
    return sessionFactory.getCurrentSession()
      .createCriteria(Usuario.class)
      .add(Restrictions.ilike("email", "%" + email + "%"))
      .list();
  }

  @Override
  public Usuario buscarPorId(Long id) {
    logger.debug("Buscando usuario por ID: {}", id);
    return sessionFactory.getCurrentSession().get(Usuario.class, id);
  }
}
