package com.colegio.establecimiento.controller;

import com.colegio.establecimiento.dto.EstablecimientoDTO;
import com.colegio.establecimiento.model.Establecimiento;
import com.colegio.establecimiento.service.EstablecimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/establecimiento")
@RequiredArgsConstructor
public class EstablecimientoController {

    private final EstablecimientoService establecimientoService;

    @GetMapping("/rbd/{rbd}")
    public ResponseEntity<EstablecimientoDTO> buscarPorRbd(@PathVariable String rbd) {
        return establecimientoService.buscarPorRbd(rbd)
                .map(e -> {
                    EstablecimientoDTO dto = new EstablecimientoDTO();
                    dto.setIdEstablecimiento(e.getIdEstablecimiento());
                    dto.setRbd(e.getRbd());
                    dto.setNombre(e.getNombre());
                    dto.setSostenedor(e.getSostenedor());
                    dto.setDirector(e.getDirector());
                    dto.setCorreoElectronico(e.getCorreoElectronico());
                    dto.setTelefono(e.getTelefono());
                    dto.setEstado(e.getEstado());
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/existe/{rbd}")
    public ResponseEntity<Boolean> existeRbd(@PathVariable String rbd) {
        return ResponseEntity.ok(establecimientoService.existeRbd(rbd));
    }

}