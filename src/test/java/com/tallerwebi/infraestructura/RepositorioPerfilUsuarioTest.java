package com.tallerwebi.infraestructura;


import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.repository.RepositorioUsuario;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioPerfilUsuarioTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Test
    @Transactional
    @Rollback
    public void alModificarUnUsuarioLosCambiosDebenReflejarseEnElMismoRegistro() {
        // given
        Usuario usuarioOriginal = new Usuario();
        usuarioOriginal.setNombre("Brian");
        usuarioOriginal.setApellido("Arrojas");
        usuarioOriginal.setTelefono("2211334455");
        usuarioOriginal.setEmail("test@unlam.edu.ar");
        usuarioOriginal.setPassword("test");

        sessionFactory.getCurrentSession().save(usuarioOriginal);

        Long idOriginal = usuarioOriginal.getId();

        sessionFactory.getCurrentSession().clear();

        Usuario usuarioDelFront = new Usuario();

        usuarioDelFront.setId(idOriginal);

        usuarioDelFront.setNombre("Arian");// le realizo varios cambios
        usuarioDelFront.setApellido("Arrojas");
        usuarioDelFront.setTelefono("1234567890");
        usuarioDelFront.setEmail("brian@gmail.com.ar");
        usuarioDelFront.setPassword("123456");

        // when
        repositorioUsuario.modificar(usuarioDelFront);

        // then
        Usuario usuarioModificado = sessionFactory.getCurrentSession().get(Usuario.class, idOriginal);

        assertThat(usuarioModificado.getId(), equalTo(idOriginal));
        assertThat(usuarioModificado.getNombre(), equalTo("Arian"));
    }

    @Test
    @Transactional
    @Rollback
    public void modificarUsuarioInexistenteNoDeberiaLanzarErrorPeroTampocoPersistir() {
        // given
        Usuario usuarioInexistente = new Usuario();
        usuarioInexistente.setId(999L); // un id que no existe
        usuarioInexistente.setNombre("Brian");
        usuarioInexistente.setApellido("Arrojas");
        usuarioInexistente.setTelefono("1234567890");
        usuarioInexistente.setEmail("brian@unlam.edu.ar");
        usuarioInexistente.setPassword("123456");

        // when
        repositorioUsuario.modificar(usuarioInexistente);

        // then
        Usuario usuarioBuscado = sessionFactory.getCurrentSession().get(Usuario.class, 999L);

        assertThat(usuarioBuscado, nullValue());
    }


}
