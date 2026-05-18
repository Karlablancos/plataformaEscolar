package com.colegio.usuario.factory;

import com.colegio.usuario.dto.CrearUsuarioRequest;
import com.colegio.usuario.model.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProfesorUsuarioFactory implements UsuarioFactory {

    @Override
    public int getIdRol() {
        return 2;
    }

    @Override
    public Usuario crearUsuario(CrearUsuarioRequest request, PasswordEncoder encoder) {
        Usuario u = new Usuario();
        u.setUsername(request.getUsername());
        u.setPasswordHash(encoder.encode(request.getPassword()));
        u.setIdEstablecimiento(request.getIdEstablecimiento());
        u.setIdRol(2);
        u.setCorreoElectronico(request.getCorreoElectronico());
        u.setEstado("ACTIVO");
        u.setBloqueado(false);
        u.setIntentosFallidos(0);
        u.setFechaCreacion(LocalDateTime.now());
        return u;
    }
}
