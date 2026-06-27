package com.colegio.establecimiento.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EstudianteDTO {

    private Integer idEstudiante;
    private Integer idEstablecimiento;
    private String rut;
    private String dv;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombreCompleto;
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
