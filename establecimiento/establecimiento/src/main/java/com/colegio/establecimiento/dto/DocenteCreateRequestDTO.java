package com.colegio.establecimiento.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DocenteCreateRequestDTO {

    private String rut;
    private String dv;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private LocalDate fechaNacimiento;
    private String correoElectronico;
    private String telefono;
    private String calle;
    private String numero;
    private Integer idComuna;
    private Integer idCategoriaSned;
    private Integer anioEvaluacionSned;
    private LocalDate fechaContratacion;
    private String estado;
}
