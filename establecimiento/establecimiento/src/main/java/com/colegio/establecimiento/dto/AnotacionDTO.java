package com.colegio.establecimiento.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AnotacionDTO {

    private Integer idAnotacion;
    private Integer idEstudiante;
    private Integer idDocente;
    private String tipo;
    private String descripcion;
    private LocalDate fecha;
    private Boolean requiereCitacion;
    private LocalDate fechaCitacion;
    private String estado;
}
