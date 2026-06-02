package com.colegio.establecimiento.dto;

import lombok.Data;

@Data
public class AsignaturaDTO {

    private Integer idAsignatura;
    private Integer idEstablecimiento;
    private String nombre;
    private String codigo;
    private Integer idTipoCalificacion;
    private String estado;
}
