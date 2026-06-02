package com.colegio.establecimiento.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "asignatura")
public class Asignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignatura")
    private Integer idAsignatura;

    @Column(name = "id_establecimiento", nullable = false)
    private Integer idEstablecimiento;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "codigo", nullable = false)
    private String codigo;

    @Column(name = "id_tipo_calificacion", nullable = false)
    private Integer idTipoCalificacion;

    @Column(name = "estado", nullable = false)
    private String estado;
}
