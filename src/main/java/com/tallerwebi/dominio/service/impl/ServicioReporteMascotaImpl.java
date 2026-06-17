package com.tallerwebi.dominio.service.impl;

import com.tallerwebi.dominio.excepcion.FechaInvalidaException;
import com.tallerwebi.dominio.excepcion.FormatoImagenInvalidaException;
import com.tallerwebi.dominio.excepcion.ImagenExcedeTamanoException;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.repository.RepositorioReporteMascota;
import com.tallerwebi.dominio.repository.RepositorioUsuario;
import com.tallerwebi.dominio.service.ServicioReporteMascota;
import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServicioReporteMascotaImpl implements ServicioReporteMascota {

    private  RepositorioReporteMascota repositorioReporteMascota;
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioReporteMascotaImpl(RepositorioReporteMascota repositorioReporteMascota,RepositorioUsuario repositorioUsuario) {
        this.repositorioReporteMascota = repositorioReporteMascota;
        this.repositorioUsuario = repositorioUsuario;
    }

    public Boolean validarQueLaImagenCumplaConFormato(DatosReporteMascotaDTO datosReporteMascotaDTO) {
        String tipo = datosReporteMascotaDTO.getImagen().getContentType();

        if (!tipo.equals("image/png") && !tipo.equals("image/jpeg")) {
            throw new FormatoImagenInvalidaException("El formato de la imagen debe ser PNG o JPG.");
        }



        return true;
    }

    public Boolean validarQueFechaDeReporteNoSeaFutura(DatosReporteMascotaDTO datosReporteMascotaDTO) {
        if (datosReporteMascotaDTO.getFecha() == null) {
            throw new FechaInvalidaException("La fecha ingresada no puede ser futura al dia de hoy.");
        }
        if (datosReporteMascotaDTO.getFecha().isAfter(LocalDate.now())) {
            throw new FechaInvalidaException("La fecha ingresada no puede ser futura al dia de hoy.");
        }

        return true;
    }

    public Boolean guardarReporteMascota(DatosReporteMascotaDTO datosReporteMascotaDTO,String email) throws UsuarioExistente {

        if(datosReporteMascotaDTO.getFecha() != null) {

            Usuario usuarioBuscado = repositorioUsuario.buscar(email);

            if (usuarioBuscado == null) {
                throw new UsuarioExistente();
            }
            repositorioReporteMascota.guardarReporte(datosReporteMascotaDTO,usuarioBuscado);
            return true;
        }
        return false;
    }

    @Override
    public void cancelarReporte(Long id) {
        ReporteMascota reporte = repositorioReporteMascota.buscarPorId(id);
        if (reporte != null) {
            reporte.setRegistroActivo(false);
        }
    }

    @Override
    public ReporteMascota buscarReporte(Long id) {
        return repositorioReporteMascota.buscarPorId(id);
    }

    @Override
    public void actualizarReporte(DatosReporteMascotaDTO datosReporteMascotaDTO) {

        ReporteMascota reporteExistente = repositorioReporteMascota.buscarPorId(datosReporteMascotaDTO.getId());

        if (reporteExistente != null) {

            reporteExistente.setNombre(datosReporteMascotaDTO.getNombre());
            reporteExistente.setDescripcion(datosReporteMascotaDTO.getDescripcion());
            reporteExistente.setUbicacion(datosReporteMascotaDTO.getUbicacion());
            reporteExistente.setRaza(datosReporteMascotaDTO.getRaza());
            reporteExistente.setColor(datosReporteMascotaDTO.getColor());
            reporteExistente.setTamano(datosReporteMascotaDTO.getTamano());
            reporteExistente.setEspecie(datosReporteMascotaDTO.getEspecie());
            reporteExistente.setTipoDeReporte(datosReporteMascotaDTO.getTipoDeReporte());
            reporteExistente.setFecha(datosReporteMascotaDTO.getFecha());

            repositorioReporteMascota.actualizarReporte(reporteExistente);

        }
    }
    @Override
    public Boolean validarQueLaImagenNoExcedaTamano(DatosReporteMascotaDTO datosReporteMascotaDTO) {
        String tipo = datosReporteMascotaDTO.getImagen().getContentType();

        long limiteMaximo = 20 * 1024 * 1024;
        long tamanoImagen = datosReporteMascotaDTO.getImagen().getSize();
        if (tamanoImagen >= limiteMaximo) {
            throw new ImagenExcedeTamanoException("La foto es demasiado pesada. El tamaño máximo permitido es 20 MB");
        }

        return true;
    }

    @Override
    public List<ReporteMascota> obtenerTodosLosReportes() {
        return repositorioReporteMascota.obtenerTodosLosReportes();
    }

    @Override
    public List<ReporteMascota> buscarPorUsuario(Usuario usuario) {
        return repositorioReporteMascota.buscarPorUsuario(usuario);
    }

    @Override
    public List<DatosReporteMascotaDTO> listarMascotas(String busqueda) {
        List<ReporteMascota> reportes = repositorioReporteMascota.buscarReportes(busqueda);
        return reportes.stream().map(reporte -> {
            DatosReporteMascotaDTO dto = new DatosReporteMascotaDTO();
            dto.setNombre(reporte.getNombre());
            dto.setId(reporte.getId());
            dto.setEspecie(reporte.getEspecie());
            dto.setFecha(reporte.getFecha());
            dto.setTipoDeReporte(reporte.getTipoDeReporte());
            dto.setRaza(reporte.getRaza());
            dto.setColor(reporte.getColor());
            dto.setTamano(reporte.getTamano());
            dto.setUbicacion(reporte.getUbicacion());
            dto.setDescripcion(reporte.getDescripcion());
            if (reporte.getFotos() != null && !reporte.getFotos().isEmpty()) {
                String imgPath = reporte.getFotos().get(0).getImg();
                if (imgPath.startsWith("img/")) {
                    dto.setRutaImagen("/" + imgPath);
                } else if (imgPath.startsWith("/img/")) {
                    dto.setRutaImagen(imgPath);
                } else {
                    dto.setRutaImagen("/img/" + imgPath);
                }
            }
            return dto;
        }).collect(Collectors.toList());
    }

}
