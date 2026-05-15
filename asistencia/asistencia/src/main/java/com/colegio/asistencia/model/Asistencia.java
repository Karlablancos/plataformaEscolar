package com.colegio.asistencia.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "asistencia")
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asistencia")
    private Integer idAsistencia;

    @Column(name = "id_estudiante", nullable = false)
    private Integer idEstudiante;

    @Column(name = "id_horario", nullable = false)
    private Integer idHorario;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "estado_asistencia", nullable = false)
    private String estadoAsistencia;

    @Column(name = "hora_llegada")
    private LocalTime horaLlegada;

    @Column(name = "justificada", nullable = false)
    private Boolean justificada;

    @Column(name = "observacion")
    private String observacion;

    public Integer getIdAsistencia() { return idAsistencia; }
    public Integer getIdEstudiante() { return idEstudiante; }
    public Integer getIdHorario() { return idHorario; }
    public LocalDate getFecha() { return fecha; }
    public String getEstadoAsistencia() { return estadoAsistencia; }
    public LocalTime getHoraLlegada() { return horaLlegada; }
    public Boolean getJustificada() { return justificada; }
    public String getObservacion() { return observacion; }
    public void setIdEstudiante(Integer v) { this.idEstudiante = v; }
    public void setIdHorario(Integer v) { this.idHorario = v; }
    public void setFecha(LocalDate v) { this.fecha = v; }
    public void setEstadoAsistencia(String v) { this.estadoAsistencia = v; }
    public void setHoraLlegada(LocalTime v) { this.horaLlegada = v; }
    public void setJustificada(Boolean v) { this.justificada = v; }
    public void setObservacion(String v) { this.observacion = v; }
}