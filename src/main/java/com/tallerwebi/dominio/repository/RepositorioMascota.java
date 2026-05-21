package com.tallerwebi.dominio.repository;

import com.tallerwebi.presentacion.dto.Mascota;

import java.util.List;

public interface RepositorioMascota {
  List<Mascota> obtenerTodasLasMascotas();
}
