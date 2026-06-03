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

        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre no puede estar vacío.");
        }

        if (usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
            throw new DatosInvalidosException("El apellido no puede estar vacío.");
        }

        if(usuario.getTelefono() == null || usuario.getTelefono().trim().isEmpty() || usuario.getTelefono().length() != 10 ) {
            throw new DatosInvalidosException("El telefono debe tener 10 caracteres.");
        }

        String email = usuario.getEmail();
        if (email == null || !email.contains("@") || !email.endsWith(".com.ar")) {
            throw new DatosInvalidosException("El correo electrónico debe contener un '@' y terminar con '.com.ar'.");
        }

        if (usuario.getPassword() == null || usuario.getPassword().length() < 6) {
            throw new DatosInvalidosException("La contraseña debe tener 6 caracteres.");
        }

        repositorioUsuario.modificar(usuario);
    }


}
