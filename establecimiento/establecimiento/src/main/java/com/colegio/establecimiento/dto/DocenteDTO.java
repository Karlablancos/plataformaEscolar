package com.colegio.establecimiento.dto;

import lombok.Data;

@Data
public class DocenteDTO {

    private Integer idDocente;
    private Integer idEstablecimiento;
    private String rut;
    private String dv;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombreCompleto;
    private String correoElectronico;
    private String telefono;
    private String estado;
}
