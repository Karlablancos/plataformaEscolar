package com.colegio.academico.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "evaluacion")
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluacion")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoEvaluacion tipo;

    @Column(name = "ponderacion", nullable = false, precision = 5, scale = 2)
    private BigDecimal ponderacion;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "id_curso", nullable = false)
    private Long idCurso;

    @Column(name = "id_asignatura", nullable = false)
    private Long idAsignatura;

    @Column(name = "id_docente", nullable = false)
    private Long idDocente;

    @Column(name = "id_establecimiento", nullable = false)
    private Long idEstablecimiento;
}
