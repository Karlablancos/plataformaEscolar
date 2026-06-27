package com.colegio.establecimiento.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PeriodoAcademicoDTO {

    private Integer idPeriodo;
    private Integer idEstablecimiento;
    private Integer anio;
    private String nombrePeriodo;
    private LocalDate fechaInicio;
    private LocalDate fechaTermino;
    private String estado;
}
