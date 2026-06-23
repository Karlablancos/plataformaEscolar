package com.colegio.establecimiento.controller;

import com.colegio.establecimiento.dto.AnotacionDTO;
import com.colegio.establecimiento.service.AnotacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/anotaciones")
@RequiredArgsConstructor
public class AnotacionController {

    private final AnotacionService anotacionService;

    @GetMapping("/estudiante/{idEstudiante}")
    public ResponseEntity<List<AnotacionDTO>> listarPorEstudiante(@PathVariable Integer idEstudiante) {
        return ResponseEntity.ok(anotacionService.listarPorEstudiante(idEstudiante));
    }

    @GetMapping("/docente/{idDocente}")
    public ResponseEntity<List<AnotacionDTO>> listarPorDocente(@PathVariable Integer idDocente) {
        return ResponseEntity.ok(anotacionService.listarPorDocente(idDocente));
    }

    @GetMapping("/citaciones-pendientes")
    public ResponseEntity<List<AnotacionDTO>> listarCitacionesPendientes() {
        return ResponseEntity.ok(anotacionService.listarCitacionesPendientes());
    }

    @PostMapping
    public ResponseEntity<AnotacionDTO> crear(@RequestBody AnotacionDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(anotacionService.crear(request));
    }

    @PutMapping("/{idAnotacion}")
    public ResponseEntity<AnotacionDTO> actualizar(
            @PathVariable Integer idAnotacion,
            @RequestBody AnotacionDTO request) {
        return anotacionService.actualizar(idAnotacion, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{idAnotacion}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idAnotacion) {
        anotacionService.eliminar(idAnotacion);
        return ResponseEntity.noContent().build();
    }
}