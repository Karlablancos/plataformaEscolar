package com.colegio.establecimiento.dto;

import lombok.Data;

@Data
public class CursoDTO {

    private Integer idCurso;
    private Integer idEstablecimiento;
    private Integer numero;
    private String letra;
    private String tipoEnsenanza;
    private String modalidad;
    private Integer anioAcademico;
    private Boolean esNivelSimce;
    private String estado;
    private String nombre; // calculado: "1° Básica A"
}
