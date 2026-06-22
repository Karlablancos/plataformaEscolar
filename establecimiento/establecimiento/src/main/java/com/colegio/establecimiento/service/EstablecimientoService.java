package com.colegio.establecimiento.service;

import com.colegio.establecimiento.dto.AsignaturaDTO;
import com.colegio.establecimiento.dto.CursoDTO;
import com.colegio.establecimiento.dto.EstablecimientoDTO;
import com.colegio.establecimiento.dto.EstudianteDTO;
import com.colegio.establecimiento.dto.TipoCalificacionDTO;
import com.colegio.establecimiento.model.Asignatura;
import com.colegio.establecimiento.model.Curso;
import com.colegio.establecimiento.model.Establecimiento;
import com.colegio.establecimiento.model.Estudiante;
import com.colegio.establecimiento.model.TipoCalificacion;
import com.colegio.establecimiento.repository.AsignaturaRepository;
import com.colegio.establecimiento.repository.CursoRepository;
import com.colegio.establecimiento.repository.EstablecimientoRepository;
import com.colegio.establecimiento.repository.EstudianteRepository;
import com.colegio.establecimiento.repository.TipoCalificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final TipoCalificacionRepository tipoCalificacionRepository;

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

    public CursoDTO crearCurso(Integer idEstablecimiento, CursoDTO dto) {
        validarEstablecimiento(idEstablecimiento);
        validarDatosCurso(dto);

        Curso curso = new Curso();
        curso.setIdEstablecimiento(idEstablecimiento);
        curso.setNumero(dto.getNumero());
        curso.setLetra(dto.getLetra().trim().toUpperCase());
        curso.setTipoEnsenanza(dto.getTipoEnsenanza().trim());
        curso.setModalidad(dto.getModalidad().trim());
        curso.setAnioAcademico(dto.getAnioAcademico());
        curso.setEsNivelSimce(Boolean.TRUE.equals(dto.getEsNivelSimce()));
        curso.setEstado(normalizarEstadoCurso(dto.getEstado()));

        return toCursoDTO(cursoRepository.save(curso));
    }

    public Optional<CursoDTO> actualizarCurso(
            Integer idEstablecimiento, Integer idCurso, CursoDTO dto) {
        return cursoRepository.findByIdCursoAndIdEstablecimiento(idCurso, idEstablecimiento)
                .map(curso -> {
                    validarDatosCurso(dto);
                    curso.setNumero(dto.getNumero());
                    curso.setLetra(dto.getLetra().trim().toUpperCase());
                    curso.setTipoEnsenanza(dto.getTipoEnsenanza().trim());
                    curso.setModalidad(dto.getModalidad().trim());
                    curso.setAnioAcademico(dto.getAnioAcademico());
                    curso.setEsNivelSimce(Boolean.TRUE.equals(dto.getEsNivelSimce()));
                    curso.setEstado(normalizarEstadoCurso(dto.getEstado()));
                    return toCursoDTO(cursoRepository.save(curso));
                });
    }

    public boolean eliminarCurso(Integer idEstablecimiento, Integer idCurso) {
        Optional<Curso> curso = cursoRepository
                .findByIdCursoAndIdEstablecimiento(idCurso, idEstablecimiento);

        if (curso.isEmpty()) {
            return false;
        }

        try {
            cursoRepository.delete(curso.get());
            return true;
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException(
                    "No se puede eliminar el curso porque tiene alumnos, asignaturas o evaluaciones asociadas.");
        }
    }

    private void validarEstablecimiento(Integer idEstablecimiento) {
        if (!establecimientoRepository.existsById(idEstablecimiento)) {
            throw new IllegalArgumentException("Establecimiento no encontrado: " + idEstablecimiento);
        }
    }

    private void validarDatosCurso(CursoDTO dto) {
        if (dto.getNumero() == null || dto.getNumero() < 1) {
            throw new IllegalArgumentException("El número del curso es obligatorio.");
        }
        if (dto.getLetra() == null || dto.getLetra().isBlank()) {
            throw new IllegalArgumentException("La letra del curso es obligatoria.");
        }
        if (dto.getTipoEnsenanza() == null || dto.getTipoEnsenanza().isBlank()) {
            throw new IllegalArgumentException("El tipo de enseñanza es obligatorio.");
        }
        if (dto.getModalidad() == null || dto.getModalidad().isBlank()) {
            throw new IllegalArgumentException("La modalidad es obligatoria.");
        }
        if (dto.getAnioAcademico() == null) {
            throw new IllegalArgumentException("El año académico es obligatorio.");
        }
    }

    private String normalizarEstadoCurso(String estado) {
        if (estado == null || estado.isBlank()) {
            return "ACTIVO";
        }
        String normalizado = estado.trim().toUpperCase();
        if (normalizado.startsWith("INACTIV")) {
            return "INACTIVO";
        }
        if (normalizado.startsWith("CERRAD")) {
            return "CERRADO";
        }
        return "ACTIVO";
    }

    public List<AsignaturaDTO> listarAsignaturas(Integer idEstablecimiento) {
        return asignaturaRepository.findByIdEstablecimiento(idEstablecimiento)
                .stream()
                .map(this::toAsignaturaDTO)
                .toList();
    }

    public List<TipoCalificacionDTO> listarTiposCalificacion() {
        return tipoCalificacionRepository.findAll()
                .stream()
                .map(this::toTipoCalificacionDTO)
                .toList();
    }

    public AsignaturaDTO crearAsignatura(Integer idEstablecimiento, AsignaturaDTO dto) {
        validarEstablecimiento(idEstablecimiento);
        validarTipoCalificacion(dto.getIdTipoCalificacion());
        validarDatosAsignatura(dto);

        Asignatura asignatura = new Asignatura();
        asignatura.setIdEstablecimiento(idEstablecimiento);
        asignatura.setNombre(dto.getNombre().trim());
        asignatura.setCodigo(dto.getCodigo().trim().toUpperCase());
        asignatura.setIdTipoCalificacion(dto.getIdTipoCalificacion());
        asignatura.setEstado(normalizarEstado(dto.getEstado()));

        return toAsignaturaDTO(asignaturaRepository.save(asignatura));
    }

    public Optional<AsignaturaDTO> actualizarAsignatura(
            Integer idEstablecimiento, Integer idAsignatura, AsignaturaDTO dto) {
        return asignaturaRepository.findByIdAsignaturaAndIdEstablecimiento(idAsignatura, idEstablecimiento)
                .map(asignatura -> {
                    validarTipoCalificacion(dto.getIdTipoCalificacion());
                    validarDatosAsignatura(dto);
                    asignatura.setNombre(dto.getNombre().trim());
                    asignatura.setCodigo(dto.getCodigo().trim().toUpperCase());
                    asignatura.setIdTipoCalificacion(dto.getIdTipoCalificacion());
                    asignatura.setEstado(normalizarEstado(dto.getEstado()));
                    return toAsignaturaDTO(asignaturaRepository.save(asignatura));
                });
    }

    public boolean eliminarAsignatura(Integer idEstablecimiento, Integer idAsignatura) {
        Optional<Asignatura> asignatura = asignaturaRepository
                .findByIdAsignaturaAndIdEstablecimiento(idAsignatura, idEstablecimiento);

        if (asignatura.isEmpty()) {
            return false;
        }

        try {
            asignaturaRepository.delete(asignatura.get());
            return true;
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException(
                    "No se puede eliminar la asignatura porque está asociada a cursos o evaluaciones.");
        }
    }

    private void validarTipoCalificacion(Integer idTipoCalificacion) {
        if (idTipoCalificacion == null
                || !tipoCalificacionRepository.existsById(idTipoCalificacion)) {
            throw new IllegalArgumentException("Tipo de calificación no válido.");
        }
    }

    private void validarDatosAsignatura(AsignaturaDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (dto.getCodigo() == null || dto.getCodigo().isBlank()) {
            throw new IllegalArgumentException("El código es obligatorio.");
        }
    }

    private String normalizarEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return "ACTIVO";
        }
        String normalizado = estado.trim().toUpperCase();
        if (normalizado.startsWith("INACTIV")) {
            return "INACTIVO";
        }
        return "ACTIVO";
    }

    private TipoCalificacionDTO toTipoCalificacionDTO(TipoCalificacion tipo) {
        TipoCalificacionDTO dto = new TipoCalificacionDTO();
        dto.setIdTipoCalificacion(tipo.getIdTipoCalificacion());
        dto.setNombre(tipo.getNombre());
        dto.setEscala(tipo.getEscala());
        dto.setEntraPromedio(tipo.getEntraPromedio());
        dto.setMinimoAprobacion(tipo.getMinimoAprobacion());
        return dto;
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
        // nombre calculado: "1° Básica A"
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
