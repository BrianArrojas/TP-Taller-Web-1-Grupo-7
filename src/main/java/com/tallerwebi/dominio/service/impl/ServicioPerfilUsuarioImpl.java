package com.tallerwebi.dominio.service.impl;

import com.tallerwebi.dominio.excepcion.DatosInvalidosException;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.repository.RepositorioUsuario;
import com.tallerwebi.dominio.service.ServicioPerfilUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ServicioPerfilUsuarioImpl implements ServicioPerfilUsuario {

    private RepositorioUsuario repositorioUsuario;
    @Autowired
    public ServicioPerfilUsuarioImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public void actualizarPerfil(Usuario usuario) {

        if (usuario.getEmail() == null || !usuario.getEmail().contains("@") || !usuario.getEmail().endsWith(".com.ar")) {
            throw new DatosInvalidosException("El correo electrónico debe contener un '@' y terminar con '.com.ar'.");
        }

        Usuario usuarioOriginal = repositorioUsuario.buscar(usuario.getEmail());

        if (usuarioOriginal == null) {
            throw new DatosInvalidosException("El usuario que se intenta modificar no existe.");
        }

        if (usuario.getNombre() != null) {
            if (usuario.getNombre().trim().isEmpty()) {
                throw new DatosInvalidosException("El nombre no puede estar vacío.");
            }
            usuarioOriginal.setNombre(usuario.getNombre());
        }

        if (usuario.getApellido() != null) {
            if (usuario.getApellido().trim().isEmpty()) {
                throw new DatosInvalidosException("El apellido no puede estar vacío.");
            }
            usuarioOriginal.setApellido(usuario.getApellido());
        }

        if (usuario.getTelefono() != null) {
            if (usuario.getTelefono().trim().isEmpty() || usuario.getTelefono().length() < 10) {
                throw new DatosInvalidosException("El telefono debe tener 10 caracteres.");
            }
            usuarioOriginal.setTelefono(usuario.getTelefono());
        }

        if (usuario.getPassword() != null) {
            if (usuario.getPassword().trim().isEmpty() || usuario.getPassword().length() < 6) {
                throw new DatosInvalidosException("La contraseña debe tener 6 caracteres.");
            }
            usuarioOriginal.setPassword(usuario.getPassword());
        }

        repositorioUsuario.modificar(usuarioOriginal);
    }


}
