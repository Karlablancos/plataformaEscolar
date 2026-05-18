package com.colegio.usuario.factory;

import com.colegio.usuario.dto.CrearUsuarioRequest;
import com.colegio.usuario.model.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UsuarioFactory {
    int getIdRol();
    Usuario crearUsuario(CrearUsuarioRequest request, PasswordEncoder encoder);
}
