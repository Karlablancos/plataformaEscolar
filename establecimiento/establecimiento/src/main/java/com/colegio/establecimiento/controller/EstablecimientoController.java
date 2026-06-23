package com.colegio.establecimiento.controller;

import com.colegio.establecimiento.dto.AsignaturaDTO;
import com.colegio.establecimiento.dto.CursoDTO;
import com.colegio.establecimiento.dto.EstablecimientoDTO;
import com.colegio.establecimiento.dto.EstudianteDTO;
import com.colegio.establecimiento.service.EstablecimientoService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/{id}/cursos")
    public ResponseEntity<List<CursoDTO>> listarCursos(@PathVariable Integer id) {
        return ResponseEntity.ok(establecimientoService.listarCursos(id));
    }

    @GetMapping("/{id}/asignaturas")
    public ResponseEntity<List<AsignaturaDTO>> listarAsignaturas(@PathVariable Integer id) {
        return ResponseEntity.ok(establecimientoService.listarAsignaturas(id));
    }

    @GetMapping("/{id}/estudiantes")
    public ResponseEntity<List<EstudianteDTO>> listarEstudiantes(@PathVariable Integer id) {
        return ResponseEntity.ok(establecimientoService.listarEstudiantes(id));
    }
}
