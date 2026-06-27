package com.colegio.establecimiento.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sala")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sala")
    private Integer idSala;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "piso", nullable = false)
    private Integer piso;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "id_establecimiento", nullable = false)
    private Integer idEstablecimiento;
}
