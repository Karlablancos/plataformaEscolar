package com.colegio.establecimiento.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "curso_asignatura")
public class CursoAsignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso_asignatura")
    private Integer idCursoAsignatura;

    @Column(name = "id_curso", nullable = false)
    private Integer idCurso;

    @Column(name = "id_asignatura", nullable = false)
    private Integer idAsignatura;

    @Column(name = "id_docente", nullable = false)
    private Integer idDocente;

    @Column(name = "horas_semanales", nullable = false)
    private Integer horasSemanales;

    @Column(name = "estado", nullable = false)
    private String estado;
}
