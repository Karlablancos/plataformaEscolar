package com.colegio.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EliminarUsuarioResponse {

    private String accion;
    private String mensaje;
    private UsuarioDTO usuario;
}
