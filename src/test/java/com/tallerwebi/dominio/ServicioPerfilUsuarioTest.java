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
import static org.mockito.Mockito.*;

public class ServicioPerfilUsuarioTest {

    RepositorioUsuario repositorioUsuario = mock(RepositorioUsuario.class);
    ServicioPerfilUsuario servicio = new ServicioPerfilUsuarioImpl(repositorioUsuario);


    @Test
    public void siLosDatosSonCorrectosDebeActualizarLosDatosDelUsuarioConExito() {
        // given
        Usuario usuarioValido = new Usuario();
        usuarioValido.setNombre("Brian");
        usuarioValido.setApellido("Arrojas");
        usuarioValido.setEmail("brian@gmail.com.ar");
        usuarioValido.setTelefono("1234567890");
        usuarioValido.setPassword("123456");

        when(repositorioUsuario.buscar("brian@test.com.ar")).thenReturn(usuarioValido);

        Usuario usuarioDelFront = new Usuario();
        usuarioDelFront.setEmail("brian@test.com.ar");// Obligatorio para identificarlo
        usuarioDelFront.setNombre("Arian");

        servicio.actualizarPerfil(usuarioDelFront);

        verify(repositorioUsuario, times(1)).modificar(usuarioValido);

        assertThat(usuarioValido.getNombre(), equalTo("Arian"));
        assertThat(usuarioValido.getApellido(), equalTo("Arrojas"));
    }
    @Test
    public void siElTelefonoNoTieneDiezCaracteresDebeLanzarExcepcion() {
        // given
        Usuario usuarioOriginal = new Usuario();
        usuarioOriginal.setEmail("brian@test.com.ar");
        when(repositorioUsuario.buscar("brian@test.com.ar")).thenReturn(usuarioOriginal);

        Usuario usuarioInvalido = new Usuario();
        usuarioInvalido.setEmail("brian@test.com.ar");
        usuarioInvalido.setTelefono("123"); // Menos de 10 caracteres

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
        Usuario usuarioOriginal = new Usuario();
        usuarioOriginal.setEmail("test@gmail.com");

        when(repositorioUsuario.buscar("test@gmail.com")).thenReturn(usuarioOriginal);

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
        Usuario usuarioOriginal = new Usuario();
        usuarioOriginal.setEmail("test@unlam.com.ar");

        when(repositorioUsuario.buscar("test@unlam.com.ar")).thenReturn(usuarioOriginal);

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
        Usuario usuarioOriginal = new Usuario();
        usuarioOriginal.setEmail("test@unlam.com.ar");
        when(repositorioUsuario.buscar("test@unlam.com.ar")).thenReturn(usuarioOriginal);

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
