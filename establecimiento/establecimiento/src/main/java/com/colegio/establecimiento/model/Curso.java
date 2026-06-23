package com.colegio.establecimiento.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private Integer idCurso;

    @Column(name = "id_establecimiento", nullable = false)
    private Integer idEstablecimiento;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "letra", nullable = false, columnDefinition = "char(1)")
    private String letra;

    @Column(name = "tipo_ensenianza", nullable = false)
    private String tipoEnsenanza;

    @Column(name = "modalidad", nullable = false)
    private String modalidad;

    @Column(name = "anio_academico", nullable = false)
    private Integer anioAcademico;

    @Column(name = "es_nivel_simce", nullable = false)
    private Boolean esNivelSimce;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "id_docente_jefe")
    private Integer idDocenteJefe;
}
