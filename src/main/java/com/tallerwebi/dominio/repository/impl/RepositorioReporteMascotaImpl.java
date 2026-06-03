package com.tallerwebi.dominio.repository.impl;

import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.repository.RepositorioReporteMascota;
import com.tallerwebi.dominio.repository.RepositorioUsuario;
import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.hibernate.criterion.Restrictions;
import java.util.List;

import java.util.List;

@Repository
public class RepositorioReporteMascotaImpl implements RepositorioReporteMascota {


    private SessionFactory sessionFactory;
    private RepositorioUsuario repositorioUsuario;
    @Autowired
    public RepositorioReporteMascotaImpl(SessionFactory sessionFactory,RepositorioUsuario repositorioUsuario) {
        this.sessionFactory = sessionFactory;
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public void guardarReporte(DatosReporteMascotaDTO datosReporteMascota,Usuario usuario) {

        Usuario usuarioBuscado = repositorioUsuario.buscar(usuario.getEmail());

        if(usuarioBuscado == null){
            throw new RuntimeException("No se puede guardar el reporte: El usuario no existe.");
        }

        ReporteMascota reporteMascota = new ReporteMascota();

        reporteMascota.setNombre(datosReporteMascota.getNombre());
        reporteMascota.setDescripcion(datosReporteMascota.getDescripcion());
        reporteMascota.setTipoDeReporte(datosReporteMascota.getTipoDeReporte());
        reporteMascota.setFecha(datosReporteMascota.getFecha());
        reporteMascota.setEspecie(datosReporteMascota.getEspecie());
        reporteMascota.setColor(datosReporteMascota.getColor());
        reporteMascota.setUbicacion(datosReporteMascota.getUbicacion());
        reporteMascota.setRaza(datosReporteMascota.getRaza());
        reporteMascota.setTamano(datosReporteMascota.getTamano());
        reporteMascota.setUsuario(usuario);
        sessionFactory.getCurrentSession().save(reporteMascota);

    }

    @Override
    public List buscarPorUsuario(Usuario usuario) {
        return sessionFactory.getCurrentSession()
                .createCriteria(ReporteMascota.class)
                .add(org.hibernate.criterion.Restrictions.eq("usuario", usuario))
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ReporteMascota> obtenerTodosLosReportes() {
        return sessionFactory.getCurrentSession()
                .createCriteria(ReporteMascota.class)
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ReporteMascota> buscarReportes(String busqueda) {
        if (busqueda == null || busqueda.trim().isEmpty()) {
            return obtenerTodosLosReportes();
        }
        String wildcard = "%" + busqueda.toLowerCase().trim() + "%";
        return sessionFactory.getCurrentSession()
                .createCriteria(ReporteMascota.class)
                .add(Restrictions.or(
                        Restrictions.ilike("especie", wildcard),
                        Restrictions.ilike("nombre", wildcard)
                ))
                .list();
    }
}
