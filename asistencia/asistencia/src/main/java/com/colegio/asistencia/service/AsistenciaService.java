package com.colegio.asistencia.service;

import com.colegio.asistencia.dto.AsistenciaDTO;
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