package com.colegio.asistencia.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AsistenciaDTO {

    private Integer idAsistencia;
    private Integer idEstudiante;
    private Integer idHorario;
    private LocalDate fecha;
    private String estadoAsistencia;
    private LocalTime horaLlegada;
    private Boolean justificada;
    private String observacion;

    public Integer getIdAsistencia() { return idAsistencia; }
    public Integer getIdEstudiante() { return idEstudiante; }
    public Integer getIdHorario() { return idHorario; }
    public LocalDate getFecha() { return fecha; }
    public String getEstadoAsistencia() { return estadoAsistencia; }
    public LocalTime getHoraLlegada() { return horaLlegada; }
    public Boolean getJustificada() { return justificada; }
    public String getObservacion() { return observacion; }
    public void setIdAsistencia(Integer v) { this.idAsistencia = v; }
    public void setIdEstudiante(Integer v) { this.idEstudiante = v; }
    public void setIdHorario(Integer v) { this.idHorario = v; }
    public void setFecha(LocalDate v) { this.fecha = v; }
    public void setEstadoAsistencia(String v) { this.estadoAsistencia = v; }
    public void setHoraLlegada(LocalTime v) { this.horaLlegada = v; }
    public void setJustificada(Boolean v) { this.justificada = v; }
    public void setObservacion(String v) { this.observacion = v; }
}