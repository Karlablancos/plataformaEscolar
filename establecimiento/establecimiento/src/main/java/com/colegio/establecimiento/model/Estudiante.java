package com.colegio.establecimiento.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "estudiante")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudiante")
    private Integer idEstudiante;

    @Column(name = "id_establecimiento", nullable = false)
    private Integer idEstablecimiento;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "rut", nullable = false, columnDefinition = "char(8)")
    private String rut;

    @Column(name = "dv", nullable = false, columnDefinition = "char(1)")
    private String dv;

    @Column(name = "nombres", nullable = false)
    private String nombres;

    @Column(name = "apellido_paterno", nullable = false)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", nullable = false)
    private String apellidoMaterno;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "correo_electronico", nullable = false)
    private String correoElectronico;

    @Column(name = "telefono", nullable = false)
    private String telefono;

    @Column(name = "calle", nullable = false)
    private String calle;

    @Column(name = "numero", nullable = false)
    private String numero;

    @Column(name = "id_comuna", nullable = false)
    private Integer idComuna;

    @Column(name = "colegio_procedente", nullable = false)
    private String colegioProcedente;

    @Column(name = "fecha_matricula", nullable = false)
    private LocalDate fechaMatricula;

    @Column(name = "prioritario", nullable = false)
    private Boolean prioritario;

    @Column(name = "preferente", nullable = false)
    private Boolean preferente;

    @Column(name = "tiene_nee", nullable = false)
    private Boolean tieneNee;

    @Column(name = "id_tipo_nee")
    private Integer idTipoNee;

    @Column(name = "en_pie", nullable = false)
    private Boolean enPie;

    @Column(name = "estado", nullable = false)
    private String estado;
}
