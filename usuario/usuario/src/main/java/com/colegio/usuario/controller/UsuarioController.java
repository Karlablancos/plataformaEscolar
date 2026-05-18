package com.colegio.usuario.controller;

import com.colegio.usuario.dto.CrearUsuarioRequest;
import com.colegio.usuario.dto.LoginRequest;
import com.colegio.usuario.dto.UsuarioDTO;
import com.colegio.usuario.dto.ActualizarUsuarioRequest;
import com.colegio.usuario.service.UsuarioService;
import com.colegio.usuario.dto.CambiarEstadoUsuarioRequest;

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

    @PatchMapping("/{id}/estado")
    public ResponseEntity<UsuarioDTO> cambiarEstadoUsuario(
            @PathVariable Integer id,
            @RequestBody CambiarEstadoUsuarioRequest request
    ) {
        UsuarioDTO usuarioActualizado = usuarioService.cambiarEstadoUsuario(id, request.getEstado());
        return ResponseEntity.ok(usuarioActualizado);
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> crear(@RequestBody CrearUsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.crear(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(
            @PathVariable Integer id,
            @RequestBody ActualizarUsuarioRequest request
    ) {
        UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id, request);
        return ResponseEntity.ok(usuarioActualizado);
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
