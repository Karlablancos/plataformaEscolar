package com.colegio.academico.service;

import com.colegio.academico.dto.EvaluacionDTO;
import com.colegio.academico.model.Evaluacion;
import com.colegio.academico.repository.EvaluacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;

    public EvaluacionDTO crear(EvaluacionDTO dto) {
        Evaluacion e = toEntity(dto);
        return toDTO(evaluacionRepository.save(e));
    }

    public Optional<EvaluacionDTO> buscarPorId(Long id) {
        return evaluacionRepository.findById(id).map(this::toDTO);
    }

    public List<EvaluacionDTO> listarPorCurso(Long idCurso) {
        return evaluacionRepository.findByIdCurso(idCurso)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<EvaluacionDTO> actualizar(Long id, EvaluacionDTO dto) {
        return evaluacionRepository.findById(id).map(e -> {
            e.setNombre(dto.getNombre());
            e.setTipo(dto.getTipo());
            e.setPonderacion(dto.getPonderacion());
            e.setFecha(dto.getFecha());
            e.setIdCurso(dto.getIdCurso());
            e.setIdAsignatura(dto.getIdAsignatura());
            e.setIdDocente(dto.getIdDocente());
            e.setIdEstablecimiento(dto.getIdEstablecimiento());
            return toDTO(evaluacionRepository.save(e));
        });
    }

    public void eliminar(Long id) {
        evaluacionRepository.deleteById(id);
    }

    private Evaluacion toEntity(EvaluacionDTO dto) {
        Evaluacion e = new Evaluacion();
        e.setNombre(dto.getNombre());
        e.setTipo(dto.getTipo());
        e.setPonderacion(dto.getPonderacion());
        e.setFecha(dto.getFecha());
        e.setIdCurso(dto.getIdCurso());
        e.setIdAsignatura(dto.getIdAsignatura());
        e.setIdDocente(dto.getIdDocente());
        e.setIdEstablecimiento(dto.getIdEstablecimiento());
        return e;
    }

    EvaluacionDTO toDTO(Evaluacion e) {
        EvaluacionDTO dto = new EvaluacionDTO();
        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setTipo(e.getTipo());
        dto.setPonderacion(e.getPonderacion());
        dto.setFecha(e.getFecha());
        dto.setIdCurso(e.getIdCurso());
        dto.setIdAsignatura(e.getIdAsignatura());
        dto.setIdDocente(e.getIdDocente());
        dto.setIdEstablecimiento(e.getIdEstablecimiento());
        return dto;
    }
}
