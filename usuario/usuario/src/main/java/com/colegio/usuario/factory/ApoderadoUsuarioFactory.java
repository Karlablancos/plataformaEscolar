package com.colegio.usuario.factory;

import com.colegio.usuario.dto.CrearUsuarioRequest;
import com.colegio.usuario.model.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

// Roles 3 (Apoderado) y 4 (Apoderado Titular) — inician en estado PENDIENTE
// hasta que un administrador los active manualmente.
@Component
public class ApoderadoUsuarioFactory implements UsuarioFactory {

    @Override
    public int getIdRol() {
        return 3;
    }

    @Override
    public Usuario crearUsuario(CrearUsuarioRequest request, PasswordEncoder encoder) {
        Usuario u = new Usuario();
        u.setUsername(request.getUsername());
        u.setPasswordHash(encoder.encode(request.getPassword()));
        u.setIdEstablecimiento(request.getIdEstablecimiento());
        u.setIdRol(request.getIdRol());
        u.setCorreoElectronico(request.getCorreoElectronico());
        u.setEstado("PENDIENTE");
        u.setBloqueado(false);
        u.setIntentosFallidos(0);
        u.setFechaCreacion(LocalDateTime.now());
        return u;
    }
}
