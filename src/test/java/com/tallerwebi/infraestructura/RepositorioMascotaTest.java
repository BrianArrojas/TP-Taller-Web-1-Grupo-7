package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.repository.RepositorioReporteMascota;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import com.tallerwebi.presentacion.dto.DatosReporteMascotaDTO;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioMascotaTest {

    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    RepositorioReporteMascota repositorioMascota;

    @Test
    @Transactional
    @Rollback
    public void sePuedeGuardarUnNuevoReporteMascota(){

        //given
        Usuario usuario = new Usuario();
        usuario.setEmail("test@unlam.edu.ar");
        usuario.setPassword("test");
        sessionFactory.getCurrentSession().save(usuario);
        DatosReporteMascotaDTO datosReporteMascota = new DatosReporteMascotaDTO();
        datosReporteMascota.setNombre("Brian");
        datosReporteMascota.setRaza("Dogo");
        datosReporteMascota.setColor("Blanco");
        datosReporteMascota.setDescripcion("Esta Lastimado");
        datosReporteMascota.setUbicacion("San Justo");
        datosReporteMascota.setTipoDeReporte("Perdido");
        datosReporteMascota.setTamano("Grande");
        datosReporteMascota.setEspecie("Perro");
        datosReporteMascota.setFecha(LocalDate.now().minusDays(1));
        // Simulamos una foto real pasando: nombre del parámetro, nombre del archivo, tipo, y el contenido en bytes
        MockMultipartFile fotoSimulada = new MockMultipartFile("foto", "perrito.png", "image/png", "bytes-de-png".getBytes());
        datosReporteMascota.setImagen(fotoSimulada);
        //when

        repositorioMascota.guardarReporte(datosReporteMascota,usuario);


        ReporteMascota reporteGuardado = (ReporteMascota) sessionFactory.getCurrentSession()
                .createCriteria(ReporteMascota.class)
                .add(Restrictions.eq("usuario", usuario)) // Compara directamente el objeto mapeado
                .add(Restrictions.eq("nombre", "Brian"))
                .uniqueResult();

        //then
        assertThat(usuario.getId(),notNullValue());

        assertThat(reporteGuardado.getId(), notNullValue());
        assertThat(reporteGuardado.getRaza(), equalTo("Dogo"));
        assertThat(reporteGuardado.getColor(), equalTo("Blanco"));
        assertThat(reporteGuardado.getUbicacion(), equalTo("San Justo"));

    }


    @Test
    @Transactional
    @Rollback
    public void alBuscarReportesConUsuarioObtengoListaDeLosMismos(){

        //given
        Usuario usuario = new Usuario();
        usuario.setEmail("test@unlam.edu.ar");
        usuario.setPassword("test");
        sessionFactory.getCurrentSession().save(usuario);

        DatosReporteMascotaDTO datosReporteMascota = new DatosReporteMascotaDTO();
        datosReporteMascota.setNombre("Brian");
        datosReporteMascota.setRaza("Dogo");
        datosReporteMascota.setColor("Blanco");
        datosReporteMascota.setDescripcion("Esta Lastimado");
        datosReporteMascota.setUbicacion("San Justo");
        datosReporteMascota.setTipoDeReporte("Perdido");
        datosReporteMascota.setTamano("Grande");
        datosReporteMascota.setEspecie("Perro");
        datosReporteMascota.setFecha(LocalDate.now().minusDays(1));
        // Simulamos una foto real pasando: nombre del parámetro, nombre del archivo, tipo, y el contenido en bytes
        MockMultipartFile fotoSimulada = new MockMultipartFile("foto", "perrito.png", "image/png", "bytes-de-png".getBytes());
        datosReporteMascota.setImagen(fotoSimulada);
        repositorioMascota.guardarReporte(datosReporteMascota,usuario);

        Usuario usuario2 = new Usuario();
        usuario2.setEmail("brian@unlam.edu.ar");
        usuario2.setPassword("1234");
        sessionFactory.getCurrentSession().save(usuario2);

        DatosReporteMascotaDTO datosReporteMascota2 = new DatosReporteMascotaDTO();
        datosReporteMascota.setNombre("Arian");
        datosReporteMascota.setRaza("Labrador");
        datosReporteMascota.setColor("Negro");
        datosReporteMascota.setDescripcion("Esta sano");
        datosReporteMascota.setUbicacion("Moron");
        datosReporteMascota.setTipoDeReporte("Encontrado");
        datosReporteMascota.setTamano("Mediano");
        datosReporteMascota.setEspecie("Perro");
        datosReporteMascota.setFecha(LocalDate.now().minusDays(4));
        // Simulamos una foto real pasando: nombre del parámetro, nombre del archivo, tipo, y el contenido en bytes
        MockMultipartFile fotoSimulada2 = new MockMultipartFile("foto", "perrito2.png", "image/png", "bytes-de-png".getBytes());
        datosReporteMascota.setImagen(fotoSimulada2);
        repositorioMascota.guardarReporte(datosReporteMascota2,usuario);


        //when
        List<ReporteMascota> reportesDelPrimerUsuario = repositorioMascota.buscarPorUsuario(usuario);


        //then
        assertThat(datosReporteMascota, notNullValue());
        assertThat(reportesDelPrimerUsuario.size(), equalTo(2));

    }


    @Test
    @Transactional
    @Rollback
    public void sePuedeBuscarUnReportePorIdExistente() {
        // given
        Usuario usuario = new Usuario();
        usuario.setEmail("test@unlam.edu.ar");
        usuario.setPassword("test");
        sessionFactory.getCurrentSession().save(usuario);

        DatosReporteMascotaDTO datos = new DatosReporteMascotaDTO();
        datos.setNombre("Firulais");
        datos.setRaza("Labrador");
        datos.setColor("Negro");
        datos.setDescripcion("Lleva collar rojo");
        datos.setUbicacion("Parque Central");
        datos.setTipoDeReporte("Perdida");
        datos.setTamano("Grande");
        datos.setEspecie("Perro");
        datos.setFecha(LocalDate.now().minusDays(1));
        MockMultipartFile fotoSimulada = new MockMultipartFile("foto", "firulais.jpg", "image/jpeg", "bytes".getBytes());
        datos.setImagen(fotoSimulada);

        repositorioMascota.guardarReporte(datos, usuario);

        ReporteMascota reporteGuardado = (ReporteMascota) sessionFactory.getCurrentSession()
                .createCriteria(ReporteMascota.class)
                .add(Restrictions.eq("nombre", "Firulais"))
                .uniqueResult();

        // when
        ReporteMascota resultado = repositorioMascota.buscarPorId(reporteGuardado.getId());

        // then
        assertThat(resultado, notNullValue());
        assertThat(resultado.getNombre(), equalTo("Firulais"));
        assertThat(resultado.getRaza(), equalTo("Labrador"));
        assertThat(resultado.getUsuario().getEmail(), equalTo("test@unlam.edu.ar"));
    }

    @Test
    @Transactional
    @Rollback
    public void buscarPorIdConIdInexistenteDevuelveNull() {
        // when
        ReporteMascota resultado = repositorioMascota.buscarPorId(999L);

        // then
        assertThat(resultado, equalTo(null));
    }
}

