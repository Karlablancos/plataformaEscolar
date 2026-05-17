package com.colegio.bff.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import java.util.List;

@Component
public class JwtFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    // Rutas públicas — no necesitan token
    private final List<String> RUTAS_PUBLICAS = List.of(
            "/auth/login",
            "/auth/validar-rbd"
    );

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             WebFilterChain chain) {

        String ruta = exchange.getRequest()
                .getPath()
                .toString();

        // Si es ruta pública deja pasar sin token
        if (RUTAS_PUBLICAS.stream()
                .anyMatch(ruta::startsWith)) {
            return chain.filter(exchange);
        }

        // Busca el token en el header
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        // Si no hay token o es inválido rechaza
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {
            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validarToken(token)) {
            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}