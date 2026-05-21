package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.DatosDetalleMascotaDTO;

public interface DetalleMascotaService {
  DatosDetalleMascotaDTO obtenerDetalle(Long id);
}
