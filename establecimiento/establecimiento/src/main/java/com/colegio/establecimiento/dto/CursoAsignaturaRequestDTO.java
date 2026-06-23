package com.colegio.establecimiento.dto;

import lombok.Data;

@Data
public class CursoAsignaturaRequestDTO {

    private Integer idAsignatura;
    private Integer idDocente;
    private Integer horasSemanales;
}
