package com.tallerwebi.dominio.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ReporteMascota reporteMascota;

    private String nombreRemitente;

    private Long idInteresado;

    @Column(length = 1000)
    private String texto;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private String codigoChat;

    public String getFechaCreacionFormateada() {
        if (this.fechaCreacion == null) return "";
        return this.fechaCreacion.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}