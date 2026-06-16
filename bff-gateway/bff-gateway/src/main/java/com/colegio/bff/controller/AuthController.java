package com.colegio.bff.controller;

import com.colegio.bff.dto.LoginRequest;
import com.colegio.bff.dto.LoginResponse;
import com.colegio.bff.security.JwtUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final WebClient webClient;

    @Value("${microservicios.usuario-url}")
    private String usuarioUrl;

    @Value("${microservicios.establecimiento-url}")
    private String establecimientoUrl;

    public AuthController(JwtUtil jwtUtil, WebClient.Builder builder) {
        this.jwtUtil = jwtUtil;
        this.webClient = builder.build();
    }

    @CircuitBreaker(name = "establecimiento-service", fallbackMethod = "loginFallback")
    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> login(@RequestBody LoginRequest request) {

        if (request.getRbd() == null || request.getUsername() == null || request.getPassword() == null) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        return webClient.get()
                .uri(establecimientoUrl + "/establecimiento/rbd/" + request.getRbd())
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(estab -> {
                    // cast seguro: Jackson puede deserializar enteros JSON como Integer o Long
                    Integer idEstablecimiento = ((Number) estab.get("idEstablecimiento")).intValue();
                    String nombreEstab = (String) estab.get("nombre");

                    return webClient.post()
                            .uri(usuarioUrl + "/usuarios/login")
                            .bodyValue(Map.of(
                                    "username", request.getUsername(),
                                    "password", request.getPassword(),
                                    "idEstablecimiento", idEstablecimiento))
                            .retrieve()
                            .onStatus(status -> status.value() == 401,
                                    response -> Mono
                                            .error(new RuntimeException("Credenciales inválidas o usuario inactivo")))
                            .bodyToMono(Map.class)
                            .map(usuario -> {
                                // Intenta obtener el nombre del rol devuelto por usuario-service
                                String rolBD = usuario.get("nombreRol") != null
                                        ? (String) usuario.get("nombreRol")
                                        : null;
                                // Fallback por idRol si findNombreRolById devolvió null
                                String rol;
                                if (rolBD != null) {
                                    rol = normalizarRol(rolBD);
                                } else {
                                    Integer idRol = usuario.get("idRol") != null
                                            ? ((Number) usuario.get("idRol")).intValue() : 1;
                                    rol = normalizarRolPorId(idRol);
                                }
                                // Usar el id_establecimiento del usuario autenticado
                                Integer idEstabUsuario = usuario.get("idEstablecimiento") != null
                                        ? ((Number) usuario.get("idEstablecimiento")).intValue()
                                        : idEstablecimiento;
                                String token = jwtUtil.generarToken(
                                        request.getUsername(),
                                        request.getRbd(),
                                        rol);
                                LoginResponse response = new LoginResponse(
                                        token,
                                        request.getUsername(),
                                        rol,
                                        nombreEstab,
                                        request.getRbd(),
                                        idEstabUsuario);
                                return ResponseEntity.ok(response);
                            })
                            .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
                });
    }

    private String normalizarRol(String nombreRol) {
        if (nombreRol == null) return "ADMIN";
        return switch (nombreRol.toUpperCase()) {
            case "ADMINISTRADOR" -> "ADMIN";
            case "DOCENTE"       -> "PROFESOR";
            case "SOSTENEDOR"    -> "SOSTENEDOR";
            default              -> nombreRol.toUpperCase();
        };
    }

    // Fallback cuando findNombreRolById devuelve null — mapea directamente por id_rol
    private String normalizarRolPorId(Integer idRol) {
        return switch (idRol) {
            case 1 -> "ADMIN";
            case 2 -> "PROFESOR";
            case 5 -> "SOSTENEDOR";
            default -> "APODERADO";
        };
    }

    public Mono<ResponseEntity<LoginResponse>> loginFallback(LoginRequest request, Throwable t) {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
    }

    @CircuitBreaker(name = "establecimiento-service", fallbackMethod = "validarRbdFallback")
    @GetMapping("/validar-rbd/{rbd}")
    public Mono<ResponseEntity<Map<String, Object>>> validarRbd(@PathVariable String rbd) {
        return webClient.get()
                .uri(establecimientoUrl + "/establecimiento/rbd/" + rbd)
                .retrieve()
                .bodyToMono(Map.class)
                .map(e -> {
                    Map<String, Object> result = Map.of(
                            "idEstablecimiento", e.get("idEstablecimiento"),
                            "nombre", e.get("nombre"));
                    return ResponseEntity.ok(result);
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public Mono<ResponseEntity<Map<String, Object>>> validarRbdFallback(String rbd, Throwable t) {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
    }
}
