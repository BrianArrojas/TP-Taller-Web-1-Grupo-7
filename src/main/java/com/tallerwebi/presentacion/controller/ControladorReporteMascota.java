package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.excepcion.FechaInvalidaException;
import com.tallerwebi.dominio.excepcion.FormatoImagenInvalidaException;
import com.tallerwebi.dominio.ServicioReporteMascota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorReporteMascota {

  private final ServicioReporteMascota servicioReporteMascota;
  @Autowired
  public ControladorReporteMascota(ServicioReporteMascota servicioReporteMascota) {
    this.servicioReporteMascota = servicioReporteMascota;
  }

  @RequestMapping("/realizar-reporte")
  public ModelAndView mostrarFormularioReporteMascota() {
    ModelMap modelo = new ModelMap();
    modelo.put("datosReporte", new DatosReporteMascotaDTO());
    return new ModelAndView("realizar-reporte", modelo);
  }

  @RequestMapping(path = "/procesando-reporte", method = RequestMethod.POST)
  public ModelAndView realizarReporte(
    @ModelAttribute("datosReporte") DatosReporteMascotaDTO datosReporteMascotaDTO
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

    if (datosReporteMascotaDTO.getImagen() == null || datosReporteMascotaDTO.getImagen().isEmpty()) {
      modelo.put("mensaje","Debe adjuntar una imagen");
      modelo.put("datosReporte", datosReporteMascotaDTO);
      return new ModelAndView("realizar-reporte", modelo);
    }

    try{
      servicioReporteMascota.validarQueLaImagenCumplaConFormato(datosReporteMascotaDTO);
    }catch(FormatoImagenInvalidaException exception){
      modelo.put("mensaje", "El formato de la foto debe ser JPG o PNG");
      modelo.put("datosReporte", datosReporteMascotaDTO);
      return new ModelAndView("realizar-reporte", modelo);
    }

    try{
      servicioReporteMascota.validarQueFechaDeReporteNoSeaFutura(datosReporteMascotaDTO);
    }catch(FechaInvalidaException exception){
      modelo.put("mensaje", "La fecha ingresada no puede ser futura al dia de hoy");
      modelo.put("datosReporte", datosReporteMascotaDTO);
      return new ModelAndView("realizar-reporte", modelo);
    }

    modelo.put("tipoDeReporte", datosReporteMascotaDTO.getTipoDeReporte());
    modelo.put("especie", datosReporteMascotaDTO.getEspecie());
    modelo.put("tamano", datosReporteMascotaDTO.getTamano());
    modelo.put("raza", datosReporteMascotaDTO.getRaza());
    modelo.put("nombre", datosReporteMascotaDTO.getNombre());
    modelo.put("color", datosReporteMascotaDTO.getColor());
    modelo.put("descripcion", datosReporteMascotaDTO.getDescripcion());
    modelo.put("ubicacion", datosReporteMascotaDTO.getUbicacion());
    modelo.put("fecha",datosReporteMascotaDTO.getFecha());
    modelo.put("imagen", datosReporteMascotaDTO.getImagen());
    modelo.put("mensaje", "El reporte se realizo correctamente");
    modelo.put("datosReporte", new DatosReporteMascotaDTO());
    return new ModelAndView("lista-de-reportes", modelo);
  }
}
