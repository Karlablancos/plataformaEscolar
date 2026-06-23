package com.colegio.establecimiento.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "tipo_calificacion")
public class TipoCalificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_calificacion")
    private Integer idTipoCalificacion;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "escala", nullable = false)
    private String escala;

    @Column(name = "entra_promedio", nullable = false)
    private Boolean entraPromedio;

    @Column(name = "minimo_aprobacion", nullable = false)
    private BigDecimal minimoAprobacion;
}
