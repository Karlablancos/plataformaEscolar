package com.colegio.establecimiento.service;

import com.colegio.establecimiento.dto.AsignaturaDTO;
import com.colegio.establecimiento.dto.ComunaDTO;
import com.colegio.establecimiento.dto.CursoAsignaturaDTO;
import com.colegio.establecimiento.dto.CursoAsignaturaRequestDTO;
import com.colegio.establecimiento.dto.CursoDTO;
import com.colegio.establecimiento.dto.DocenteDTO;
import com.colegio.establecimiento.dto.EstablecimientoDTO;
import com.colegio.establecimiento.dto.EstudianteDTO;
import com.colegio.establecimiento.dto.RegionDTO;
import com.colegio.establecimiento.dto.SalaDTO;
import com.colegio.establecimiento.dto.TipoCalificacionDTO;
import com.colegio.establecimiento.model.Asignatura;
import com.colegio.establecimiento.model.Comuna;
import com.colegio.establecimiento.model.Curso;
import com.colegio.establecimiento.model.CursoAsignatura;
import com.colegio.establecimiento.model.Docente;
import com.colegio.establecimiento.model.Establecimiento;
import com.colegio.establecimiento.model.Estudiante;
import com.colegio.establecimiento.model.EstudianteCurso;
import com.colegio.establecimiento.model.Region;
import com.colegio.establecimiento.model.Rol;
import com.colegio.establecimiento.model.Sala;
import com.colegio.establecimiento.model.TipoCalificacion;
import com.colegio.establecimiento.model.Usuario;
import com.colegio.establecimiento.repository.AsignaturaRepository;
import com.colegio.establecimiento.repository.ComunaRepository;
import com.colegio.establecimiento.repository.CursoAsignaturaRepository;
import com.colegio.establecimiento.repository.CursoRepository;
import com.colegio.establecimiento.repository.DocenteRepository;
import com.colegio.establecimiento.repository.EstablecimientoRepository;
import com.colegio.establecimiento.repository.EstudianteCursoRepository;
import com.colegio.establecimiento.repository.EstudianteRepository;
import com.colegio.establecimiento.repository.RegionRepository;
import com.colegio.establecimiento.repository.RolRepository;
import com.colegio.establecimiento.repository.SalaRepository;
import com.colegio.establecimiento.repository.TipoCalificacionRepository;
import com.colegio.establecimiento.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstablecimientoService {

    private final EstablecimientoRepository establecimientoRepository;
    private final CursoRepository cursoRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final EstudianteRepository estudianteRepository;
    private final EstudianteCursoRepository estudianteCursoRepository;
    private final DocenteRepository docenteRepository;
    private final CursoAsignaturaRepository cursoAsignaturaRepository;
    private final TipoCalificacionRepository tipoCalificacionRepository;
    private final RegionRepository regionRepository;
    private final ComunaRepository comunaRepository;
    private final SalaRepository salaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

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
        validarEstablecimiento(idEstablecimiento);

        List<Estudiante> estudiantes = estudianteRepository.findByIdEstablecimiento(idEstablecimiento);
        Map<Integer, Integer> cursoPorEstudiante = mapCursoActivoPorEstudiante(estudiantes);

        return estudiantes.stream()
                .map(estudiante -> toEstudianteDTO(
                        estudiante,
                        cursoPorEstudiante.get(estudiante.getIdEstudiante())))
                .toList();
    }

    public EstudianteDTO crearEstudiante(Integer idEstablecimiento, EstudianteDTO dto) {
        validarEstablecimiento(idEstablecimiento);
        validarDatosEstudiante(dto);

        Rol rolEstudiante = rolRepository.findByNombreRol("ESTUDIANTE")
                .orElseThrow(() -> new IllegalArgumentException("Rol ESTUDIANTE no encontrado en el sistema."));

        Usuario usuario = new Usuario();
        usuario.setIdEstablecimiento(idEstablecimiento);
        usuario.setIdRol(rolEstudiante.getIdRol());
        usuario.setUsername(dto.getRut().trim() + dto.getDv().trim().toUpperCase());
        usuario.setPasswordHash("SIN_ACCESO");
        usuario.setCorreoElectronico(dto.getCorreoElectronico().trim());
        usuario.setIntentosFallidos(0);
        usuario.setBloqueado(true);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setEstado("INACTIVO");
        Integer idUsuario = usuarioRepository.save(usuario).getIdUsuario();

        Estudiante estudiante = new Estudiante();
        estudiante.setIdEstablecimiento(idEstablecimiento);
        estudiante.setIdUsuario(idUsuario);
        estudiante.setRut(dto.getRut().trim());
        estudiante.setDv(dto.getDv().trim().toUpperCase());
        estudiante.setNombres(dto.getNombres().trim());
        estudiante.setApellidoPaterno(dto.getApellidoPaterno().trim());
        estudiante.setApellidoMaterno(dto.getApellidoMaterno() != null ? dto.getApellidoMaterno().trim() : "");
        estudiante.setFechaNacimiento(dto.getFechaNacimiento());
        estudiante.setCorreoElectronico(dto.getCorreoElectronico().trim());
        estudiante.setTelefono(dto.getTelefono() != null ? dto.getTelefono().trim() : "");
        estudiante.setCalle(dto.getCalle() != null ? dto.getCalle().trim() : "");
        estudiante.setNumero(dto.getNumero() != null ? dto.getNumero().trim() : "");
        estudiante.setIdComuna(dto.getIdComuna() != null ? dto.getIdComuna() : 1);
        estudiante.setColegioProcedente(dto.getColegioProcedente() != null ? dto.getColegioProcedente().trim() : "");
        estudiante.setFechaMatricula(dto.getFechaMatricula() != null ? dto.getFechaMatricula() : LocalDate.now());
        estudiante.setPrioritario(Boolean.TRUE.equals(dto.getPrioritario()));
        estudiante.setPreferente(Boolean.TRUE.equals(dto.getPreferente()));
        estudiante.setTieneNee(Boolean.TRUE.equals(dto.getTieneNee()));
        estudiante.setIdTipoNee(Boolean.TRUE.equals(dto.getTieneNee()) ? dto.getIdTipoNee() : null);
        estudiante.setEnPie(Boolean.TRUE.equals(dto.getEnPie()));
        estudiante.setEstado(normalizarEstado(dto.getEstado()));

        return toEstudianteDTO(estudianteRepository.save(estudiante), null);
    }

    private void validarDatosEstudiante(EstudianteDTO dto) {
        if (dto.getRut() == null || dto.getRut().isBlank()) {
            throw new IllegalArgumentException("El RUT es obligatorio.");
        }
        if (dto.getDv() == null || dto.getDv().isBlank()) {
            throw new IllegalArgumentException("El dígito verificador es obligatorio.");
        }
        if (dto.getNombres() == null || dto.getNombres().isBlank()) {
            throw new IllegalArgumentException("Los nombres son obligatorios.");
        }
        if (dto.getApellidoPaterno() == null || dto.getApellidoPaterno().isBlank()) {
            throw new IllegalArgumentException("El apellido paterno es obligatorio.");
        }
        if (dto.getFechaNacimiento() == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        }
        if (dto.getCorreoElectronico() == null || dto.getCorreoElectronico().isBlank()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio.");
        }
    }

    public List<EstudianteDTO> listarEstudiantesPorCurso(
            Integer idEstablecimiento, Integer idCurso) {
        validarCurso(idEstablecimiento, idCurso);

        return estudianteCursoRepository.findByIdCursoAndEstado(idCurso, "ACTIVO")
                .stream()
                .map(matricula -> estudianteRepository.findById(matricula.getIdEstudiante()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(estudiante -> estudiante.getIdEstablecimiento().equals(idEstablecimiento))
                .map(estudiante -> toEstudianteDTO(estudiante, idCurso))
                .toList();
    }

    public EstudianteDTO matricularEstudiante(
            Integer idEstablecimiento, Integer idCurso, Integer idEstudiante) {
        validarCurso(idEstablecimiento, idCurso);

        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
                .filter(item -> item.getIdEstablecimiento().equals(idEstablecimiento))
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado."));

        estudianteCursoRepository.findByIdEstudianteAndEstado(idEstudiante, "ACTIVO")
                .ifPresent(matriculaActiva -> {
                    if (!matriculaActiva.getIdCurso().equals(idCurso)) {
                        matriculaActiva.setEstado("INACTIVO");
                        estudianteCursoRepository.save(matriculaActiva);
                    }
                });

        EstudianteCurso matricula = estudianteCursoRepository
                .findByIdEstudianteAndIdCurso(idEstudiante, idCurso)
                .orElseGet(() -> {
                    EstudianteCurso nueva = new EstudianteCurso();
                    nueva.setIdEstudiante(idEstudiante);
                    nueva.setIdCurso(idCurso);
                    return nueva;
                });

        matricula.setFechaMatricula(LocalDate.now());
        matricula.setEstado("ACTIVO");
        estudianteCursoRepository.save(matricula);

        return toEstudianteDTO(estudiante, idCurso);
    }

    public boolean desmatricularEstudiante(
            Integer idEstablecimiento, Integer idCurso, Integer idEstudiante) {
        if (cursoRepository.findByIdCursoAndIdEstablecimiento(idCurso, idEstablecimiento).isEmpty()) {
            return false;
        }

        Optional<EstudianteCurso> matricula = estudianteCursoRepository
                .findByIdEstudianteAndIdCurso(idEstudiante, idCurso);

        if (matricula.isEmpty() || !"ACTIVO".equals(matricula.get().getEstado())) {
            return false;
        }

        EstudianteCurso registro = matricula.get();
        registro.setEstado("INACTIVO");
        estudianteCursoRepository.save(registro);
        return true;
    }

    public List<DocenteDTO> listarDocentes(Integer idEstablecimiento) {
        validarEstablecimiento(idEstablecimiento);

        return docenteRepository.findByIdEstablecimiento(idEstablecimiento)
                .stream()
                .map(this::toDocenteDTO)
                .toList();
    }

    public Optional<CursoDTO> asignarProfesorJefe(
            Integer idEstablecimiento, Integer idCurso, Integer idDocente) {
        return cursoRepository.findByIdCursoAndIdEstablecimiento(idCurso, idEstablecimiento)
                .map(curso -> {
                    if (idDocente != null) {
                        validarDocente(idEstablecimiento, idDocente);
                    }
                    curso.setIdDocenteJefe(idDocente);
                    return toCursoDTO(cursoRepository.save(curso));
                });
    }

    public List<CursoAsignaturaDTO> listarAsignaturasCurso(
            Integer idEstablecimiento, Integer idCurso) {
        validarCurso(idEstablecimiento, idCurso);

        return cursoAsignaturaRepository.findByIdCursoAndEstado(idCurso, "ACTIVO")
                .stream()
                .map(relacion -> toCursoAsignaturaDTO(relacion, idEstablecimiento))
                .toList();
    }

    public CursoAsignaturaDTO asignarAsignaturaCurso(
            Integer idEstablecimiento,
            Integer idCurso,
            CursoAsignaturaRequestDTO request) {
        validarCurso(idEstablecimiento, idCurso);
        validarDatosAsignaturaCurso(request);

        Integer idAsignatura = request.getIdAsignatura();
        Integer idDocente = request.getIdDocente();
        Integer horasSemanales = request.getHorasSemanales() != null ? request.getHorasSemanales() : 4;

        validarAsignatura(idEstablecimiento, idAsignatura);
        validarDocente(idEstablecimiento, idDocente);

        Optional<CursoAsignatura> existente = cursoAsignaturaRepository
                .findByIdCursoAndIdAsignatura(idCurso, idAsignatura);

        if (existente.isPresent() && "ACTIVO".equals(existente.get().getEstado())) {
            throw new IllegalArgumentException("La asignatura ya está asignada al curso.");
        }

        CursoAsignatura relacion = existente.orElseGet(() -> {
            CursoAsignatura nueva = new CursoAsignatura();
            nueva.setIdCurso(idCurso);
            nueva.setIdAsignatura(idAsignatura);
            return nueva;
        });

        relacion.setIdDocente(idDocente);
        relacion.setHorasSemanales(horasSemanales);
        relacion.setEstado("ACTIVO");

        return toCursoAsignaturaDTO(cursoAsignaturaRepository.save(relacion), idEstablecimiento);
    }

    public boolean quitarAsignaturaCurso(
            Integer idEstablecimiento, Integer idCurso, Integer idAsignatura) {
        if (cursoRepository.findByIdCursoAndIdEstablecimiento(idCurso, idEstablecimiento).isEmpty()) {
            return false;
        }

        Optional<CursoAsignatura> relacion = cursoAsignaturaRepository
                .findByIdCursoAndIdAsignatura(idCurso, idAsignatura);

        if (relacion.isEmpty() || !"ACTIVO".equals(relacion.get().getEstado())) {
            return false;
        }

        CursoAsignatura registro = relacion.get();
        registro.setEstado("INACTIVO");
        cursoAsignaturaRepository.save(registro);
        return true;
    }

    private void validarAsignatura(Integer idEstablecimiento, Integer idAsignatura) {
        if (asignaturaRepository.findByIdAsignaturaAndIdEstablecimiento(idAsignatura, idEstablecimiento).isEmpty()) {
            throw new IllegalArgumentException("Asignatura no encontrada.");
        }
    }

    private void validarDatosAsignaturaCurso(CursoAsignaturaRequestDTO request) {
        if (request == null || request.getIdAsignatura() == null) {
            throw new IllegalArgumentException("La asignatura es obligatoria.");
        }
        if (request.getIdDocente() == null) {
            throw new IllegalArgumentException("El docente responsable es obligatorio.");
        }
        if (request.getHorasSemanales() != null && request.getHorasSemanales() < 1) {
            throw new IllegalArgumentException("Las horas semanales deben ser mayores a cero.");
        }
    }

    private CursoAsignaturaDTO toCursoAsignaturaDTO(CursoAsignatura relacion, Integer idEstablecimiento) {
        CursoAsignaturaDTO dto = new CursoAsignaturaDTO();
        dto.setIdCursoAsignatura(relacion.getIdCursoAsignatura());
        dto.setIdCurso(relacion.getIdCurso());
        dto.setIdAsignatura(relacion.getIdAsignatura());
        dto.setIdDocente(relacion.getIdDocente());
        dto.setHorasSemanales(relacion.getHorasSemanales());
        dto.setEstado(relacion.getEstado());

        asignaturaRepository.findByIdAsignaturaAndIdEstablecimiento(
                        relacion.getIdAsignatura(), idEstablecimiento)
                .ifPresent(asignatura -> dto.setAsignaturaNombre(asignatura.getNombre()));

        docenteRepository.findByIdDocenteAndIdEstablecimiento(
                        relacion.getIdDocente(), idEstablecimiento)
                .ifPresent(docente -> dto.setDocenteNombre(
                        docente.getNombres() + " " + docente.getApellidoPaterno()));

        return dto;
    }

    private void validarDocente(Integer idEstablecimiento, Integer idDocente) {
        if (docenteRepository.findByIdDocenteAndIdEstablecimiento(idDocente, idEstablecimiento).isEmpty()) {
            throw new IllegalArgumentException("Docente no encontrado.");
        }
    }

    private void validarCurso(Integer idEstablecimiento, Integer idCurso) {
        if (cursoRepository.findByIdCursoAndIdEstablecimiento(idCurso, idEstablecimiento).isEmpty()) {
            throw new IllegalArgumentException("Curso no encontrado.");
        }
    }

    private Map<Integer, Integer> mapCursoActivoPorEstudiante(List<Estudiante> estudiantes) {
        if (estudiantes.isEmpty()) {
            return Map.of();
        }

        List<Integer> ids = estudiantes.stream()
                .map(Estudiante::getIdEstudiante)
                .toList();

        return estudianteCursoRepository.findByIdEstudianteInAndEstado(ids, "ACTIVO")
                .stream()
                .collect(Collectors.toMap(
                        EstudianteCurso::getIdEstudiante,
                        EstudianteCurso::getIdCurso,
                        (actual, duplicado) -> actual));
    }

    private EstudianteDTO toEstudianteDTO(Estudiante e) {
        return toEstudianteDTO(e, null);
    }

    private EstudianteDTO toEstudianteDTO(Estudiante e, Integer idCurso) {
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
        dto.setCalle(e.getCalle());
        dto.setNumero(e.getNumero());
        dto.setIdComuna(e.getIdComuna());
        dto.setColegioProcedente(e.getColegioProcedente());
        dto.setFechaMatricula(e.getFechaMatricula());
        dto.setPrioritario(e.getPrioritario());
        dto.setPreferente(e.getPreferente());
        dto.setTieneNee(e.getTieneNee());
        dto.setIdTipoNee(e.getIdTipoNee());
        dto.setEnPie(e.getEnPie());
        dto.setEstado(e.getEstado());
        dto.setIdCurso(idCurso);
        return dto;
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
        dto.setIdDocenteJefe(c.getIdDocenteJefe());
        dto.setNombre(c.getNumero() + "° " + c.getTipoEnsenanza() + " " + c.getLetra().trim());
        return dto;
    }

    private DocenteDTO toDocenteDTO(Docente d) {
        DocenteDTO dto = new DocenteDTO();
        dto.setIdDocente(d.getIdDocente());
        dto.setIdEstablecimiento(d.getIdEstablecimiento());
        dto.setRut(d.getRut());
        dto.setDv(d.getDv());
        dto.setNombres(d.getNombres());
        dto.setApellidoPaterno(d.getApellidoPaterno());
        dto.setApellidoMaterno(d.getApellidoMaterno());
        dto.setNombreCompleto(d.getNombres() + " " + d.getApellidoPaterno() + " " + d.getApellidoMaterno());
        dto.setCorreoElectronico(d.getCorreoElectronico());
        dto.setTelefono(d.getTelefono());
        dto.setEstado(d.getEstado());
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

    public List<SalaDTO> listarSalas(Integer idEstablecimiento) {
        validarEstablecimiento(idEstablecimiento);
        return salaRepository.findByIdEstablecimiento(idEstablecimiento)
                .stream()
                .map(this::toSalaDTO)
                .toList();
    }

    public SalaDTO crearSala(Integer idEstablecimiento, SalaDTO dto) {
        validarEstablecimiento(idEstablecimiento);
        validarDatosSala(dto);

        Sala sala = new Sala();
        sala.setIdEstablecimiento(idEstablecimiento);
        sala.setNumero(dto.getNumero());
        sala.setNombre(dto.getNombre().trim());
        sala.setCapacidad(dto.getCapacidad());
        sala.setTipo(dto.getTipo().trim().toUpperCase());
        sala.setPiso(dto.getPiso());
        sala.setEstado(normalizarEstado(dto.getEstado()));

        return toSalaDTO(salaRepository.save(sala));
    }

    public Optional<SalaDTO> actualizarSala(Integer idEstablecimiento, Integer idSala, SalaDTO dto) {
        return salaRepository.findByIdSalaAndIdEstablecimiento(idSala, idEstablecimiento)
                .map(sala -> {
                    validarDatosSala(dto);
                    sala.setNumero(dto.getNumero());
                    sala.setNombre(dto.getNombre().trim());
                    sala.setCapacidad(dto.getCapacidad());
                    sala.setTipo(dto.getTipo().trim().toUpperCase());
                    sala.setPiso(dto.getPiso());
                    sala.setEstado(normalizarEstado(dto.getEstado()));
                    return toSalaDTO(salaRepository.save(sala));
                });
    }

    public boolean eliminarSala(Integer idEstablecimiento, Integer idSala) {
        Optional<Sala> sala = salaRepository.findByIdSalaAndIdEstablecimiento(idSala, idEstablecimiento);

        if (sala.isEmpty()) {
            return false;
        }

        try {
            salaRepository.delete(sala.get());
            return true;
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException(
                    "No se puede eliminar la sala porque está asociada a horarios.");
        }
    }

    private void validarDatosSala(SalaDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la sala es obligatorio.");
        }
        if (dto.getCapacidad() == null || dto.getCapacidad() < 1) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a cero.");
        }
        if (dto.getTipo() == null || dto.getTipo().isBlank()) {
            throw new IllegalArgumentException("El tipo de sala es obligatorio.");
        }
        if (dto.getPiso() == null) {
            throw new IllegalArgumentException("El piso es obligatorio.");
        }
    }

    private SalaDTO toSalaDTO(Sala s) {
        SalaDTO dto = new SalaDTO();
        dto.setIdSala(s.getIdSala());
        dto.setIdEstablecimiento(s.getIdEstablecimiento());
        dto.setNumero(s.getNumero());
        dto.setNombre(s.getNombre());
        dto.setCapacidad(s.getCapacidad());
        dto.setTipo(s.getTipo());
        dto.setPiso(s.getPiso());
        dto.setEstado(s.getEstado());
        return dto;
    }

    public List<RegionDTO> listarRegiones() {
        List<Region> regiones = regionRepository.findAll();
        List<Comuna> comunas = comunaRepository.findAll();
        Map<Integer, List<ComunaDTO>> comunasPorRegion = comunas.stream()
                .collect(Collectors.groupingBy(
                        Comuna::getIdRegion,
                        Collectors.mapping(this::toComunaDTO, Collectors.toList())
                ));
        return regiones.stream().map(r -> {
            RegionDTO dto = new RegionDTO();
            dto.setIdRegion(r.getIdRegion());
            dto.setNombreRegion(r.getNombreRegion());
            dto.setComunas(comunasPorRegion.getOrDefault(r.getIdRegion(), List.of()));
            return dto;
        }).toList();
    }

    public List<ComunaDTO> listarComunas() {
        return comunaRepository.findAll().stream().map(this::toComunaDTO).toList();
    }

    private ComunaDTO toComunaDTO(Comuna c) {
        ComunaDTO dto = new ComunaDTO();
        dto.setIdComuna(c.getIdComuna());
        dto.setNombreComuna(c.getNombreComuna());
        dto.setIdRegion(c.getIdRegion());
        return dto;
    }
}
