package com.tallerwebi.dominio.repository.impl;

import com.tallerwebi.dominio.model.Foto;
import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.repository.RepositorioReporteMascota;
import com.tallerwebi.dominio.repository.RepositorioUsuario;
import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import java.util.List;
import java.util.UUID;
import java.io.File;

@Repository
public class RepositorioReporteMascotaImpl implements RepositorioReporteMascota {


    @Autowired
    private javax.servlet.ServletContext servletContext;

    private SessionFactory sessionFactory;
    private RepositorioUsuario repositorioUsuario;
    @Autowired
    public RepositorioReporteMascotaImpl(SessionFactory sessionFactory,RepositorioUsuario repositorioUsuario) {
        this.sessionFactory = sessionFactory;
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public void guardarReporte(DatosReporteMascotaDTO datosReporteMascota, Usuario usuario) {
    Usuario usuarioBuscado = repositorioUsuario.buscar(usuario.getEmail());
    if (usuarioBuscado == null) {
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
    reporteMascota.setRegistroActivo(true);

    sessionFactory.getCurrentSession().save(reporteMascota);

    // Procesar la imagen
        if (datosReporteMascota.getImagenes() != null && !datosReporteMascota.getImagenes().isEmpty()) {
            try {
                String uploadDir = "src/main/webapp/img/";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                for (MultipartFile imagen : datosReporteMascota.getImagenes()) {
                    if (!imagen.isEmpty()) {
                        String nombreArchivo = UUID.randomUUID().toString() + ".png";

                        // Guardar físico
                        File fileSrc = new File(dir, nombreArchivo);
                        imagen.transferTo(fileSrc);

                        // Crear entidad Foto por cada archivo
                        Foto foto = new Foto();
                        foto.setImg(nombreArchivo);
                        foto.setReporteMascota(reporteMascota);
                        sessionFactory.getCurrentSession().save(foto);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Error al guardar las imágenes", e);
            }
    }
}

    @Override
    public List buscarPorUsuario(Usuario usuario) {
        return sessionFactory.getCurrentSession()
                .createCriteria(ReporteMascota.class)
                .add(org.hibernate.criterion.Restrictions.eq("usuario", usuario))
                .setResultTransformer(org.hibernate.criterion.CriteriaSpecification.DISTINCT_ROOT_ENTITY)
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ReporteMascota> obtenerTodosLosReportes() {
        List<ReporteMascota> reportes = sessionFactory.getCurrentSession()
                .createCriteria(ReporteMascota.class)
                .setResultTransformer(org.hibernate.criterion.CriteriaSpecification.DISTINCT_ROOT_ENTITY)
                .list();
        for (ReporteMascota r : reportes) {
            if (r.getFotos() != null) {
                r.getFotos().size();
            }
        }
        return reportes;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ReporteMascota> buscarReportes(String busqueda) {
        if (busqueda == null || busqueda.trim().isEmpty()) {
            return sessionFactory.getCurrentSession()
                    .createCriteria(ReporteMascota.class)
                    .add(Restrictions.eq("registroActivo", true))
                    .setResultTransformer(org.hibernate.criterion.CriteriaSpecification.DISTINCT_ROOT_ENTITY)
                    .list();
        }
        String wildcard = "%" + busqueda.toLowerCase().trim() + "%";
        return sessionFactory.getCurrentSession()
                .createCriteria(ReporteMascota.class)
                .add(Restrictions.eq("registroActivo", true))
                .add(Restrictions.or(
                        Restrictions.ilike("especie", wildcard),
                        Restrictions.ilike("nombre", wildcard)
                ))
                .list();
    }

    @Override
    public ReporteMascota buscarPorId(Long id) {
        ReporteMascota reporte = sessionFactory.getCurrentSession().get(ReporteMascota.class, id);

        if (reporte != null && reporte.getFotos() != null) {
            reporte.getFotos().size();
        }
        return reporte;
    }

    @Override
    public void actualizarReporte(ReporteMascota reporteMascota) {
        sessionFactory.getCurrentSession().update(reporteMascota);
    }

    @Override
    public void eliminarReporte(ReporteMascota reporte) {
        sessionFactory.getCurrentSession().delete(reporte);
    }
}
