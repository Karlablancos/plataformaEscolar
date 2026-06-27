package com.colegio.establecimiento.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "id_establecimiento", nullable = false)
    private Integer idEstablecimiento;

    @Column(name = "id_rol", nullable = false)
    private Integer idRol;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "correo_electronico", nullable = false)
    private String correoElectronico;

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

    @Column(name = "intentos_fallidos", nullable = false)
    private Integer intentosFallidos;

    @Column(name = "bloqueado", nullable = false)
    private Boolean bloqueado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "estado", nullable = false)
    private String estado;
}
