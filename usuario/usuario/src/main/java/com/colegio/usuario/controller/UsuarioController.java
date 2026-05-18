package com.colegio.usuario.controller;

import com.colegio.usuario.dto.ActualizarUsuarioRequest;
import com.colegio.usuario.dto.CambiarPasswordRequest;
import com.colegio.usuario.dto.CrearUsuarioRequest;
import com.colegio.usuario.dto.LoginRequest;
import com.colegio.usuario.dto.UsuarioDTO;
import com.colegio.usuario.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listar(
            @RequestParam(required = false) Integer idEstablecimiento) {
        if (idEstablecimiento != null) {
            return ResponseEntity.ok(usuarioService.listarPorEstablecimiento(idEstablecimiento));
        }
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Integer id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> crear(@RequestBody CrearUsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizar(
            @PathVariable Integer id,
            @RequestBody ActualizarUsuarioRequest request) {
        return usuarioService.actualizar(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> cambiarPassword(
            @PathVariable Integer id,
            @RequestBody CambiarPasswordRequest request) {
        boolean ok = usuarioService.cambiarPassword(id, request);
        return ok ? ResponseEntity.noContent().build()
                  : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioDTO> login(@RequestBody LoginRequest loginRequest) {
        return usuarioService
                .login(loginRequest.getUsername(),
                        loginRequest.getPassword(),
                        loginRequest.getIdEstablecimiento())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
