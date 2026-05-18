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

    @Column(name = "id_tipo_estab")
    private Integer idTipoEstab;

    @Column(name = "sostenedor", nullable = false)
    private String sostenedor;

    @Column(name = "director", nullable = false)
    private String director;

    @Column(name = "calle")
    private String calle;

    @Column(name = "numero")
    private String numero;

    @Column(name = "id_comuna")
    private Integer idComuna;

    @Column(name = "correo_electronico", nullable = false)
    private String correoElectronico;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "modo_aula")
    private String modoAula;

    @Column(name = "estado")
    private String estado;

    public Integer getIdEstablecimiento() { return idEstablecimiento; }
    public String getRbd() { return rbd; }
    public String getNombre() { return nombre; }
    public Integer getIdTipoEstab() { return idTipoEstab; }
    public String getSostenedor() { return sostenedor; }
    public String getDirector() { return director; }
    public String getCalle() { return calle; }
    public String getNumero() { return numero; }
    public Integer getIdComuna() { return idComuna; }
    public String getCorreoElectronico() { return correoElectronico; }
    public String getTelefono() { return telefono; }
    public String getModoAula() { return modoAula; }
    public String getEstado() { return estado; }
}