package com.tallerwebi.presentacion.controller;

import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.service.ServicioReporteMascota;
import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorReporteMascota {

  private final ServicioReporteMascota servicioReporteMascota;
  @Autowired
  public ControladorReporteMascota(ServicioReporteMascota servicioReporteMascota) {
    this.servicioReporteMascota = servicioReporteMascota;
  }

  @RequestMapping("/realizar-reporte")
  public ModelAndView mostrarFormularioReporteMascota(HttpServletRequest request) {
    ModelMap modelo = new ModelMap();
    Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuario");

    if (usuarioLogueado == null) {
      return new ModelAndView("redirect:/login");
    }

    modelo.put("datosReporte", new DatosReporteMascotaDTO());
    return new ModelAndView("realizar-reporte", modelo);
  }

  @RequestMapping(path = "/procesando-reporte", method = RequestMethod.POST)
  public ModelAndView realizarReporte(
          @ModelAttribute("datosReporte") DatosReporteMascotaDTO datosReporteMascotaDTO, HttpServletRequest request
  ) {
    ModelMap modelo = new ModelMap();

    if (
            datosReporteMascotaDTO.getNombre() == null ||
                    datosReporteMascotaDTO.getNombre().isEmpty() ||
                    datosReporteMascotaDTO.getDescripcion() == null ||
                    datosReporteMascotaDTO.getDescripcion().isEmpty() ||
                    datosReporteMascotaDTO.getUbicacion() == null ||
                    datosReporteMascotaDTO.getUbicacion().isEmpty()
    ) {
      modelo.put("mensaje", "Debe completar todos los campos para realizar el reporte");
      modelo.put("datosReporte", datosReporteMascotaDTO);
      return new ModelAndView("realizar-reporte", modelo);
    }

    if (datosReporteMascotaDTO.getLatitud() == null || datosReporteMascotaDTO.getLatitud() == 0.0 ||
            datosReporteMascotaDTO.getLongitud() == null || datosReporteMascotaDTO.getLongitud() == 0.0
    ) {
      modelo.put("mensaje", "Debe marcar la ubicación de la mascota haciendo clic en el mapa.");
      modelo.put("datosReporte", datosReporteMascotaDTO);
      return new ModelAndView("realizar-reporte", modelo);
    }

    if(
            datosReporteMascotaDTO.getTipoDeReporte() == null ||
                    datosReporteMascotaDTO.getTipoDeReporte().isEmpty()||
                    datosReporteMascotaDTO.getTamano() == null ||
                    datosReporteMascotaDTO.getTamano().isEmpty()||
                    datosReporteMascotaDTO.getEspecie() == null ||
                    datosReporteMascotaDTO.getEspecie().isEmpty()
    ){
      modelo.put("mensaje", "Debe seleccionar una opcion de todos los desplegables");
      modelo.put("datosReporte", datosReporteMascotaDTO);
      return new ModelAndView("realizar-reporte", modelo);

    }

    if (datosReporteMascotaDTO.getImagenes() == null || datosReporteMascotaDTO.getImagenes().isEmpty()) {
      modelo.put("mensaje", "Debe adjuntar al menos una imagen");
      modelo.put("datosReporte", datosReporteMascotaDTO);
      return new ModelAndView("realizar-reporte", modelo);
    }

    try{
      servicioReporteMascota.validarQueLaImagenCumplaConFormato(datosReporteMascotaDTO);
    }catch(FormatoImagenInvalidaException exception){
      modelo.put("mensaje", exception.getMessage());
      modelo.put("datosReporte", datosReporteMascotaDTO);
      return new ModelAndView("realizar-reporte", modelo);
    } catch (ImagenExcedeTamanoException exception) {
      modelo.put("mensaje", "La foto es demasiado pesada. El tamaño máximo permitido es 20 MB");
      modelo.put("datosReporte", datosReporteMascotaDTO);
      return new ModelAndView("realizar-reporte", modelo);
    }
    try{
      servicioReporteMascota.validarQueFechaDeReporteNoSeaFutura(datosReporteMascotaDTO);
    }catch(FechaInvalidaException exception){
      modelo.put("mensaje", exception.getMessage());
      modelo.put("datosReporte", datosReporteMascotaDTO);
      return new ModelAndView("realizar-reporte", modelo);
    }
    try{
      servicioReporteMascota.validarQueLaImagenNoExcedaTamano(datosReporteMascotaDTO);
    }catch(ImagenExcedeTamanoException exception){
      modelo.put("mensaje", exception.getMessage());
      modelo.put("datosReporte", datosReporteMascotaDTO);
      return new ModelAndView("realizar-reporte", modelo);
    }

    try{
      servicioReporteMascota.validarCantidadDeFotos(datosReporteMascotaDTO);
    }catch(CantidadFotosExcedidaException exception){
      modelo.put("mensaje", exception.getMessage());
      modelo.put("datosReporte", datosReporteMascotaDTO);
      return new ModelAndView("realizar-reporte", modelo);
    }

    Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuario");

    if (usuarioLogueado == null) {
      return new ModelAndView("redirect:/login", modelo);
    }

    String emailUsuario = usuarioLogueado.getEmail();
    servicioReporteMascota.guardarReporteMascota(datosReporteMascotaDTO,emailUsuario);

    String nombreImagenPublicada = datosReporteMascotaDTO.getNombreImagenPublicada();
    String rutaOficialImagen = "/img/" + nombreImagenPublicada;

    modelo.put("tipoDeReporte", datosReporteMascotaDTO.getTipoDeReporte());
    modelo.put("especie", datosReporteMascotaDTO.getEspecie());
    modelo.put("tamano", datosReporteMascotaDTO.getTamano());
    modelo.put("raza", datosReporteMascotaDTO.getRaza());
    modelo.put("nombre", datosReporteMascotaDTO.getNombre());
    modelo.put("color", datosReporteMascotaDTO.getColor());
    modelo.put("descripcion", datosReporteMascotaDTO.getDescripcion());
    modelo.put("ubicacion", datosReporteMascotaDTO.getUbicacion());
    modelo.put("fecha",datosReporteMascotaDTO.getFecha());
    modelo.put("rutaImagen", rutaOficialImagen);
    modelo.put("mensaje", "El reporte se realizo correctamente");
    modelo.put("datosReporte", new DatosReporteMascotaDTO());
    return new ModelAndView("lista-de-reportes", modelo);
  }

  @RequestMapping(path = "/modificar-reporte", method = RequestMethod.POST)
  public ModelAndView modificarReporte(@ModelAttribute("datosReporte") DatosReporteMascotaDTO datosReporteMascotaDTO) {
    ModelMap modelo = new ModelMap();

    Long idFallo = datosReporteMascotaDTO.getId();

    if (
            datosReporteMascotaDTO.getNombre() == null || datosReporteMascotaDTO.getNombre().isEmpty() ||
                    datosReporteMascotaDTO.getDescripcion() == null || datosReporteMascotaDTO.getDescripcion().isEmpty() ||
                    datosReporteMascotaDTO.getUbicacion() == null || datosReporteMascotaDTO.getUbicacion().isEmpty()
    ) {
      modelo.addAttribute("error", "Debe completar todos los campos de texto para actualizar el reporte");
      return new ModelAndView("redirect:/mis-reportes?idModal=" + idFallo, modelo);
    }

    if (
            datosReporteMascotaDTO.getTipoDeReporte() == null || datosReporteMascotaDTO.getTipoDeReporte().isEmpty() ||
                    datosReporteMascotaDTO.getTamano() == null || datosReporteMascotaDTO.getTamano().isEmpty() ||
                    datosReporteMascotaDTO.getEspecie() == null || datosReporteMascotaDTO.getEspecie().isEmpty()
    ) {
      modelo.addAttribute("error", "Debe seleccionar opciones válidas en los desplegables");
      return new ModelAndView("redirect:/mis-reportes?idModal=" + idFallo, modelo);
    }

    if (datosReporteMascotaDTO.getNuevasImagenes() != null) {
      datosReporteMascotaDTO.setImagenes(datosReporteMascotaDTO.getNuevasImagenes());
    }

    try {
      servicioReporteMascota.validarQueFechaDeReporteNoSeaFutura(datosReporteMascotaDTO);

      if (datosReporteMascotaDTO.getImagenes() != null && !datosReporteMascotaDTO.getImagenes().isEmpty() && !datosReporteMascotaDTO.getImagenes().get(0).isEmpty()) {
        servicioReporteMascota.validarQueLaImagenCumplaConFormato(datosReporteMascotaDTO);
        servicioReporteMascota.validarQueLaImagenNoExcedaTamano(datosReporteMascotaDTO);
      }
    } catch (FechaInvalidaException exception) {
      modelo.addAttribute("error", exception.getMessage());
      return new ModelAndView("redirect:/mis-reportes?idModal=" + idFallo, modelo);
    } catch (FormatoImagenInvalidaException exception) {
      modelo.addAttribute("error", exception.getMessage());
      return new ModelAndView("redirect:/mis-reportes?idModal=" + idFallo, modelo);
    } catch (ImagenExcedeTamanoException exception) {
      modelo.addAttribute("error", "Una de las fotos nuevas es demasiado pesada. (Máximo 20MB)");
      return new ModelAndView("redirect:/mis-reportes?idModal=" + idFallo, modelo);
    }

    try {
      servicioReporteMascota.actualizarReporte(datosReporteMascotaDTO);
    } catch (CantidadFotosExcedidaException e) {
      modelo.addAttribute("error", e.getMessage());
      return new ModelAndView("redirect:/mis-reportes?idModal=" + idFallo, modelo);
    } catch (Exception e) {
      modelo.addAttribute("error", "Ocurrió un error inesperado al actualizar el reporte.");
      return new ModelAndView("redirect:/mis-reportes?idModal=" + idFallo, modelo);
    }

    modelo.addAttribute("exito", "El reporte se actualizó correctamente.");
    return new ModelAndView("redirect:/mis-reportes", modelo);
  }
}