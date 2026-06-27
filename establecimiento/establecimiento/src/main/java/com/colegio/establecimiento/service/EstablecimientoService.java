package com.colegio.establecimiento.service;

import com.colegio.establecimiento.dto.AsignaturaDTO;
import com.colegio.establecimiento.dto.ComunaDTO;
import com.colegio.establecimiento.dto.CursoAsignaturaDTO;
import com.colegio.establecimiento.dto.CursoAsignaturaRequestDTO;
import com.colegio.establecimiento.dto.CursoDTO;
import com.colegio.establecimiento.dto.DocenteDTO;
import com.colegio.establecimiento.dto.EstablecimientoDTO;
import com.colegio.establecimiento.dto.EstudianteCreateRequestDTO;
import com.colegio.establecimiento.dto.EstudianteDTO;
import com.colegio.establecimiento.dto.PeriodoAcademicoDTO;
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
import com.colegio.establecimiento.model.Rol;
import com.colegio.establecimiento.model.Usuario;
import com.colegio.establecimiento.model.Horario;
import com.colegio.establecimiento.model.PeriodoAcademico;
import com.colegio.establecimiento.model.Region;
import com.colegio.establecimiento.model.Sala;
import com.colegio.establecimiento.model.TipoCalificacion;
import com.colegio.establecimiento.repository.AsignaturaRepository;
import com.colegio.establecimiento.repository.ComunaRepository;
import com.colegio.establecimiento.repository.CursoAsignaturaRepository;
import com.colegio.establecimiento.repository.CursoRepository;
import com.colegio.establecimiento.repository.DocenteRepository;
import com.colegio.establecimiento.repository.EstablecimientoRepository;
import com.colegio.establecimiento.repository.EstudianteCursoRepository;
import com.colegio.establecimiento.repository.EstudianteRepository;
import com.colegio.establecimiento.repository.RolRepository;
import com.colegio.establecimiento.repository.UsuarioRepository;
import com.colegio.establecimiento.repository.HorarioRepository;
import com.colegio.establecimiento.repository.PeriodoAcademicoRepository;
import com.colegio.establecimiento.repository.RegionRepository;
import com.colegio.establecimiento.repository.SalaRepository;
import com.colegio.establecimiento.repository.TipoCalificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstablecimientoService {

    private static final String DEFAULT_PASSWORD_HASH =
            "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi";

    private final EstablecimientoRepository establecimientoRepository;
    private final CursoRepository cursoRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final EstudianteRepository estudianteRepository;
    private final EstudianteCursoRepository estudianteCursoRepository;
    private final DocenteRepository docenteRepository;
    private final CursoAsignaturaRepository cursoAsignaturaRepository;
    private final PeriodoAcademicoRepository periodoAcademicoRepository;
    private final TipoCalificacionRepository tipoCalificacionRepository;
    private final RegionRepository regionRepository;
    private final ComunaRepository comunaRepository;
    private final SalaRepository salaRepository;
    private final HorarioRepository horarioRepository;
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
        return cursoRepository.findByIdEstablecimientoOrderByIdCursoDesc(idEstablecimiento)
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

    @Transactional
    public EstudianteDTO crearEstudiante(
            Integer idEstablecimiento, EstudianteCreateRequestDTO request) {
        validarEstablecimiento(idEstablecimiento);

        if (request == null || request.getNombres() == null || request.getNombres().isBlank()) {
            throw new IllegalArgumentException("Los nombres del estudiante son obligatorios.");
        }
        if (request.getApellidoPaterno() == null || request.getApellidoPaterno().isBlank()) {
            throw new IllegalArgumentException("El apellido paterno es obligatorio.");
        }
        if (request.getRut() == null || request.getRut().isBlank()) {
            throw new IllegalArgumentException("El RUT es obligatorio.");
        }

        RutPart rutPart = parseRut(request.getRut());
        Integer idUsuario = crearUsuarioEstudiante(idEstablecimiento, rutPart, request);

        Estudiante estudiante = new Estudiante();
        estudiante.setIdEstablecimiento(idEstablecimiento);
        estudiante.setIdUsuario(idUsuario);
        estudiante.setRut(rutPart.rut());
        estudiante.setDv(rutPart.dv());
        estudiante.setNombres(request.getNombres().trim());
        estudiante.setApellidoPaterno(request.getApellidoPaterno().trim());
        estudiante.setApellidoMaterno(
                request.getApellidoMaterno() != null ? request.getApellidoMaterno().trim() : "");
        estudiante.setFechaNacimiento(
                request.getFechaNacimiento() != null
                        ? request.getFechaNacimiento()
                        : LocalDate.of(2010, 1, 1));
        estudiante.setCorreoElectronico(
                valorODefault(request.getCorreoElectronico(), "sin-correo@bohiggins.cl"));
        estudiante.setTelefono(valorODefault(request.getTelefono(), "000000000"));
        estudiante.setCalle(valorODefault(request.getCalle(), "Sin calle"));
        estudiante.setNumero(valorODefault(request.getNumero(), "S/N"));
        estudiante.setIdComuna(request.getIdComuna() != null ? request.getIdComuna() : 12);
        estudiante.setColegioProcedente(
                valorODefault(request.getColegioProcedente(), "Sin información"));
        estudiante.setFechaMatricula(
                request.getFechaMatricula() != null ? request.getFechaMatricula() : LocalDate.now());
        estudiante.setPrioritario(Boolean.TRUE.equals(request.getPrioritario()));
        estudiante.setPreferente(Boolean.TRUE.equals(request.getPreferente()));
        estudiante.setTieneNee(Boolean.TRUE.equals(request.getTieneNee()));
        estudiante.setIdTipoNee(
                Boolean.TRUE.equals(request.getTieneNee()) ? request.getIdTipoNee() : null);
        estudiante.setEnPie(Boolean.TRUE.equals(request.getEnPie()));
        estudiante.setEstado(normalizarEstadoEstudiante(request.getEstado()));

        Estudiante guardado;
        try {
            guardado = estudianteRepository.save(estudiante);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("No se pudo registrar el estudiante. Verifica RUT y comuna.");
        }

        if (request.getIdCurso() != null) {
            return matricularEstudiante(
                    idEstablecimiento, request.getIdCurso(), guardado.getIdEstudiante());
        }

        return toEstudianteDTO(guardado, null);
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

    public List<PeriodoAcademicoDTO> listarPeriodos(Integer idEstablecimiento, Integer anio) {
        validarEstablecimiento(idEstablecimiento);

        return periodoAcademicoRepository
                .findByIdEstablecimientoAndAnioOrderByFechaInicioAsc(idEstablecimiento, anio)
                .stream()
                .map(this::sincronizarEstadoPeriodo)
                .map(this::toPeriodoAcademicoDTO)
                .toList();
    }

    public PeriodoAcademicoDTO crearPeriodo(Integer idEstablecimiento, PeriodoAcademicoDTO dto) {
        validarEstablecimiento(idEstablecimiento);
        validarDatosPeriodo(dto);

        PeriodoAcademico periodo = new PeriodoAcademico();
        periodo.setIdEstablecimiento(idEstablecimiento);
        periodo.setAnio(dto.getAnio());
        periodo.setNombrePeriodo(dto.getNombrePeriodo().trim());
        periodo.setFechaInicio(dto.getFechaInicio());
        periodo.setFechaTermino(dto.getFechaTermino());
        periodo.setEstado(calcularEstadoPeriodo(dto.getFechaInicio(), dto.getFechaTermino()));

        return toPeriodoAcademicoDTO(periodoAcademicoRepository.save(periodo));
    }

    public Optional<PeriodoAcademicoDTO> actualizarPeriodo(
            Integer idEstablecimiento, Integer idPeriodo, PeriodoAcademicoDTO dto) {
        return periodoAcademicoRepository.findByIdPeriodoAndIdEstablecimiento(idPeriodo, idEstablecimiento)
                .map(periodo -> {
                    validarDatosPeriodo(dto);
                    periodo.setAnio(dto.getAnio());
                    periodo.setNombrePeriodo(dto.getNombrePeriodo().trim());
                    periodo.setFechaInicio(dto.getFechaInicio());
                    periodo.setFechaTermino(dto.getFechaTermino());
                    periodo.setEstado(calcularEstadoPeriodo(dto.getFechaInicio(), dto.getFechaTermino()));
                    return toPeriodoAcademicoDTO(periodoAcademicoRepository.save(periodo));
                });
    }

    public List<CursoAsignaturaDTO> listarAsignaturasCurso(
            Integer idEstablecimiento, Integer idCurso, Integer idPeriodo) {
        validarCurso(idEstablecimiento, idCurso);

        List<CursoAsignatura> relaciones = idPeriodo != null
                ? cursoAsignaturaRepository.findByIdCursoAndIdPeriodoAndEstado(idCurso, idPeriodo, "ACTIVO")
                : cursoAsignaturaRepository.findByIdCursoAndEstado(idCurso, "ACTIVO");

        return relaciones.stream()
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
        Integer idPeriodo = request.getIdPeriodo();
        Integer horasSemanales = request.getHorasSemanales();
        Integer idSala = request.getIdSala();

        validarAsignatura(idEstablecimiento, idAsignatura);
        validarDocente(idEstablecimiento, idDocente);
        validarPeriodoParaCurso(idEstablecimiento, idCurso, idPeriodo);
        validarSala(idEstablecimiento, idSala);

        Optional<CursoAsignatura> existente = cursoAsignaturaRepository
                .findByIdCursoAndIdAsignaturaAndIdPeriodo(idCurso, idAsignatura, idPeriodo);

        if (existente.isPresent() && "ACTIVO".equals(existente.get().getEstado())) {
            throw new IllegalArgumentException("La asignatura ya está asignada al curso en este periodo.");
        }

        CursoAsignatura relacion = existente.orElseGet(() -> {
            CursoAsignatura nueva = new CursoAsignatura();
            nueva.setIdCurso(idCurso);
            nueva.setIdAsignatura(idAsignatura);
            nueva.setIdPeriodo(idPeriodo);
            return nueva;
        });

        relacion.setIdDocente(idDocente);
        relacion.setHorasSemanales(horasSemanales);
        relacion.setIdPeriodo(idPeriodo);
        relacion.setIdSala(idSala);
        relacion.setEstado("ACTIVO");

        return toCursoAsignaturaDTO(cursoAsignaturaRepository.save(relacion), idEstablecimiento);
    }

    public boolean quitarAsignaturaCurso(
            Integer idEstablecimiento, Integer idCurso, Integer idAsignatura, Integer idPeriodo) {
        if (cursoRepository.findByIdCursoAndIdEstablecimiento(idCurso, idEstablecimiento).isEmpty()) {
            return false;
        }

        if (idPeriodo == null) {
            throw new IllegalArgumentException("El periodo académico es obligatorio.");
        }

        Optional<CursoAsignatura> relacion = cursoAsignaturaRepository
                .findByIdCursoAndIdAsignaturaAndIdPeriodo(idCurso, idAsignatura, idPeriodo);

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
        if (request.getIdPeriodo() == null) {
            throw new IllegalArgumentException("El periodo académico es obligatorio.");
        }
        if (request.getHorasSemanales() == null || request.getHorasSemanales() < 1) {
            throw new IllegalArgumentException("Las horas semanales son obligatorias y deben ser mayores a cero.");
        }
        if (request.getHorasSemanales() > 6) {
            throw new IllegalArgumentException("Las horas semanales no pueden ser mayores a 6.");
        }
        if (request.getIdSala() == null) {
            throw new IllegalArgumentException("La sala de clases es obligatoria.");
        }
    }

    private void validarSala(Integer idEstablecimiento, Integer idSala) {
        if (salaRepository.findByIdSalaAndIdEstablecimiento(idSala, idEstablecimiento).isEmpty()) {
            throw new IllegalArgumentException("Sala de clases no encontrada.");
        }
    }

    private void validarPeriodoParaCurso(Integer idEstablecimiento, Integer idCurso, Integer idPeriodo) {
        Curso curso = cursoRepository.findByIdCursoAndIdEstablecimiento(idCurso, idEstablecimiento)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado."));

        PeriodoAcademico periodo = periodoAcademicoRepository
                .findByIdPeriodoAndIdEstablecimiento(idPeriodo, idEstablecimiento)
                .orElseThrow(() -> new IllegalArgumentException("Periodo académico no encontrado."));

        if (!periodo.getAnio().equals(curso.getAnioAcademico())) {
            throw new IllegalArgumentException("El periodo no corresponde al año académico del curso.");
        }
    }

    private void validarDatosPeriodo(PeriodoAcademicoDTO dto) {
        if (dto == null || dto.getNombrePeriodo() == null || dto.getNombrePeriodo().isBlank()) {
            throw new IllegalArgumentException("El nombre del periodo es obligatorio.");
        }
        if (dto.getAnio() == null) {
            throw new IllegalArgumentException("El año del periodo es obligatorio.");
        }
        if (dto.getFechaInicio() == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria.");
        }
        if (dto.getFechaTermino() == null) {
            throw new IllegalArgumentException("La fecha de término es obligatoria.");
        }
        if (dto.getFechaTermino().isBefore(dto.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de término debe ser posterior a la fecha de inicio.");
        }
    }

    private String calcularEstadoPeriodo(LocalDate fechaInicio, LocalDate fechaTermino) {
        LocalDate hoy = LocalDate.now();
        if (hoy.isBefore(fechaInicio)) {
            return "PENDIENTE";
        }
        if (hoy.isAfter(fechaTermino)) {
            return "CERRADO";
        }
        return "ACTIVO";
    }

    private PeriodoAcademico sincronizarEstadoPeriodo(PeriodoAcademico periodo) {
        String estadoCalculado = calcularEstadoPeriodo(periodo.getFechaInicio(), periodo.getFechaTermino());
        if (!estadoCalculado.equals(periodo.getEstado())) {
            periodo.setEstado(estadoCalculado);
            return periodoAcademicoRepository.save(periodo);
        }
        return periodo;
    }

    private PeriodoAcademicoDTO toPeriodoAcademicoDTO(PeriodoAcademico periodo) {
        PeriodoAcademicoDTO dto = new PeriodoAcademicoDTO();
        dto.setIdPeriodo(periodo.getIdPeriodo());
        dto.setIdEstablecimiento(periodo.getIdEstablecimiento());
        dto.setAnio(periodo.getAnio());
        dto.setNombrePeriodo(periodo.getNombrePeriodo());
        dto.setFechaInicio(periodo.getFechaInicio());
        dto.setFechaTermino(periodo.getFechaTermino());
        dto.setEstado(periodo.getEstado());
        return dto;
    }

    private CursoAsignaturaDTO toCursoAsignaturaDTO(CursoAsignatura relacion, Integer idEstablecimiento) {
        CursoAsignaturaDTO dto = new CursoAsignaturaDTO();
        dto.setIdCursoAsignatura(relacion.getIdCursoAsignatura());
        dto.setIdCurso(relacion.getIdCurso());
        dto.setIdAsignatura(relacion.getIdAsignatura());
        dto.setIdDocente(relacion.getIdDocente());
        dto.setIdPeriodo(relacion.getIdPeriodo());
        dto.setHorasSemanales(relacion.getHorasSemanales());
        dto.setEstado(relacion.getEstado());

        periodoAcademicoRepository.findByIdPeriodoAndIdEstablecimiento(
                        relacion.getIdPeriodo(), idEstablecimiento)
                .ifPresent(periodo -> dto.setNombrePeriodo(periodo.getNombrePeriodo()));

        asignaturaRepository.findByIdAsignaturaAndIdEstablecimiento(
                        relacion.getIdAsignatura(), idEstablecimiento)
                .ifPresent(asignatura -> dto.setAsignaturaNombre(asignatura.getNombre()));

        docenteRepository.findByIdDocenteAndIdEstablecimiento(
                        relacion.getIdDocente(), idEstablecimiento)
                .ifPresent(docente -> dto.setDocenteNombre(
                        docente.getNombres() + " " + docente.getApellidoPaterno()));

        if (relacion.getIdSala() != null) {
            dto.setIdSala(relacion.getIdSala());
            salaRepository.findByIdSalaAndIdEstablecimiento(relacion.getIdSala(), idEstablecimiento)
                    .ifPresent(sala -> dto.setSalaNombre(sala.getNombre()));
        } else {
            horarioRepository.findFirstByIdCursoAndIdAsignaturaAndIdPeriodo(
                            relacion.getIdCurso(), relacion.getIdAsignatura(), relacion.getIdPeriodo())
                    .ifPresent(horario -> {
                        dto.setIdSala(horario.getIdSala());
                        salaRepository.findByIdSalaAndIdEstablecimiento(horario.getIdSala(), idEstablecimiento)
                                .ifPresent(sala -> dto.setSalaNombre(sala.getNombre()));
                    });
        }

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

    private Integer crearUsuarioEstudiante(
            Integer idEstablecimiento, RutPart rutPart, EstudianteCreateRequestDTO request) {
        String username = "alumno." + rutPart.rut();
        if (usuarioRepository.existsByUsernameAndIdEstablecimiento(username, idEstablecimiento)) {
            throw new IllegalArgumentException("Ya existe un usuario asociado a este RUT.");
        }

        Integer idRol = rolRepository.findByNombreRolIgnoreCase("APODERADO")
                .map(Rol::getIdRol)
                .orElseGet(() -> {
                    Rol rol = new Rol();
                    rol.setNombreRol("APODERADO");
                    rol.setDescripcion("Apoderado / acceso estudiante");
                    return rolRepository.save(rol).getIdRol();
                });

        Usuario usuario = new Usuario();
        usuario.setIdEstablecimiento(idEstablecimiento);
        usuario.setIdRol(idRol);
        usuario.setUsername(username);
        usuario.setPasswordHash(DEFAULT_PASSWORD_HASH);
        usuario.setCorreoElectronico(
                valorODefault(request.getCorreoElectronico(), "sin-correo@bohiggins.cl"));
        usuario.setIntentosFallidos(0);
        usuario.setBloqueado(false);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setEstado("ACTIVO");

        return usuarioRepository.save(usuario).getIdUsuario();
    }

    private record RutPart(String rut, String dv) {}

    private RutPart parseRut(String rawRut) {
        String cleaned = rawRut.replaceAll("[^0-9kK]", "");
        if (cleaned.length() < 2) {
            throw new IllegalArgumentException("RUT inválido.");
        }

        String dv = cleaned.substring(cleaned.length() - 1).toUpperCase();
        String rut = cleaned.substring(0, cleaned.length() - 1);
        if (rut.length() > 8) {
            throw new IllegalArgumentException("RUT inválido.");
        }
        while (rut.length() < 8) {
            rut = "0" + rut;
        }

        return new RutPart(rut, dv);
    }

    private String valorODefault(String value, String defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value.trim();
    }

    private String normalizarEstadoEstudiante(String estado) {
        if (estado == null || estado.isBlank()) {
            return "ACTIVO";
        }
        return estado.trim().toUpperCase();
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
        dto.setFechaMatricula(e.getFechaMatricula());
        dto.setPrioritario(e.getPrioritario());
        dto.setPreferente(e.getPreferente());
        dto.setTieneNee(e.getTieneNee());
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
        return salaRepository.findByIdEstablecimientoAndEstadoOrderByNombreAsc(idEstablecimiento, "ACTIVO")
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
