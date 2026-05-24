package com.tallerwebi.dominio.service;

import com.tallerwebi.presentacion.dto.Mascota;

import java.util.List;

public interface ServicioMascota {
  List<Mascota> listarMascotas(String busqueda);
}
