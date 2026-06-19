package com.colegio.academico.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class NotaDTO {
    private Long id;
    private BigDecimal calificacion;
    private Long idEvaluacion;
    private Long idEstudiante;
    private Long idEstablecimiento;
}
