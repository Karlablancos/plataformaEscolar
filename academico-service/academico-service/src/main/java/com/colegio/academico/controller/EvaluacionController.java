package com.colegio.academico.controller;

import com.colegio.academico.dto.EvaluacionDTO;
import com.colegio.academico.service.EvaluacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluacion")
@RequiredArgsConstructor
public class EvaluacionController {

    private final EvaluacionService evaluacionService;

    @PostMapping
    public ResponseEntity<EvaluacionDTO> crear(@RequestBody EvaluacionDTO dto) {
        return ResponseEntity.ok(evaluacionService.crear(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluacionDTO> buscarPorId(@PathVariable Long id) {
        return evaluacionService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/curso/{idCurso}")
    public ResponseEntity<List<EvaluacionDTO>> listarPorCurso(@PathVariable Long idCurso) {
        return ResponseEntity.ok(evaluacionService.listarPorCurso(idCurso));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvaluacionDTO> actualizar(@PathVariable Long id,
                                                     @RequestBody EvaluacionDTO dto) {
        return evaluacionService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        evaluacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
