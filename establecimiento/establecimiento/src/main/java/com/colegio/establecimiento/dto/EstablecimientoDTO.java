package com.colegio.establecimiento.dto;

import lombok.Data;

@Data
public class EstablecimientoDTO {

    private Integer idEstablecimiento;
    private String rbd;
    private String nombre;
    private String sostenedor;
    private String director;
    private String correoElectronico;
    private String telefono;
    private String estado;

}