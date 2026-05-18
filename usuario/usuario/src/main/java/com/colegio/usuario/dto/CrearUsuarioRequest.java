package com.colegio.usuario.dto;

import lombok.Data;

@Data
public class CrearUsuarioRequest {
    private String username;
    private String password;
    private Integer idEstablecimiento;
    private Integer idRol;
    private String correoElectronico;
    private String estado;
}
