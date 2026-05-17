package com.colegio.bff.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    private final String SECRET =
            "colegio-bernardo-ohiggins-secret-key-2026";

    private final long EXPIRATION = 10800000;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(
                SECRET.getBytes(StandardCharsets.UTF_8)
        );
    }

    // Genera el token JWT
    public String generarToken(String username,
                               String rbd,
                               String rol) {
        return Jwts.builder()
                .subject(username)
                .claim("rbd", rbd)
                .claim("rol", rol)
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis() + EXPIRATION
                ))
                .signWith(getKey())
                .compact();
    }

    // Valida si el token es correcto
    public boolean validarToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Obtiene el username del token
    public String obtenerUsername(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}