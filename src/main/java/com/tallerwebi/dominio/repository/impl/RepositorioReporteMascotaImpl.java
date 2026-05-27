package com.tallerwebi.dominio.repository.impl;

import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.repository.RepositorioReporteMascota;
import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioReporteMascotaImpl implements RepositorioReporteMascota {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void guardarReporte(DatosReporteMascotaDTO datosReporteMascota) {

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
        //FALTA LA IMAGEN
        sessionFactory.getCurrentSession().save(reporteMascota);

    }
}
