package com.tallerwebi.presentacion.controller;

import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.service.ServicioReporteMascota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ControladorHome {

  private final ServicioReporteMascota servicioReporteMascota;

  @Autowired
  public ControladorHome(ServicioReporteMascota servicioReporteMascota) {
    this.servicioReporteMascota = servicioReporteMascota;
  }

  @RequestMapping(path = "/home", method = RequestMethod.GET)
  public ModelAndView irAHome(
      @RequestParam(value = "busqueda", required = false) String busqueda,
      @RequestParam(value = "tipoDeReporte", required = false) String tipoDeReporte,
      @RequestParam(value = "especie", required = false) String especie,
      @RequestParam(value = "fechaDesde", required = false) String fechaDesde,
      @RequestParam(value = "fechaHasta", required = false) String fechaHasta,
      @RequestParam(value = "page", defaultValue = "1") int page
  ) {
    if (page < 1) page = 1;
    int pageSize = 16;
    
    int totalReports = servicioReporteMascota.contarReportesFiltrados(
        busqueda, tipoDeReporte, especie, fechaDesde, fechaHasta);
    
    int totalPages = (int) Math.ceil((double) totalReports / pageSize);
    if (totalPages == 0) totalPages = 1;
    if (page > totalPages) page = totalPages;
    
    List<ReporteMascota> paginated = servicioReporteMascota.buscarReportesFiltradosYPaginados(
        busqueda, tipoDeReporte, especie, fechaDesde, fechaHasta, page, pageSize);
    
    ModelMap modelo = new ModelMap();
    modelo.put("mascotas", paginated);
    modelo.put("currentPage", page);
    modelo.put("totalPages", totalPages);
    modelo.put("busqueda", busqueda);
    modelo.put("tipoDeReporte", tipoDeReporte);
    modelo.put("especie", especie);
    modelo.put("fechaDesde", fechaDesde);
    modelo.put("fechaHasta", fechaHasta);
    
    return new ModelAndView("home", modelo);
  }
}
