package com.tallerwebi.dominio.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "reporte_mascota")
public class ReporteMascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_de_reporte")
    private String tipoDeReporte;
    private String nombre;
    private String especie;
    private String raza;
    private String color;
    private String tamano;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fecha; // Cuando se perdio/encontro
    private String ubicacion;
    private String descripcion;
    @Column(name = "fecha_creacion_reporte")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaCreacionReporte;
    @Column(name = "registro_activo")
    private Boolean registroActivo;
    @JsonProperty("latitud")
    private Double latitud;
    @JsonProperty("longitud")
    private Double longitud;

    @JsonIgnoreProperties("reporteMascota")
    @OneToMany(mappedBy = "reporteMascota", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
    private List<Foto> fotos ;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


    public ReporteMascota() {
        this.fechaCreacionReporte = LocalDateTime.now();
        this.registroActivo = true;
        this.fotos = new ArrayList<>();
    }

    public String getFechaFormateada() {
        return (this.fecha != null) ? this.fecha.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }
    public String getPrimeraRutaImagen() {
        if (this.fotos == null || this.fotos.isEmpty()) {
            return "/img/default-pet.png";
        }
        String imgPath = this.fotos.get(0).getImg();
        if (imgPath.startsWith("img/")) {
            return "/" + imgPath;
        } else if (imgPath.startsWith("/img/")) {
            return imgPath;
        } else {
            return "/img/" + imgPath;
        }
    }
}
