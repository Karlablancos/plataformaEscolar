package com.colegio.establecimiento.dto;

import lombok.Data;

@Data
public class CursoAsignaturaDTO {

    private Integer idCursoAsignatura;
    private Integer idCurso;
    private Integer idAsignatura;
    private Integer idDocente;
    private String asignaturaNombre;
    private String docenteNombre;
    private Integer horasSemanales;
    private String estado;
}
