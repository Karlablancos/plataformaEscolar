package com.colegio.usuario.dto;

import lombok.Data;

@Data
public class ActualizarUsuarioRequest {
    private String username;
    private String correoElectronico;
    private Integer idRol;
    private String estado;
}