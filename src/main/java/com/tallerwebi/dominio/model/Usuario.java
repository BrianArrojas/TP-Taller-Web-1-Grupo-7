package com.tallerwebi.dominio.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    private String nombre;
    private String apellido;
    private String telefono;
    private String rol;
    private Boolean activo = false;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReporteMascota> reportesMascotas = new ArrayList<>();

    public void activar() {
        activo = true;
    }
}
