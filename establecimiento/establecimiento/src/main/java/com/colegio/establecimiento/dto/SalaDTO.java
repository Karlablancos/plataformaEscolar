package com.colegio.establecimiento.dto;

import lombok.Data;

@Data
public class SalaDTO {

    private Integer idSala;
    private Integer idEstablecimiento;
    private Integer numero;
    private String nombre;
    private Integer capacidad;
    private String tipo;
    private Integer piso;
    private String estado;
}
