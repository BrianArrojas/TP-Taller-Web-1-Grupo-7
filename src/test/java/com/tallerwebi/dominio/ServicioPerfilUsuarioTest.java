package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.DatosInvalidosException;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.repository.RepositorioUsuario;
import com.tallerwebi.dominio.service.ServicioPerfilUsuario;
import com.tallerwebi.dominio.service.impl.ServicioPerfilUsuarioImpl;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class ServicioPerfilUsuarioTest {

    RepositorioUsuario repositorioUsuario = mock(RepositorioUsuario.class);
    ServicioPerfilUsuario servicio = new ServicioPerfilUsuarioImpl(repositorioUsuario);


    @Test
    public void siLosDatosSonCorrectosDebeActualizarSinLanzarExcepcion() {
        // given
        Usuario usuarioValido = new Usuario();
        usuarioValido.setNombre("Brian");
        usuarioValido.setApellido("Arrojas");
        usuarioValido.setEmail("brian@gmail.com.ar");
        usuarioValido.setTelefono("1234567890");
        usuarioValido.setPassword("123456");

        servicio.actualizarPerfil(usuarioValido);

        assertThat(usuarioValido.getNombre(), equalTo("Brian"));
    }
    @Test
    public void siElTelefonoNoTieneDiezCaracteresDebeLanzarExcepcion() {
        // given
        RepositorioUsuario repositorioMock = mock(RepositorioUsuario.class);
        ServicioPerfilUsuario servicio = new ServicioPerfilUsuarioImpl(repositorioMock);

        Usuario usuarioInvalido = new Usuario();
        usuarioInvalido.setNombre("Brian");
        usuarioInvalido.setApellido("Arrojas");
        usuarioInvalido.setTelefono("12345");
        usuarioInvalido.setEmail("test@unlam.com.ar");
        usuarioInvalido.setPassword("1234567");

        // when
        DatosInvalidosException excepcion = assertThrows(DatosInvalidosException.class, () -> {
            servicio.actualizarPerfil(usuarioInvalido);
        });
        //then
        assertThat(excepcion.getMessage(), equalTo("El telefono debe tener 10 caracteres."));
    }

    @Test
    public void siElEmailNoTerminaComoCorrespondeDebeLanzarExcepcion() {
        // given
        RepositorioUsuario repositorioMock = mock(RepositorioUsuario.class);
        ServicioPerfilUsuario servicio = new ServicioPerfilUsuarioImpl(repositorioMock);

        Usuario usuarioInvalido = new Usuario();
        usuarioInvalido.setNombre("Brian");
        usuarioInvalido.setApellido("Arrojas");
        usuarioInvalido.setTelefono("1234567890");
        usuarioInvalido.setEmail("test@gmail.com");
        usuarioInvalido.setPassword("1234567");

        // when
        DatosInvalidosException excepcion = assertThrows(DatosInvalidosException.class, () -> {
            servicio.actualizarPerfil(usuarioInvalido);
        });
        //then
        assertThat(excepcion.getMessage(), equalTo("El correo electrónico debe contener un '@' y terminar con '.com.ar'."));
    }


    @Test
    public void siElNombreEstaVacioDebeLanzarExcepcion() {
        // given
        RepositorioUsuario repositorioMock = mock(RepositorioUsuario.class);
        ServicioPerfilUsuario servicio = new ServicioPerfilUsuarioImpl(repositorioMock);

        Usuario usuarioInvalido = new Usuario();
        usuarioInvalido.setNombre("");
        usuarioInvalido.setApellido("Arrojas");
        usuarioInvalido.setTelefono("1234567890");
        usuarioInvalido.setEmail("test@unlam.com.ar");
        usuarioInvalido.setPassword("1234567");

        // when
        DatosInvalidosException excepcion = assertThrows(DatosInvalidosException.class, () -> {
            servicio.actualizarPerfil(usuarioInvalido);
        });
        //then
        assertThat(excepcion.getMessage(), equalTo("El nombre no puede estar vacío."));
    }

    @Test
    public void siLaPasswordTieneMenosDeSeisDebeLanzarExcepcion() {
        // given
        RepositorioUsuario repositorioMock = mock(RepositorioUsuario.class);
        ServicioPerfilUsuario servicio = new ServicioPerfilUsuarioImpl(repositorioMock);

        Usuario usuarioInvalido = new Usuario();
        usuarioInvalido.setNombre("Brian");
        usuarioInvalido.setApellido("Arrojas");
        usuarioInvalido.setTelefono("1234567890");
        usuarioInvalido.setEmail("test@unlam.com.ar");
        usuarioInvalido.setPassword("1234");

        // when
        DatosInvalidosException excepcion = assertThrows(DatosInvalidosException.class, () -> {
            servicio.actualizarPerfil(usuarioInvalido);
        });
        //then
        assertThat(excepcion.getMessage(), equalTo("La contraseña debe tener 6 caracteres."));
    }

}
