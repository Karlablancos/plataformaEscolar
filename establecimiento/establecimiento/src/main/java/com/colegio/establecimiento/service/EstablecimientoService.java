package com.colegio.establecimiento.service;

import com.colegio.establecimiento.dto.AsignaturaDTO;
import com.colegio.establecimiento.dto.CursoDTO;
import com.colegio.establecimiento.dto.EstablecimientoDTO;
import com.colegio.establecimiento.dto.EstudianteDTO;
import com.colegio.establecimiento.model.Asignatura;
import com.colegio.establecimiento.model.Curso;
import com.colegio.establecimiento.model.Establecimiento;
import com.colegio.establecimiento.model.Estudiante;
import com.colegio.establecimiento.repository.AsignaturaRepository;
import com.colegio.establecimiento.repository.CursoRepository;
import com.colegio.establecimiento.repository.EstablecimientoRepository;
import com.colegio.establecimiento.repository.EstudianteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EstablecimientoService {

    private final EstablecimientoRepository establecimientoRepository;
    private final CursoRepository cursoRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final EstudianteRepository estudianteRepository;

    public List<EstablecimientoDTO> listarTodos(Integer idEstablecimiento) {
        List<Establecimiento> fuente = (idEstablecimiento != null)
                ? establecimientoRepository.findById(idEstablecimiento)
                  .map(List::of).orElse(List.of())
                : establecimientoRepository.findByEstado("ACTIVO");
        return fuente.stream().map(this::toEstablecimientoDTO).toList();
    }

    private EstablecimientoDTO toEstablecimientoDTO(Establecimiento e) {
        EstablecimientoDTO dto = new EstablecimientoDTO();
        dto.setIdEstablecimiento(e.getIdEstablecimiento());
        dto.setRbd(e.getRbd());
        dto.setNombre(e.getNombre());
        dto.setIdTipoEstab(e.getIdTipoEstab());
        dto.setSostenedor(e.getSostenedor());
        dto.setDirector(e.getDirector());
        dto.setCalle(e.getCalle());
        dto.setNumero(e.getNumero());
        dto.setIdComuna(e.getIdComuna());
        dto.setCorreoElectronico(e.getCorreoElectronico());
        dto.setTelefono(e.getTelefono());
        dto.setModoAula(e.getModoAula());
        dto.setEstado(e.getEstado());
        return dto;
    }

    public Optional<Establecimiento> buscarPorRbd(String rbd) {
        return establecimientoRepository.findByRbd(rbd);
    }

    public boolean existeRbd(String rbd) {
        return establecimientoRepository.findByRbd(rbd).isPresent();
    }

    public List<CursoDTO> listarCursos(Integer idEstablecimiento) {
        return cursoRepository.findByIdEstablecimiento(idEstablecimiento)
                .stream()
                .map(this::toCursoDTO)
                .toList();
    }

    public CursoDTO crearCurso(Integer idEstablecimiento, CursoDTO request) {
        Curso curso = new Curso();
        curso.setIdEstablecimiento(idEstablecimiento);
        curso.setNumero(request.getNumero());
        curso.setLetra(request.getLetra());
        curso.setTipoEnsenanza(request.getTipoEnsenanza());
        curso.setModalidad(request.getModalidad());
        curso.setAnioAcademico(request.getAnioAcademico());
        curso.setEsNivelSimce(request.getEsNivelSimce() != null ? request.getEsNivelSimce() : false);
        curso.setEstado(request.getEstado() != null ? request.getEstado() : "ACTIVO");
        return toCursoDTO(cursoRepository.save(curso));
    }

    public Optional<CursoDTO> actualizarCurso(Integer idEstablecimiento, Integer idCurso, CursoDTO request) {
        return cursoRepository.findById(idCurso)
                .filter(c -> c.getIdEstablecimiento().equals(idEstablecimiento))
                .map(c -> {
                    if (request.getNumero() != null) c.setNumero(request.getNumero());
                    if (request.getLetra() != null) c.setLetra(request.getLetra());
                    if (request.getTipoEnsenanza() != null) c.setTipoEnsenanza(request.getTipoEnsenanza());
                    if (request.getModalidad() != null) c.setModalidad(request.getModalidad());
                    if (request.getAnioAcademico() != null) c.setAnioAcademico(request.getAnioAcademico());
                    if (request.getEsNivelSimce() != null) c.setEsNivelSimce(request.getEsNivelSimce());
                    if (request.getEstado() != null) c.setEstado(request.getEstado());
                    return toCursoDTO(cursoRepository.save(c));
                });
    }

    public void eliminarCurso(Integer idEstablecimiento, Integer idCurso) {
        cursoRepository.findById(idCurso)
                .filter(c -> c.getIdEstablecimiento().equals(idEstablecimiento))
                .ifPresent(cursoRepository::delete);
    }

    public List<AsignaturaDTO> listarAsignaturas(Integer idEstablecimiento) {
        return asignaturaRepository.findByIdEstablecimiento(idEstablecimiento)
                .stream()
                .map(this::toAsignaturaDTO)
                .toList();
    }

    public List<EstudianteDTO> listarEstudiantes(Integer idEstablecimiento) {
        return estudianteRepository.findByIdEstablecimiento(idEstablecimiento)
                .stream()
                .map(this::toEstudianteDTO)
                .toList();
    }

    private CursoDTO toCursoDTO(Curso c) {
        CursoDTO dto = new CursoDTO();
        dto.setIdCurso(c.getIdCurso());
        dto.setIdEstablecimiento(c.getIdEstablecimiento());
        dto.setNumero(c.getNumero());
        dto.setLetra(c.getLetra());
        dto.setTipoEnsenanza(c.getTipoEnsenanza());
        dto.setModalidad(c.getModalidad());
        dto.setAnioAcademico(c.getAnioAcademico());
        dto.setEsNivelSimce(c.getEsNivelSimce());
        dto.setEstado(c.getEstado());
        dto.setNombre(c.getNumero() + "° " + c.getTipoEnsenanza() + " " + c.getLetra().trim());
        return dto;
    }

    private AsignaturaDTO toAsignaturaDTO(Asignatura a) {
        AsignaturaDTO dto = new AsignaturaDTO();
        dto.setIdAsignatura(a.getIdAsignatura());
        dto.setIdEstablecimiento(a.getIdEstablecimiento());
        dto.setNombre(a.getNombre());
        dto.setCodigo(a.getCodigo());
        dto.setIdTipoCalificacion(a.getIdTipoCalificacion());
        dto.setEstado(a.getEstado());
        return dto;
    }

    private EstudianteDTO toEstudianteDTO(Estudiante e) {
        EstudianteDTO dto = new EstudianteDTO();
        dto.setIdEstudiante(e.getIdEstudiante());
        dto.setIdEstablecimiento(e.getIdEstablecimiento());
        dto.setRut(e.getRut());
        dto.setDv(e.getDv());
        dto.setNombres(e.getNombres());
        dto.setApellidoPaterno(e.getApellidoPaterno());
        dto.setApellidoMaterno(e.getApellidoMaterno());
        dto.setNombreCompleto(e.getNombres() + " " + e.getApellidoPaterno() + " " + e.getApellidoMaterno());
        dto.setFechaNacimiento(e.getFechaNacimiento());
        dto.setCorreoElectronico(e.getCorreoElectronico());
        dto.setTelefono(e.getTelefono());
        dto.setFechaMatricula(e.getFechaMatricula());
        dto.setPrioritario(e.getPrioritario());
        dto.setPreferente(e.getPreferente());
        dto.setTieneNee(e.getTieneNee());
        dto.setEnPie(e.getEnPie());
        dto.setEstado(e.getEstado());
        return dto;
    }
}