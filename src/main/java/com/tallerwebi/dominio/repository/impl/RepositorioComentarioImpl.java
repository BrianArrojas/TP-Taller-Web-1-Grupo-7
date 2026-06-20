package com.tallerwebi.dominio.repository.impl;

import com.tallerwebi.dominio.model.Comentario;
import com.tallerwebi.dominio.repository.RepositorioComentario;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioComentario")
public class RepositorioComentarioImpl implements RepositorioComentario {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioComentarioImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Comentario comentario) {
        sessionFactory.getCurrentSession().save(comentario);
    }

    @Override
    public List<Comentario> obtenerTodosComentariosDelReporte(Long reporteId) {
        return (List<Comentario>) sessionFactory.getCurrentSession()
                .createCriteria(Comentario.class)
                .add(Restrictions.eq("reporteMascota.id", reporteId))
                .add(Restrictions.isNull("codigoChat"))
                .addOrder(Order.asc("fechaCreacion"))
                .list();
    }

    @Override
    public List<Comentario> obtenerMensajesPorCodigoChat(String codigoChat) {
        return (List<Comentario>) sessionFactory.getCurrentSession()
                .createCriteria(Comentario.class)
                .add(Restrictions.eq("codigoChat", codigoChat))
                .addOrder(Order.asc("fechaCreacion"))
                .list();
    }

    @Override
    public Comentario buscarChatDelInteresado(Long reporteId, Long interesadoId) {
        return (Comentario) sessionFactory.getCurrentSession()
                .createCriteria(Comentario.class)
                .add(Restrictions.eq("reporteMascota.id", reporteId))
                .add(Restrictions.eq("idInteresado", interesadoId))
                .add(Restrictions.isNotNull("codigoChat"))
                .setMaxResults(1)
                .uniqueResult();
    }

    @Override
    public List<Comentario> obtenerTodosMensajesDelReporte(Long reporteId) {
        return (List<Comentario>) sessionFactory.getCurrentSession()
                .createCriteria(Comentario.class)
                .add(Restrictions.eq("reporteMascota.id", reporteId))
                .add(Restrictions.isNotNull("codigoChat"))
                .addOrder(Order.asc("fechaCreacion"))
                .list();
    }
}