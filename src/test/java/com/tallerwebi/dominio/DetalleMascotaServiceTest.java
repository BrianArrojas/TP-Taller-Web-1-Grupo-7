package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.*;

import com.tallerwebi.dominio.service.DetalleMascotaService;
import com.tallerwebi.dominio.service.impl.DetalleMascotaServiceImpl;
import com.tallerwebi.presentacion.dto.DatosDetalleMascotaDTO;
import org.junit.jupiter.api.Test;

public class DetalleMascotaServiceTest {

  @Test
  public void obtenerDetalle_existente() {
    DetalleMascotaService service = new DetalleMascotaServiceImpl();
    DatosDetalleMascotaDTO dto = service.obtenerDetalle(1L);
    assertEquals("Firulais", dto.getNombreMascota());
    assertEquals("Dueño #1", dto.getNombreDuenio());
  }

  @Test
  public void obtenerDetalle_inexistente() {
    DetalleMascotaService service = new DetalleMascotaServiceImpl();
    assertThrows(RuntimeException.class, () -> service.obtenerDetalle(99L));
  }
}
