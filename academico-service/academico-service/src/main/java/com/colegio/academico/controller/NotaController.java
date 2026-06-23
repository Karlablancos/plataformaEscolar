package com.colegio.academico.controller;

import com.colegio.academico.dto.NotaDTO;
import com.colegio.academico.service.NotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/notas")
@RequiredArgsConstructor
public class NotaController {

    private final NotaService notaService;

    @PostMapping
    public ResponseEntity<NotaDTO> registrar(@RequestBody NotaDTO dto) {
        return ResponseEntity.ok(notaService.registrarNota(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotaDTO> buscarPorId(@PathVariable Long id) {
        return notaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/promedio")
    public ResponseEntity<Map<String, Object>> calcularPromedio(
            @RequestParam Long idEstudiante,
            @RequestParam Long idAsignatura) {
        BigDecimal promedio = notaService.calcularPromedio(idEstudiante, idAsignatura);
        return ResponseEntity.ok(Map.of(
                "idEstudiante", idEstudiante,
                "idAsignatura", idAsignatura,
                "promedio", promedio));
    }

    @GetMapping("/promocion")
    public ResponseEntity<Map<String, Object>> calcularPromocion(
            @RequestParam Long idEstudiante) {
        String resultado = notaService.calcularPromocion(idEstudiante);
        return ResponseEntity.ok(Map.of(
                "idEstudiante", idEstudiante,
                "resultado", resultado));
    }
}
