package com.tallerwebi.dominio.repository.impl;

import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.repository.RepositorioMascota;
import com.tallerwebi.presentacion.dto.Mascota;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("repositorioMascota")
public class RepositorioMascotaImpl implements RepositorioMascota {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioMascotaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Mascota> obtenerTodasLasMascotas() {
        List<ReporteMascota> reportes = sessionFactory.getCurrentSession()
                .createCriteria(ReporteMascota.class)
                .add(Restrictions.eq("registroActivo", true))
                .list();

        List<Mascota> mascotas = new ArrayList<>();
        for (ReporteMascota r : reportes) {
            Mascota m = new Mascota();
            m.setId(r.getId());
            m.setNombre(r.getNombre());
            m.setTipo(r.getTipoDeReporte());      
            m.setFecha(java.sql.Date.valueOf(r.getFecha())); 
            m.setEstado(r.getTipoDeReporte().equals("Perdida") ? "Perdido" : "Encontrado");
            m.setImagen("/img/" + (r.getFotos() != null && !r.getFotos().isEmpty() ? r.getFotos().get(0).getImg() : "default-pet.png"));
            mascotas.add(m);
        }
        return mascotas;
    }

    
}