package com.tallerwebi.dominio.service.impl;

import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.repository.RepositorioReporteMascota;
import com.tallerwebi.dominio.repository.RepositorioUsuario;
import com.tallerwebi.dominio.service.ServicioReporteMascota;
import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServicioReporteMascotaImpl implements ServicioReporteMascota {

    private RepositorioReporteMascota repositorioReporteMascota;
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioReporteMascotaImpl(RepositorioReporteMascota repositorioReporteMascota, RepositorioUsuario repositorioUsuario) {
        this.repositorioReporteMascota = repositorioReporteMascota;
        this.repositorioUsuario = repositorioUsuario;
    }

    public Boolean validarQueLaImagenCumplaConFormato(DatosReporteMascotaDTO datosReporteMascotaDTO) {
        if (datosReporteMascotaDTO.getImagenes() != null) {
            for (MultipartFile imagen : datosReporteMascotaDTO.getImagenes()) {
                if (!imagen.isEmpty()) {
                    String tipo = imagen.getContentType();
                    if (tipo == null || (!tipo.equals("image/png") && !tipo.equals("image/jpeg"))) {
                        throw new FormatoImagenInvalidaException("Una de las imágenes tiene un formato inválido. Debe ser PNG o JPG.");
                    }
                }
            }
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

            if (datosReporteMascotaDTO.getFotosAEliminar() != null && !datosReporteMascotaDTO.getFotosAEliminar().isEmpty()) {
                reporteExistente.getFotos().removeIf(foto -> datosReporteMascotaDTO.getFotosAEliminar().contains(foto.getId()));
            }

            int fotosRestantes = reporteExistente.getFotos().size();
            int fotosNuevas = 0;
            if (datosReporteMascotaDTO.getNuevasImagenes() != null) {

                fotosNuevas = (int) datosReporteMascotaDTO.getNuevasImagenes().stream().filter(img -> !img.isEmpty()).count();
            }

            if (fotosRestantes + fotosNuevas > 4) {
                throw new CantidadFotosExcedidaException("No puedes tener más de 4 imágenes en total.");
            }

            if (fotosNuevas > 0) {
                try {
                    String uploadDir = "src/main/webapp/img/";
                    File dir = new File(uploadDir);
                    if (!dir.exists()) dir.mkdirs();

                    for (MultipartFile archivoNuevo : datosReporteMascotaDTO.getNuevasImagenes()) {
                        if (!archivoNuevo.isEmpty()) {

                            String nombreArchivo = java.util.UUID.randomUUID().toString() + ".png";

                            File fileSrc = new File(dir, nombreArchivo);
                            archivoNuevo.transferTo(fileSrc);

                            com.tallerwebi.dominio.model.Foto nuevaFoto = new com.tallerwebi.dominio.model.Foto();
                            nuevaFoto.setImg(nombreArchivo);
                            nuevaFoto.setReporteMascota(reporteExistente);

                            reporteExistente.getFotos().add(nuevaFoto);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Error al guardar las nuevas imágenes", e);
                }
            }

            repositorioReporteMascota.actualizarReporte(reporteExistente);

        }
    }
    @Override
    public Boolean validarQueLaImagenNoExcedaTamano(DatosReporteMascotaDTO datosReporteMascotaDTO) {
        if (datosReporteMascotaDTO.getImagenes() != null) {
            long limiteMaximo = 20 * 1024 * 1024; // 20 MB
            for (MultipartFile imagen : datosReporteMascotaDTO.getImagenes()) {
                if (!imagen.isEmpty()) {
                    if (imagen.getSize() >= limiteMaximo) {
                        throw new ImagenExcedeTamanoException("Una de las fotos es demasiado pesada. El tamaño máximo es 20 MB.");
                    }
                }
            }
        }

        return true;
    }

    @Override
    public List<ReporteMascota> listarReportes(String busqueda) {
        return repositorioReporteMascota.buscarReportes(busqueda);
    }

    @Override
    public void validarCantidadDeFotos(DatosReporteMascotaDTO dto) {
        if (dto.getImagenes() != null && dto.getImagenes().size() > 4) {
            throw new CantidadFotosExcedidaException("No puedes subir más de 4 imágenes por reporte.");
        }
    }

    @Override
    public List<ReporteMascota> obtenerTodosLosReportesActivos() {
        List<ReporteMascota> todosLosReportes = repositorioReporteMascota.obtenerTodosLosReportesActivos();

        return todosLosReportes.stream()
                .filter(r -> r.getRegistroActivo() != null && r.getRegistroActivo())
                .collect(Collectors.toList());
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

    @Override
    public List<ReporteMascota> buscarReportesFiltradosYPaginados(
            String busqueda, String tipoDeReporte, String especie,
            String fechaDesde, String fechaHasta, int page, int pageSize) {
        
        LocalDate fDesde = null;
        if (fechaDesde != null && !fechaDesde.trim().isEmpty()) {
            try {
                fDesde = LocalDate.parse(fechaDesde.trim());
            } catch (Exception e) {
            }
        }
        
        LocalDate fHasta = null;
        if (fechaHasta != null && !fechaHasta.trim().isEmpty()) {
            try {
                fHasta = LocalDate.parse(fechaHasta.trim());
            } catch (Exception e) {
            }
        }
        
        return repositorioReporteMascota.buscarReportesFiltradosYPaginados(
            busqueda, tipoDeReporte, especie, fDesde, fHasta, page, pageSize);
    }

    @Override
    public int contarReportesFiltrados(
            String busqueda, String tipoDeReporte, String especie,
            String fechaDesde, String fechaHasta) {
        
        LocalDate fDesde = null;
        if (fechaDesde != null && !fechaDesde.trim().isEmpty()) {
            try {
                fDesde = LocalDate.parse(fechaDesde.trim());
            } catch (Exception e) {
            }
        }
        
        LocalDate fHasta = null;
        if (fechaHasta != null && !fechaHasta.trim().isEmpty()) {
            try {
                fHasta = LocalDate.parse(fechaHasta.trim());
            } catch (Exception e) {
            }
        }
        
        return repositorioReporteMascota.contarReportesFiltrados(
            busqueda, tipoDeReporte, especie, fDesde, fHasta);
    }
}
