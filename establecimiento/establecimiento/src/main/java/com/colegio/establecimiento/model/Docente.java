package com.colegio.establecimiento.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "docente")
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_docente")
    private Integer idDocente;

    @Column(name = "id_establecimiento", nullable = false)
    private Integer idEstablecimiento;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "id_categoria_sned")
    private Integer idCategoriaSned;

    @Column(name = "anio_evaluacion_sned", nullable = false)
    private Integer anioEvaluacionSned;

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

    @Column(name = "fecha_contratacion", nullable = false)
    private LocalDate fechaContratacion;

    @Column(name = "estado", nullable = false)
    private String estado;
}
