package com.colegio.usuario.controller;

import com.colegio.usuario.dto.ActualizarUsuarioRequest;
import com.colegio.usuario.dto.CambiarPasswordRequest;
import com.colegio.usuario.dto.CrearUsuarioRequest;
import com.colegio.usuario.dto.LoginRequest;
import com.colegio.usuario.dto.UsuarioDTO;
import com.colegio.usuario.model.Rol;
import com.colegio.usuario.service.UsuarioService;
import com.colegio.usuario.dto.CambiarEstadoUsuarioRequest;
import com.colegio.usuario.dto.EliminarUsuarioResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/roles")
    public ResponseEntity<List<Rol>> listarRoles() {
        return ResponseEntity.ok(usuarioService.listarRoles());
    }

    @PostMapping("/roles")
    public ResponseEntity<Rol> crearRol(@RequestBody Map<String, String> body) {
        String nombre = body.get("nombreRol");
        String descripcion = body.get("descripcion");
        if (nombre == null || nombre.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.crearRol(nombre, descripcion));
    }

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
    public ResponseEntity<EliminarUsuarioResponse> eliminar(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.eliminar(id));
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
