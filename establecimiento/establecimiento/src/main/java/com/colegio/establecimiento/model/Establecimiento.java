package com.colegio.establecimiento.model;

import jakarta.persistence.*;
import lombok.Setter;

@Setter
@Entity
@Table(name = "establecimiento")
public class Establecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_establecimiento")
    private Integer idEstablecimiento;

    @Column(name = "rbd", nullable = false)
    private String rbd;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "sostenedor", nullable = false)
    private String sostenedor;

    @Column(name = "director", nullable = false)
    private String director;

    @Column(name = "correo_electronico", nullable = false)
    private String correoElectronico;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "estado")
    private String estado;

    public Integer getIdEstablecimiento() { return idEstablecimiento; }
    public String getRbd() { return rbd; }
    public String getNombre() { return nombre; }
    public String getSostenedor() { return sostenedor; }
    public String getDirector() { return director; }
    public String getCorreoElectronico() { return correoElectronico; }
    public String getTelefono() { return telefono; }
    public String getEstado() { return estado; }
}