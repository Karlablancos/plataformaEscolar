package com.colegio.academico.dto;

import com.colegio.academico.model.TipoEvaluacion;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EvaluacionDTO {
    private Long id;
    private String nombre;
    private TipoEvaluacion tipo;
    private BigDecimal ponderacion;
    private LocalDate fecha;
    private Long idCurso;
    private Long idAsignatura;
    private Long idDocente;
    private Long idEstablecimiento;
}
