package com.tallerwebi.infraestructura;


import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.repository.RepositorioUsuario;
import com.tallerwebi.dominio.repository.impl.RepositorioUsuarioImpl;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.hibernate.Session;
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
import static org.mockito.Mockito.mock;

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
        Usuario usuario = new Usuario();
        usuario.setNombre("Brian");
        usuario.setApellido("Arrojas");
        usuario.setTelefono("1234567890");
        usuario.setEmail("test@unlam.com.ar");
        usuario.setPassword("123456");


        sessionFactory.getCurrentSession().save(usuario);

        Long idOriginal = usuario.getId();

        // when
        usuario.setNombre("Arian");
        repositorioUsuario.modificar(usuario);

        // then
        Usuario usuarioModificado = sessionFactory.getCurrentSession().get(Usuario.class, idOriginal);

        assertThat(usuarioModificado.getId(), equalTo(idOriginal));
        assertThat(usuarioModificado.getNombre(), equalTo("Arian"));
    }


}
