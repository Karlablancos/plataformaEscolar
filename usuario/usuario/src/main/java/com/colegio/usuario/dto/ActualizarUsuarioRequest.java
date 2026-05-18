package com.colegio.usuario.dto;

import lombok.Data;

@Data
public class ActualizarUsuarioRequest {
    private Integer idRol;
    private String correoElectronico;
    private Boolean bloqueado;
    private String estado;
}
