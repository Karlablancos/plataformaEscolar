package com.colegio.usuario.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UsuarioDTO {

    private Integer idUsuario;
    private Integer idEstablecimiento;
    private Integer idRol;
    private String username;
    private String correoElectronico;
    private LocalDateTime ultimoAcceso;
    private Boolean bloqueado;
    private String estado;
}