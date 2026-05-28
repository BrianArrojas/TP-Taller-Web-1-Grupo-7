package com.tallerwebi.dominio.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;


@Data
@NoArgsConstructor
@Entity
public class Foto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String img;

    @ManyToOne
    @JoinColumn(name = "reporte_mascota_id")
    private ReporteMascota reporteMascota;
}
