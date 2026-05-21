package com.tallerwebi.dominio.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerwebi.presentacion.dto.Mascota;
import com.tallerwebi.dominio.repository.RepositorioMascota;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

@Repository("repositorioMascota")
public class RepositorioMascotaImpl implements RepositorioMascota {

  private static final Logger LOGGER = LoggerFactory.getLogger(RepositorioMascotaImpl.class);
  private final ResourceLoader resourceLoader;
  private final ObjectMapper objectMapper;

  @Autowired
  public RepositorioMascotaImpl(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
    this.objectMapper = new ObjectMapper();
  }

  @Override
  public List<Mascota> obtenerTodasLasMascotas() {
    try {
      Resource resource = resourceLoader.getResource("classpath:mascotas.json");
      if (!resource.exists()) {
        LOGGER.warn("El archivo mascotas.json no se encontró en el classpath.");
        return Collections.emptyList();
      }
      try (InputStream inputStream = resource.getInputStream()) {
        return objectMapper.readValue(inputStream, new TypeReference<List<Mascota>>() {});
      }
    } catch (IOException e) {
      LOGGER.error("Error al leer el archivo JSON de mascotas", e);
      return Collections.emptyList(); // Retorna una lista vacía en caso de error
    }
  }
}
