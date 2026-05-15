package com.colegio.bff.controller;

import com.colegio.bff.dto.LoginRequest;
import com.colegio.bff.dto.LoginResponse;
import com.colegio.bff.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // POST /auth/login
    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> login(
            @RequestBody LoginRequest request) {

        // Validación básica de campos
        if (request.getRbd() == null ||
                request.getUsername() == null ||
                request.getPassword() == null) {
            return Mono.just(
                    ResponseEntity.badRequest().build()
            );
        }

        // Por ahora validamos con datos de prueba
        // En producción llamaría a microservicio-establecimiento
        // y microservicio-usuario
        if (request.getRbd().equals("12345") &&
                request.getUsername().equals("admin") &&
                request.getPassword().equals("1234")) {

            String token = jwtUtil.generarToken(
                    request.getUsername(),
                    request.getRbd(),
                    "ADMIN"
            );

            LoginResponse response = new LoginResponse(
                    token,
                    request.getUsername(),
                    "ADMIN",
                    "Colegio Bernardo O'Higgins",
                    request.getRbd()
            );

            return Mono.just(ResponseEntity.ok(response));
        }

        return Mono.just(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        );
    }

    // GET /auth/validar-rbd/{rbd}
    @GetMapping("/validar-rbd/{rbd}")
    public Mono<ResponseEntity<String>> validarRbd(
            @PathVariable String rbd) {

        // Por ahora solo validamos el RBD de prueba
        if (rbd.equals("12345")) {
            return Mono.just(ResponseEntity.ok(
                    "Colegio Bernardo O'Higgins"
            ));
        }

        return Mono.just(
                ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        );
    }
}