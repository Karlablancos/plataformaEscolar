package com.colegio.asistencia.controller;

import com.colegio.asistencia.dto.AsistenciaDTO;
import com.colegio.asistencia.dto.ResumenAsistenciaDTO;
import com.colegio.asistencia.service.AsistenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/asistencia")
public class AsistenciaController {

    private final AsistenciaService service;

    public AsistenciaController(AsistenciaService service) {
        this.service = service;
    }

    // GET /asistencia/estudiante/5
    @GetMapping("/estudiante/{idEstudiante}")
    public ResponseEntity<List<AsistenciaDTO>> obtenerPorEstudiante(
            @PathVariable Integer idEstudiante) {
        return ResponseEntity.ok(
                service.obtenerPorEstudiante(idEstudiante)
        );
    }

    // GET /asistencia/estudiante/5/fecha/2026-05-14
    @GetMapping("/estudiante/{idEstudiante}/fecha/{fecha}")
    public ResponseEntity<List<AsistenciaDTO>> obtenerPorFecha(
            @PathVariable Integer idEstudiante,
            @PathVariable LocalDate fecha) {
        return ResponseEntity.ok(
                service.obtenerPorEstudianteYFecha(idEstudiante, fecha)
        );
    }

    // GET /asistencia/estudiante/5/resumen
    @GetMapping("/estudiante/{idEstudiante}/resumen")
    public ResponseEntity<ResumenAsistenciaDTO> obtenerResumen(
            @PathVariable Integer idEstudiante) {
        return ResponseEntity.ok(
                service.calcularResumen(idEstudiante)
        );
    }

    // POST /asistencia
    @PostMapping
    public ResponseEntity<AsistenciaDTO> registrar(
            @RequestBody AsistenciaDTO dto) {
        return ResponseEntity.ok(service.registrar(dto));
    }
}