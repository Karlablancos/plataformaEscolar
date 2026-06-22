package com.colegio.establecimiento.service;

import com.colegio.establecimiento.dto.AnotacionDTO;
import com.colegio.establecimiento.model.Anotacion;
import com.colegio.establecimiento.repository.AnotacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnotacionService {

    private final AnotacionRepository anotacionRepository;

    public List<AnotacionDTO> listarPorEstudiante(Integer idEstudiante) {
        return anotacionRepository.findByIdEstudiante(idEstudiante)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<AnotacionDTO> listarPorDocente(Integer idDocente) {
        return anotacionRepository.findByIdDocente(idDocente)
                .stream()
                .map(this::toDTO)
                .toList();
    }


    public List<AnotacionDTO> listarCitacionesPendientes() {
        return anotacionRepository.findByRequiereCitacionTrueAndFechaCitacionIsNull()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public AnotacionDTO crear(AnotacionDTO request) {
        Anotacion anotacion = new Anotacion();
        anotacion.setIdEstudiante(request.getIdEstudiante());
        anotacion.setIdDocente(request.getIdDocente());
        anotacion.setTipo(request.getTipo());
        anotacion.setDescripcion(request.getDescripcion());
        anotacion.setFecha(request.getFecha());
        anotacion.setRequiereCitacion(request.getRequiereCitacion() != null ? request.getRequiereCitacion() : false);
        anotacion.setFechaCitacion(request.getFechaCitacion());
        anotacion.setEstado(request.getEstado() != null ? request.getEstado() : "ACTIVA");
        return toDTO(anotacionRepository.save(anotacion));
    }

    public Optional<AnotacionDTO> actualizar(Integer idAnotacion, AnotacionDTO request) {
        return anotacionRepository.findById(idAnotacion)
                .map(a -> {
                    if (request.getTipo() != null) a.setTipo(request.getTipo());
                    if (request.getDescripcion() != null) a.setDescripcion(request.getDescripcion());
                    if (request.getFecha() != null) a.setFecha(request.getFecha());
                    if (request.getRequiereCitacion() != null) a.setRequiereCitacion(request.getRequiereCitacion());
                    if (request.getFechaCitacion() != null) a.setFechaCitacion(request.getFechaCitacion());
                    if (request.getEstado() != null) a.setEstado(request.getEstado());
                    return toDTO(anotacionRepository.save(a));
                });
    }

    public void eliminar(Integer idAnotacion) {
        anotacionRepository.deleteById(idAnotacion);
    }

    private AnotacionDTO toDTO(Anotacion a) {
        AnotacionDTO dto = new AnotacionDTO();
        dto.setIdAnotacion(a.getIdAnotacion());
        dto.setIdEstudiante(a.getIdEstudiante());
        dto.setIdDocente(a.getIdDocente());
        dto.setTipo(a.getTipo());
        dto.setDescripcion(a.getDescripcion());
        dto.setFecha(a.getFecha());
        dto.setRequiereCitacion(a.getRequiereCitacion());
        dto.setFechaCitacion(a.getFechaCitacion());
        dto.setEstado(a.getEstado());
        return dto;
    }
}