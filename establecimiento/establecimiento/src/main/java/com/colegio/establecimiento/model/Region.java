package com.colegio.establecimiento.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_region")
    private Integer idRegion;

    @Column(name = "nombre_region", nullable = false)
    private String nombreRegion;
}
