package com.colegio.establecimiento.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EstudianteCreateRequestDTO {

    private String rut;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private LocalDate fechaNacimiento;
    private String correoElectronico;
    private String telefono;
    private String calle;
    private String numero;
    private Integer idComuna;
    private String colegioProcedente;
    private LocalDate fechaMatricula;
    private Boolean prioritario;
    private Boolean preferente;
    private Boolean tieneNee;
    private Integer idTipoNee;
    private Boolean enPie;
    private String estado;
    private Integer idCurso;
}
