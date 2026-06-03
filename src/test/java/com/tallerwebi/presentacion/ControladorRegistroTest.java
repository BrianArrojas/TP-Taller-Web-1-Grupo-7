package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.excepcion.PasswordInvalidaException;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.repository.ServicioLogin;
import com.tallerwebi.presentacion.controller.ControladorRegistro;
import com.tallerwebi.presentacion.dto.DatosRegistroDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class ControladorRegistroTest {

    private ControladorRegistro controladorRegistro;
    private DatosRegistroDTO  datosRegistroDTO;
    private ServicioLogin servicioLoginMock;

    @BeforeEach
    public void init() {
        datosRegistroDTO = new DatosRegistroDTO();
        servicioLoginMock = mock(ServicioLogin.class);
        controladorRegistro = new ControladorRegistro(servicioLoginMock);
    }


    @Test
    public void elRegistroEsExistosoCuandoIngresoLosDatosCorrectamente ()throws UsuarioExistente {
        // preparacion
        DatosRegistroDTO datosRegistroDTO = new DatosRegistroDTO();
        datosRegistroDTO.setNombre("Brian");
        datosRegistroDTO.setApellido("Arrojas");
        datosRegistroDTO.setPassword("123456");
        datosRegistroDTO.setConfirmarPassword("123456");
        datosRegistroDTO.setTelefono("123456789");
        datosRegistroDTO.setMail("brian@gmail.com.ar");
        // ejecucion
        ModelAndView modelAndView = controladorRegistro.registrar(datosRegistroDTO);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
        assertThat(
                modelAndView.getModel().get("mensaje").toString(),
                equalToIgnoringCase("El registro fue exitoso")
        );
    }


  @Test
  public void registrarmeSiUsuarioExisteDeberiaVolverAFormularioYMostrarError()
    throws UsuarioExistente {
    // preparacion
      DatosRegistroDTO datosRegistroDTO = new DatosRegistroDTO();
      datosRegistroDTO.setNombre("Brian");
      datosRegistroDTO.setApellido("Arrojas");
      datosRegistroDTO.setPassword("123456");
      datosRegistroDTO.setConfirmarPassword("123456");
      datosRegistroDTO.setTelefono("123456789");
      datosRegistroDTO.setMail("brian@gmail.com.ar");
    doThrow(UsuarioExistente.class).when(servicioLoginMock).registrar(datosRegistroDTO);

    // ejecucion
    ModelAndView modelAndView = controladorRegistro.registrar(datosRegistroDTO);

    // validacion
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
    assertThat(
      modelAndView.getModel().get("mensaje").toString(),
      equalToIgnoringCase("El correo electrónico ya se encuentra registrado")
    );
  }


    @Test
    public void elRegistroFallaCuandoElEmailNoTieneFormatoValido() {
        // preparacion
        DatosRegistroDTO datosRegistroDTO = new DatosRegistroDTO();
        datosRegistroDTO.setNombre("Brian");
        datosRegistroDTO.setApellido("Arrojas");
        datosRegistroDTO.setPassword("123456");
        datosRegistroDTO.setConfirmarPassword("123456");
        datosRegistroDTO.setTelefono("123456789");
        datosRegistroDTO.setMail("brian@gmail.com");

        // ejecucion
        ModelAndView modelAndView = controladorRegistro.registrar(datosRegistroDTO);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
        assertThat(
                modelAndView.getModel().get("mensaje").toString(),
                equalToIgnoringCase("El email debe tener el formato correcto, Ej : usuario@gmail.com.ar")
        );

    }

  @Test
  public void errorEnRegistrarmeCuandoLaPassworTieneMenosDe6Caracteres()throws UsuarioExistente {

      // preparacion
      DatosRegistroDTO datosRegistroDTO = new DatosRegistroDTO();
      datosRegistroDTO.setNombre("Brian");
      datosRegistroDTO.setApellido("Arrojas");
      datosRegistroDTO.setPassword("1234");
      datosRegistroDTO.setConfirmarPassword("1234");
      datosRegistroDTO.setTelefono("123456789");
      datosRegistroDTO.setMail("brian@gmail.com.ar");


      doThrow(PasswordInvalidaException.class).when(servicioLoginMock).registrar(datosRegistroDTO);

    // ejecucion
    ModelAndView modelAndView = controladorRegistro.registrar(datosRegistroDTO);

    // validacion
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
    assertThat(
      modelAndView.getModel().get("mensaje").toString(),
      equalToIgnoringCase("Las contraseñas debe tener al menos 6 caracteres")
    );
  }



    @Test
    public void elRegistroFallaCuandoLaRepeticionPasswordNoCoincide() {
        // preparacion
        DatosRegistroDTO datosRegistroDTO = new DatosRegistroDTO();
        datosRegistroDTO.setNombre("Brian");
        datosRegistroDTO.setApellido("Arrojas");
        datosRegistroDTO.setPassword("123456");
        datosRegistroDTO.setConfirmarPassword("654321");
        datosRegistroDTO.setTelefono("123456789");
        datosRegistroDTO.setMail("brian@gmail.com.ar");


        doThrow(PasswordInvalidaException.class).when(servicioLoginMock).registrar(datosRegistroDTO);

        // ejecucion
        ModelAndView modelAndView = controladorRegistro.registrar(datosRegistroDTO);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
        assertThat(
                modelAndView.getModel().get("mensaje").toString(),
                equalToIgnoringCase("Las contraseñas no coinciden")
        );
    }

}
