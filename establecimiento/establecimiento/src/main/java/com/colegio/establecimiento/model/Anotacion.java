package com.colegio.establecimiento.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "anotacion")
public class Anotacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_anotacion")
    private Integer idAnotacion;

    @Column(name = "id_estudiante", nullable = false)
    private Integer idEstudiante;

    @Column(name = "id_docente", nullable = false)
    private Integer idDocente;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "requiere_citacion", nullable = false)
    private Boolean requiereCitacion;

    @Column(name = "fecha_citacion")
    private LocalDate fechaCitacion;

    @Column(name = "estado", nullable = false)
    private String estado;
}