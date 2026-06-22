package com.colegio.establecimiento.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TipoCalificacionDTO {

    private Integer idTipoCalificacion;
    private String nombre;
    private String escala;
    private Boolean entraPromedio;
    private BigDecimal minimoAprobacion;
}
