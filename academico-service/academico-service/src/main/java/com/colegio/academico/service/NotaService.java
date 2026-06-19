package com.colegio.academico.service;

import com.colegio.academico.dto.NotaDTO;
import com.colegio.academico.model.Evaluacion;
import com.colegio.academico.model.Nota;
import com.colegio.academico.repository.EvaluacionRepository;
import com.colegio.academico.repository.NotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotaService {

    private final NotaRepository notaRepository;
    private final EvaluacionRepository evaluacionRepository;

    public NotaDTO registrarNota(NotaDTO dto) {
        if (dto.getCalificacion().compareTo(BigDecimal.ONE) < 0
                || dto.getCalificacion().compareTo(new BigDecimal("7.0")) > 0) {
            throw new IllegalArgumentException("La calificación debe estar entre 1.0 y 7.0");
        }
        Evaluacion evaluacion = evaluacionRepository.findById(dto.getIdEvaluacion())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Evaluación no encontrada: " + dto.getIdEvaluacion()));
        Nota nota = new Nota();
        nota.setCalificacion(dto.getCalificacion());
        nota.setEvaluacion(evaluacion);
        nota.setIdEstudiante(dto.getIdEstudiante());
        nota.setIdEstablecimiento(dto.getIdEstablecimiento());
        return toDTO(notaRepository.save(nota));
    }

    public Optional<NotaDTO> buscarPorId(Long id) {
        return notaRepository.findById(id).map(this::toDTO);
    }

    public BigDecimal calcularPromedio(Long idEstudiante, Long idAsignatura) {
        List<Nota> notas = notaRepository
                .findByIdEstudianteAndEvaluacion_IdAsignatura(idEstudiante, idAsignatura);
        if (notas.isEmpty()) return BigDecimal.ZERO;

        BigDecimal sumaPonderada = BigDecimal.ZERO;
        BigDecimal sumaPonderaciones = BigDecimal.ZERO;
        for (Nota n : notas) {
            BigDecimal pond = n.getEvaluacion().getPonderacion();
            sumaPonderada = sumaPonderada.add(n.getCalificacion().multiply(pond));
            sumaPonderaciones = sumaPonderaciones.add(pond);
        }
        if (sumaPonderaciones.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return sumaPonderada.divide(sumaPonderaciones, 1, RoundingMode.HALF_UP);
    }

    public String calcularPromocion(Long idEstudiante) {
        List<Nota> todasLasNotas = notaRepository.findByIdEstudiante(idEstudiante);
        if (todasLasNotas.isEmpty()) return "REPROBADO";

        // Agrupar por idAsignatura
        Map<Long, List<Nota>> porAsignatura = todasLasNotas.stream()
                .collect(Collectors.groupingBy(n -> n.getEvaluacion().getIdAsignatura()));

        BigDecimal sumPromedios = BigDecimal.ZERO;
        int reprobadas = 0;

        for (List<Nota> notasAsig : porAsignatura.values()) {
            BigDecimal sumaPonderada = BigDecimal.ZERO;
            BigDecimal sumaPonderaciones = BigDecimal.ZERO;
            for (Nota n : notasAsig) {
                BigDecimal pond = n.getEvaluacion().getPonderacion();
                sumaPonderada = sumaPonderada.add(n.getCalificacion().multiply(pond));
                sumaPonderaciones = sumaPonderaciones.add(pond);
            }
            BigDecimal promedioAsig = sumaPonderaciones.compareTo(BigDecimal.ZERO) == 0
                    ? BigDecimal.ZERO
                    : sumaPonderada.divide(sumaPonderaciones, 1, RoundingMode.HALF_UP);

            sumPromedios = sumPromedios.add(promedioAsig);
            if (promedioAsig.compareTo(new BigDecimal("4.0")) < 0) reprobadas++;
        }

        // Decreto 67/2018
        if (reprobadas == 0) return "APROBADO";

        BigDecimal promedioGeneral = sumPromedios.divide(
                new BigDecimal(porAsignatura.size()), 1, RoundingMode.HALF_UP);
        if (promedioGeneral.compareTo(new BigDecimal("4.5")) >= 0 && reprobadas <= 1) {
            return "APROBADO";
        }
        return "REPROBADO";
    }

    private NotaDTO toDTO(Nota n) {
        NotaDTO dto = new NotaDTO();
        dto.setId(n.getId());
        dto.setCalificacion(n.getCalificacion());
        dto.setIdEvaluacion(n.getEvaluacion().getId());
        dto.setIdEstudiante(n.getIdEstudiante());
        dto.setIdEstablecimiento(n.getIdEstablecimiento());
        return dto;
    }
}
