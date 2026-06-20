package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.tallerwebi.dominio.model.Comentario;
import com.tallerwebi.dominio.model.ReporteMascota;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.repository.RepositorioComentario;
import com.tallerwebi.dominio.repository.impl.RepositorioComentarioImpl;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateInfraestructuraTestConfig.class })
public class RepositorioComentarioTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioComentario repositorioComentario;

    @BeforeEach
    public void init() {
        repositorioComentario = new RepositorioComentarioImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void deberiaGuardarUnComentarioPublico() {
        Usuario usuario = dadoQueTengoUnUsuario("test@unlam.edu.ar", "test", "USER");
        sessionFactory.getCurrentSession().save(usuario);
        ReporteMascota reporte = dadoQueTengoUnReporte("Firulais", "Perro", usuario);
        sessionFactory.getCurrentSession().save(reporte);

        Comentario comentario = new Comentario();
        comentario.setReporteMascota(reporte);
        comentario.setNombreRemitente(usuario.getNombre());
        comentario.setTexto("Comentario público");

        repositorioComentario.guardar(comentario);

        Comentario guardado = (Comentario) sessionFactory.getCurrentSession()
                .createCriteria(Comentario.class)
                .add(org.hibernate.criterion.Restrictions.eq("id", comentario.getId()))
                .uniqueResult();

        assertThat(guardado, notNullValue());
        assertThat(guardado.getTexto(), equalTo("Comentario público"));
        assertThat(guardado.getCodigoChat(), nullValue());
    }

    @Test
    @Transactional
    @Rollback
    public void deberiaObtenerComentariosPublicosPorReporte() {
        Usuario usuario = dadoQueTengoUnUsuario("test@unlam.edu.ar", "test", "USER");
        sessionFactory.getCurrentSession().save(usuario);
        ReporteMascota reporte = dadoQueTengoUnReporte("Firulais", "Perro", usuario);
        sessionFactory.getCurrentSession().save(reporte);

        Comentario publico = new Comentario();
        publico.setReporteMascota(reporte);
        publico.setNombreRemitente("Autor");
        publico.setTexto("Público");
        sessionFactory.getCurrentSession().save(publico);

        Comentario privado = new Comentario();
        privado.setReporteMascota(reporte);
        privado.setNombreRemitente("Autor");
        privado.setTexto("Privado");
        privado.setCodigoChat(UUID.randomUUID().toString());
        sessionFactory.getCurrentSession().save(privado);

        List<Comentario> publicos = repositorioComentario.obtenerTodosComentariosDelReporte(reporte.getId());

        assertThat(publicos, hasSize(1));
        assertThat(publicos.get(0).getTexto(), equalTo("Público"));
    }

    @Test
    @Transactional
    @Rollback
    public void deberiaObtenerMensajesPorCodigoChat() {
        Usuario usuario = dadoQueTengoUnUsuario("test@unlam.edu.ar", "test", "USER");
        sessionFactory.getCurrentSession().save(usuario);
        ReporteMascota reporte = dadoQueTengoUnReporte("Firulais", "Perro", usuario);
        sessionFactory.getCurrentSession().save(reporte);

        String codigoChat = UUID.randomUUID().toString();
        Comentario m1 = new Comentario();
        m1.setReporteMascota(reporte);
        m1.setNombreRemitente("Juan");
        m1.setTexto("Hola");
        m1.setCodigoChat(codigoChat);
        sessionFactory.getCurrentSession().save(m1);

        Comentario m2 = new Comentario();
        m2.setReporteMascota(reporte);
        m2.setNombreRemitente("María");
        m2.setTexto("Respuesta");
        m2.setCodigoChat(codigoChat);
        sessionFactory.getCurrentSession().save(m2);

        List<Comentario> mensajes = repositorioComentario.obtenerMensajesPorCodigoChat(codigoChat);

        assertThat(mensajes, hasSize(2));
        assertThat(mensajes.get(0).getTexto(), equalTo("Hola"));
        assertThat(mensajes.get(1).getTexto(), equalTo("Respuesta"));
    }

    @Test
    @Transactional
    @Rollback
    public void deberiaBuscarChatDelInteresadoExistente() {
        Usuario usuario = dadoQueTengoUnUsuario("test@unlam.edu.ar", "test", "USER");
        sessionFactory.getCurrentSession().save(usuario);
        ReporteMascota reporte = dadoQueTengoUnReporte("Firulais", "Perro", usuario);
        sessionFactory.getCurrentSession().save(reporte);

        Long interesadoId = 20L;
        Comentario comentario = new Comentario();
        comentario.setReporteMascota(reporte);
        comentario.setNombreRemitente("Interesado");
        comentario.setTexto("Chat iniciado");
        comentario.setCodigoChat(UUID.randomUUID().toString());
        comentario.setIdInteresado(interesadoId);
        sessionFactory.getCurrentSession().save(comentario);

        Comentario encontrado = repositorioComentario.buscarChatDelInteresado(reporte.getId(), interesadoId);

        assertThat(encontrado, notNullValue());
        assertThat(encontrado.getCodigoChat(), notNullValue());
    }

    @Test
    @Transactional
    @Rollback
    public void deberiaObtenerTodosLosMensajesDeChatPorReporte() {
        Usuario usuario = dadoQueTengoUnUsuario("test@unlam.edu.ar", "test", "USER");
        sessionFactory.getCurrentSession().save(usuario);
        ReporteMascota reporte = dadoQueTengoUnReporte("Firulais", "Perro", usuario);
        sessionFactory.getCurrentSession().save(reporte);

        Comentario privado = new Comentario();
        privado.setReporteMascota(reporte);
        privado.setNombreRemitente("Autor");
        privado.setTexto("Mensaje de chat");
        privado.setCodigoChat(UUID.randomUUID().toString());
        sessionFactory.getCurrentSession().save(privado);

        Comentario publico = new Comentario();
        publico.setReporteMascota(reporte);
        publico.setNombreRemitente("Autor");
        publico.setTexto("Público");
        sessionFactory.getCurrentSession().save(publico);

        List<Comentario> mensajes = repositorioComentario.obtenerTodosMensajesDelReporte(reporte.getId());

        assertThat(mensajes, hasSize(1));
        assertThat(mensajes.get(0).getTexto(), equalTo("Mensaje de chat"));
        assertThat(mensajes.get(0).getCodigoChat(), notNullValue());
    }

    private Usuario dadoQueTengoUnUsuario(String email, String password, String rol) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setRol(rol);
        return usuario;
    }

    private ReporteMascota dadoQueTengoUnReporte(String nombre, String especie, Usuario autor) {
        ReporteMascota reporte = new ReporteMascota();
        reporte.setNombre(nombre);
        reporte.setEspecie(especie);
        reporte.setFecha(LocalDate.now());
        reporte.setUsuario(autor);
        return reporte;
    }
}