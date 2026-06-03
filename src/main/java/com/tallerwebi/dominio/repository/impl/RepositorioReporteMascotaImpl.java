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
import java.util.List;
import java.util.UUID;
import java.io.File;

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
    if (datosReporteMascota.getImagen() != null && !datosReporteMascota.getImagen().isEmpty()) {
        try {
            // Directorio donde se almacenarán las imágenes
            String uploadDir = "src/main/webapp/img/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Generar un nombre único para la imagen
            String originalFilename = datosReporteMascota.getImagen().getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String nombreArchivo = UUID.randomUUID().toString() + extension;

            // Guardar el archivo en el sistema de archivos
            File file = new File(dir, nombreArchivo);
            datosReporteMascota.getImagen().transferTo(file);

            // Crear la entidad Foto y persistirla
            Foto foto = new Foto();
            foto.setImg(nombreArchivo);  // almacenamos solo el nombre del archivo
            foto.setReporteMascota(reporteMascota);
            sessionFactory.getCurrentSession().save(foto);

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen", e);
        }
    }
}

    @Override
    public List buscarPorUsuario(Usuario usuario) {
        return sessionFactory.getCurrentSession()
                .createCriteria(ReporteMascota.class)
                .add(org.hibernate.criterion.Restrictions.eq("usuario", usuario))
                .list();
    }

    @Override
    public ReporteMascota buscarPorId(Long id) {
        return sessionFactory.getCurrentSession().get(ReporteMascota.class, id);
    }
}
