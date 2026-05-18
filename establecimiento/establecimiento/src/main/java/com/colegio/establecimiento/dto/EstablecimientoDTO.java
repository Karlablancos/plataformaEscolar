package com.colegio.establecimiento.dto;

import lombok.Data;

@Data
public class EstablecimientoDTO {

    private Integer idEstablecimiento;
    private String rbd;
    private String nombre;
    private Integer idTipoEstab;
    private String sostenedor;
    private String director;
    private String calle;
    private String numero;
    private Integer idComuna;
    private String telefono;
    private String correoElectronico;
    private String modoAula;
    private String estado;
}