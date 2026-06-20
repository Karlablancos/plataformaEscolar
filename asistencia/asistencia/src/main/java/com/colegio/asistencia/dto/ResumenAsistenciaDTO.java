package com.colegio.asistencia.dto;

public class ResumenAsistenciaDTO {

    private Integer idEstudiante;
    private Integer totalClases;
    private Integer presentes;
    private Integer atrasos;
    private Integer ausenciasJustificadas;
    private Integer ausenciasInjustificadas;
    private Integer asistenciaValida;
    private Double porcentajeAsistencia;
    private Boolean cumple85;

    public Integer getIdEstudiante() { return idEstudiante; }
    public Integer getTotalClases() { return totalClases; }
    public Integer getPresentes() { return presentes; }
    public Integer getAtrasos() { return atrasos; }
    public Integer getAusenciasJustificadas() { return ausenciasJustificadas; }
    public Integer getAusenciasInjustificadas() { return ausenciasInjustificadas; }
    public Integer getAsistenciaValida() { return asistenciaValida; }
    public Double getPorcentajeAsistencia() { return porcentajeAsistencia; }
    public Boolean getCumple85() { return cumple85; }

    public void setIdEstudiante(Integer v) { this.idEstudiante = v; }
    public void setTotalClases(Integer v) { this.totalClases = v; }
    public void setPresentes(Integer v) { this.presentes = v; }
    public void setAtrasos(Integer v) { this.atrasos = v; }
    public void setAusenciasJustificadas(Integer v) { this.ausenciasJustificadas = v; }
    public void setAusenciasInjustificadas(Integer v) { this.ausenciasInjustificadas = v; }
    public void setAsistenciaValida(Integer v) { this.asistenciaValida = v; }
    public void setPorcentajeAsistencia(Double v) { this.porcentajeAsistencia = v; }
    public void setCumple85(Boolean v) { this.cumple85 = v; }
}