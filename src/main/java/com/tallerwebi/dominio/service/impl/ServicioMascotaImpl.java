package com.tallerwebi.dominio.service.impl;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.tallerwebi.dominio.repository.RepositorioMascota;
import com.tallerwebi.dominio.service.ServicioMascota;
import com.tallerwebi.presentacion.dto.Mascota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioMascota")
@Transactional
public class ServicioMascotaImpl implements ServicioMascota {

  private final RepositorioMascota repositorioMascota;

  @Autowired
  public ServicioMascotaImpl(RepositorioMascota repositorioMascota) {
    this.repositorioMascota = repositorioMascota;
  }

  @Override
  @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
  public List<Mascota> listarMascotas(String busqueda) {
    List<Mascota> mascotas = repositorioMascota.obtenerTodasLasMascotas();
    if (busqueda != null && !busqueda.trim().isEmpty()) {
      String busquedaLower = busqueda.toLowerCase(Locale.ROOT);
      return mascotas
        .stream()
        .filter(mascota ->
          (mascota.getTipo() != null &&
            mascota.getTipo().toLowerCase(Locale.ROOT).contains(busquedaLower)) ||
          (mascota.getNombre() != null &&
            mascota.getNombre().toLowerCase(Locale.ROOT).contains(busquedaLower))
        )
        .collect(Collectors.toList());
    }
    return mascotas;
  }
}
