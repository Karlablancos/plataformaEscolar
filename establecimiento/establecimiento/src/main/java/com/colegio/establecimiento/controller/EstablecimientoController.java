package com.colegio.establecimiento.controller;

import com.colegio.establecimiento.dto.AsignaturaDTO;
import com.colegio.establecimiento.dto.ComunaDTO;
import com.colegio.establecimiento.dto.CursoAsignaturaDTO;
import com.colegio.establecimiento.dto.CursoAsignaturaRequestDTO;
import com.colegio.establecimiento.dto.CursoDTO;
import com.colegio.establecimiento.dto.DocenteDTO;
import com.colegio.establecimiento.dto.EstablecimientoDTO;
import com.colegio.establecimiento.dto.EstudianteDTO;
import com.colegio.establecimiento.dto.ProfesorJefeRequestDTO;
import com.colegio.establecimiento.dto.RegionDTO;
import com.colegio.establecimiento.dto.SalaDTO;
import com.colegio.establecimiento.dto.TipoCalificacionDTO;
import com.colegio.establecimiento.service.EstablecimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/establecimiento")
@RequiredArgsConstructor
public class EstablecimientoController {

    private final EstablecimientoService establecimientoService;

    @GetMapping
    public ResponseEntity<List<EstablecimientoDTO>> listarTodos(
            @RequestParam(required = false) Integer idEstablecimiento) {
        return ResponseEntity.ok(establecimientoService.listarTodos(idEstablecimiento));
    }

    @GetMapping("/rbd/{rbd}")
    public ResponseEntity<EstablecimientoDTO> buscarPorRbd(@PathVariable String rbd) {
        return establecimientoService.buscarPorRbd(rbd)
                .map(e -> {
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
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/existe/{rbd}")
    public ResponseEntity<Boolean> existeRbd(@PathVariable String rbd) {
        return ResponseEntity.ok(establecimientoService.existeRbd(rbd));
    }

    @GetMapping("/tipos-calificacion")
    public ResponseEntity<List<TipoCalificacionDTO>> listarTiposCalificacion() {
        return ResponseEntity.ok(establecimientoService.listarTiposCalificacion());
    }

    @GetMapping("/{id}/cursos")
    public ResponseEntity<List<CursoDTO>> listarCursos(@PathVariable Integer id) {
        return ResponseEntity.ok(establecimientoService.listarCursos(id));
    }

    @PostMapping("/{id}/cursos")
    public ResponseEntity<CursoDTO> crearCurso(
            @PathVariable Integer id,
            @RequestBody CursoDTO dto) {
        return ResponseEntity.ok(establecimientoService.crearCurso(id, dto));
    }

    @PutMapping("/{id}/cursos/{idCurso}")
    public ResponseEntity<CursoDTO> actualizarCurso(
            @PathVariable Integer id,
            @PathVariable Integer idCurso,
            @RequestBody CursoDTO dto) {
        return establecimientoService.actualizarCurso(id, idCurso, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/cursos/{idCurso}")
    public ResponseEntity<Void> eliminarCurso(
            @PathVariable Integer id,
            @PathVariable Integer idCurso) {
        if (!establecimientoService.eliminarCurso(id, idCurso)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/asignaturas")
    public ResponseEntity<List<AsignaturaDTO>> listarAsignaturas(@PathVariable Integer id) {
        return ResponseEntity.ok(establecimientoService.listarAsignaturas(id));
    }

    @PostMapping("/{id}/asignaturas")
    public ResponseEntity<AsignaturaDTO> crearAsignatura(
            @PathVariable Integer id,
            @RequestBody AsignaturaDTO dto) {
        return ResponseEntity.ok(establecimientoService.crearAsignatura(id, dto));
    }

    @PutMapping("/{id}/asignaturas/{idAsignatura}")
    public ResponseEntity<AsignaturaDTO> actualizarAsignatura(
            @PathVariable Integer id,
            @PathVariable Integer idAsignatura,
            @RequestBody AsignaturaDTO dto) {
        return establecimientoService.actualizarAsignatura(id, idAsignatura, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/asignaturas/{idAsignatura}")
    public ResponseEntity<Void> eliminarAsignatura(
            @PathVariable Integer id,
            @PathVariable Integer idAsignatura) {
        if (!establecimientoService.eliminarAsignatura(id, idAsignatura)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/estudiantes")
    public ResponseEntity<List<EstudianteDTO>> listarEstudiantes(@PathVariable Integer id) {
        return ResponseEntity.ok(establecimientoService.listarEstudiantes(id));
    }

    @PostMapping("/{id}/estudiantes")
    public ResponseEntity<EstudianteDTO> crearEstudiante(
            @PathVariable Integer id,
            @RequestBody EstudianteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(establecimientoService.crearEstudiante(id, dto));
    }

    @GetMapping("/{id}/cursos/{idCurso}/estudiantes")
    public ResponseEntity<List<EstudianteDTO>> listarEstudiantesPorCurso(
            @PathVariable Integer id,
            @PathVariable Integer idCurso) {
        return ResponseEntity.ok(establecimientoService.listarEstudiantesPorCurso(id, idCurso));
    }

    @PostMapping("/{id}/cursos/{idCurso}/estudiantes/{idEstudiante}")
    public ResponseEntity<EstudianteDTO> matricularEstudiante(
            @PathVariable Integer id,
            @PathVariable Integer idCurso,
            @PathVariable Integer idEstudiante) {
        return ResponseEntity.ok(
                establecimientoService.matricularEstudiante(id, idCurso, idEstudiante));
    }

    @DeleteMapping("/{id}/cursos/{idCurso}/estudiantes/{idEstudiante}")
    public ResponseEntity<Void> desmatricularEstudiante(
            @PathVariable Integer id,
            @PathVariable Integer idCurso,
            @PathVariable Integer idEstudiante) {
        if (!establecimientoService.desmatricularEstudiante(id, idCurso, idEstudiante)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/docentes")
    public ResponseEntity<List<DocenteDTO>> listarDocentes(@PathVariable Integer id) {
        return ResponseEntity.ok(establecimientoService.listarDocentes(id));
    }

    @PutMapping("/{id}/cursos/{idCurso}/profesor-jefe")
    public ResponseEntity<CursoDTO> asignarProfesorJefe(
            @PathVariable Integer id,
            @PathVariable Integer idCurso,
            @RequestBody ProfesorJefeRequestDTO request) {
        return establecimientoService.asignarProfesorJefe(
                        id, idCurso, request != null ? request.getIdDocente() : null)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/cursos/{idCurso}/asignaturas")
    public ResponseEntity<List<CursoAsignaturaDTO>> listarAsignaturasCurso(
            @PathVariable Integer id,
            @PathVariable Integer idCurso) {
        return ResponseEntity.ok(establecimientoService.listarAsignaturasCurso(id, idCurso));
    }

    @PostMapping("/{id}/cursos/{idCurso}/asignaturas")
    public ResponseEntity<CursoAsignaturaDTO> asignarAsignaturaCurso(
            @PathVariable Integer id,
            @PathVariable Integer idCurso,
            @RequestBody CursoAsignaturaRequestDTO request) {
        return ResponseEntity.ok(
                establecimientoService.asignarAsignaturaCurso(id, idCurso, request));
    }

    @DeleteMapping("/{id}/cursos/{idCurso}/asignaturas/{idAsignatura}")
    public ResponseEntity<Void> quitarAsignaturaCurso(
            @PathVariable Integer id,
            @PathVariable Integer idCurso,
            @PathVariable Integer idAsignatura) {
        if (!establecimientoService.quitarAsignaturaCurso(id, idCurso, idAsignatura)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/salas")
    public ResponseEntity<List<SalaDTO>> listarSalas(@PathVariable Integer id) {
        return ResponseEntity.ok(establecimientoService.listarSalas(id));
    }

    @PostMapping("/{id}/salas")
    public ResponseEntity<SalaDTO> crearSala(
            @PathVariable Integer id,
            @RequestBody SalaDTO dto) {
        return ResponseEntity.ok(establecimientoService.crearSala(id, dto));
    }

    @PutMapping("/{id}/salas/{idSala}")
    public ResponseEntity<SalaDTO> actualizarSala(
            @PathVariable Integer id,
            @PathVariable Integer idSala,
            @RequestBody SalaDTO dto) {
        return establecimientoService.actualizarSala(id, idSala, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/salas/{idSala}")
    public ResponseEntity<Void> eliminarSala(
            @PathVariable Integer id,
            @PathVariable Integer idSala) {
        if (!establecimientoService.eliminarSala(id, idSala)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/regiones")
    public ResponseEntity<List<RegionDTO>> listarRegiones() {
        return ResponseEntity.ok(establecimientoService.listarRegiones());
    }

    @GetMapping("/comunas")
    public ResponseEntity<List<ComunaDTO>> listarComunas() {
        return ResponseEntity.ok(establecimientoService.listarComunas());
    }
}
