package com.colegio.bff.controller;

import com.colegio.bff.dto.LoginRequest;
import com.colegio.bff.dto.LoginResponse;
import com.colegio.bff.security.JwtUtil;
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

        public AuthController(JwtUtil jwtUtil, WebClient.Builder builder) {
                this.jwtUtil = jwtUtil;
                this.webClient = builder.build();
        }

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
                                        Integer idEstablecimiento = (Integer) estab.get("idEstablecimiento");
                                        String nombreEstab = (String) estab.get("nombre");

                                        return webClient.post()
                                                        .uri(usuarioUrl + "/usuarios/login")
                                                        .bodyValue(Map.of(
                                                                        "username", request.getUsername(),
                                                                        "password", request.getPassword(),
                                                                        "idEstablecimiento", idEstablecimiento))
                                                        .retrieve()
                                                        .bodyToMono(Map.class)
                                                        .map(usuario -> {
                                                                String token = jwtUtil.generarToken(
                                                                                request.getUsername(),
                                                                                request.getRbd(),
                                                                                "ADMIN");
                                                                LoginResponse response = new LoginResponse(
                                                                                token,
                                                                                request.getUsername(),
                                                                                "ADMIN",
                                                                                nombreEstab,
                                                                                request.getRbd());
                                                                return ResponseEntity.ok(response);
                                                        });
                                })
                                .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

        @Value("${microservicios.establecimiento-url}")
        private String establecimientoUrl;

        @GetMapping("/validar-rbd/{rbd}")
        public Mono<ResponseEntity<String>> validarRbd(
                        @PathVariable String rbd) {
                return webClient.get()
                                .uri(establecimientoUrl + "/establecimiento/rbd/" + rbd)
                                .retrieve()
                                .bodyToMono(Map.class)
                                .map(e -> ResponseEntity.ok((String) e.get("nombre")))
                                .onErrorReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }
}