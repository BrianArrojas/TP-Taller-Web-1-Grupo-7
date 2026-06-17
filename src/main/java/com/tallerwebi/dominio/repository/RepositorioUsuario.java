package com.tallerwebi.dominio.repository;

import com.tallerwebi.dominio.model.Usuario;

public interface RepositorioUsuario {
  Usuario buscarUsuario(String email, String password);
  void guardar(Usuario usuario);
  Usuario buscar(String email);
  void modificar(Usuario usuario);
  java.util.List<Usuario> obtenerTodos();
  java.util.List<Usuario> buscarPorEmail(String email);
  Usuario buscarPorId(Long id);
}

