package com.colegio.establecimiento.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
@Table(name = "horario")
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario")
    private Integer idHorario;

    @Column(name = "id_curso", nullable = false)
    private Integer idCurso;

    @Column(name = "id_asignatura", nullable = false)
    private Integer idAsignatura;

    @Column(name = "id_docente", nullable = false)
    private Integer idDocente;

    @Column(name = "id_sala", nullable = false)
    private Integer idSala;

    @Column(name = "id_periodo")
    private Integer idPeriodo;

    @Column(name = "dia_semana", nullable = false)
    private String diaSemana;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_termino", nullable = false)
    private LocalTime horaTermino;

    @Column(name = "anio_academico", nullable = false)
    private Integer anioAcademico;
}
