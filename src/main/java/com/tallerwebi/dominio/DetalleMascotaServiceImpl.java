package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.DatosDetalleMascotaDTO;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class DetalleMascotaServiceImpl implements DetalleMascotaService {

  private final Map<Long, DatosDetalleMascotaDTO> reportesSimulados = new HashMap<>();

  public DetalleMascotaServiceImpl() {
    // Cargamos dos reportes de ejemplo para la demo
    reportesSimulados.put(
      1L,
      new DatosDetalleMascotaDTO(
        1L,
        "Firulais",
        "Perro",
        "Labrador",
        "/img/default-pet.png",
        "Se perdió en el parque central. Lleva collar rojo.",
        "Dueño #1"
      )
    );
    reportesSimulados.put(
      2L,
      new DatosDetalleMascotaDTO(
        2L,
        "Michi",
        "Gato",
        "Siamés",
        "/img/default-pet.png",
        "Encontrado cerca de la avenida. Es muy cariñoso.",
        "Dueño #2"
      )
    );
  }

  @Override
  public DatosDetalleMascotaDTO obtenerDetalle(Long id) {
    DatosDetalleMascotaDTO dto = reportesSimulados.get(id);
    if (dto == null) {
      throw new RuntimeException("No se encontró el reporte con id: " + id);
    }
    return dto;
  }
}
