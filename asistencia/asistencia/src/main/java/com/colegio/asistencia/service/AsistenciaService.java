package com.colegio.asistencia.service;

import com.colegio.asistencia.dto.AsistenciaDTO;
import com.colegio.asistencia.dto.ResumenAsistenciaDTO;
import com.colegio.asistencia.model.Asistencia;
import com.colegio.asistencia.repository.AsistenciaRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsistenciaService {

    private final AsistenciaRepository repository;

    public AsistenciaService(AsistenciaRepository repository) {
        this.repository = repository;
    }

    // Obtener todas las asistencias de un estudiante
    public List<AsistenciaDTO> obtenerPorEstudiante(Integer idEstudiante) {
        return repository.findByIdEstudiante(idEstudiante)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener asistencia de un estudiante en una fecha
    public List<AsistenciaDTO> obtenerPorEstudianteYFecha(
            Integer idEstudiante, LocalDate fecha) {
        return repository.findByIdEstudianteAndFecha(idEstudiante, fecha)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Registrar una asistencia
    public AsistenciaDTO registrar(AsistenciaDTO dto) {
        Asistencia asistencia = new Asistencia();
        asistencia.setIdEstudiante(dto.getIdEstudiante());
        asistencia.setIdHorario(dto.getIdHorario());
        asistencia.setFecha(dto.getFecha());
        asistencia.setEstadoAsistencia(dto.getEstadoAsistencia());
        asistencia.setHoraLlegada(dto.getHoraLlegada());
        asistencia.setJustificada(dto.getJustificada());
        asistencia.setObservacion(dto.getObservacion());
        Asistencia guardada = repository.save(asistencia);
        return convertirADTO(guardada);
    }

    // Calcula el resumen de asistencia de un estudiante y si cumple el 85% mínimo exigido
    public ResumenAsistenciaDTO calcularResumen(Integer idEstudiante) {
        List<Asistencia> asistencias = repository.findByIdEstudiante(idEstudiante);

        int totalClases = asistencias.size();
        int presentes = 0;
        int atrasos = 0;
        int ausenciasJustificadas = 0;
        int ausenciasInjustificadas = 0;

        for (Asistencia a : asistencias) {
            String estado = a.getEstadoAsistencia();
            boolean justificada = Boolean.TRUE.equals(a.getJustificada());

            if ("PRESENTE".equals(estado)) {
                presentes++;
            } else if ("ATRASADO".equals(estado)) {
                atrasos++;
            } else if ("AUSENTE".equals(estado)) {
                if (justificada) {
                    ausenciasJustificadas++;
                } else {
                    ausenciasInjustificadas++;
                }
            }
        }

        // Asistencia válida: presentes + atrasos + ausencias justificadas
        int asistenciaValida = presentes + atrasos + ausenciasJustificadas;

        double porcentaje = totalClases > 0
                ? (asistenciaValida * 100.0) / totalClases
                : 0.0;

        ResumenAsistenciaDTO resumen = new ResumenAsistenciaDTO();
        resumen.setIdEstudiante(idEstudiante);
        resumen.setTotalClases(totalClases);
        resumen.setPresentes(presentes);
        resumen.setAtrasos(atrasos);
        resumen.setAusenciasJustificadas(ausenciasJustificadas);
        resumen.setAusenciasInjustificadas(ausenciasInjustificadas);
        resumen.setAsistenciaValida(asistenciaValida);
        resumen.setPorcentajeAsistencia(Math.round(porcentaje * 100) / 100.0); // redondeado a 2 decimales
        resumen.setCumple85(porcentaje >= 85.0);

        return resumen;
    }

    // Convertir Model a DTO
    private AsistenciaDTO convertirADTO(Asistencia a) {
        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setIdAsistencia(a.getIdAsistencia());
        dto.setIdEstudiante(a.getIdEstudiante());
        dto.setIdHorario(a.getIdHorario());
        dto.setFecha(a.getFecha());
        dto.setEstadoAsistencia(a.getEstadoAsistencia());
        dto.setHoraLlegada(a.getHoraLlegada());
        dto.setJustificada(a.getJustificada());
        dto.setObservacion(a.getObservacion());
        return dto;
    }
}