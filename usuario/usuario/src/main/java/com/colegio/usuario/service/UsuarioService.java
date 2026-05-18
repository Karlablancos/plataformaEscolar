package com.colegio.usuario.service;

import com.colegio.usuario.dto.ActualizarUsuarioRequest;
import com.colegio.usuario.dto.CambiarPasswordRequest;
import com.colegio.usuario.dto.CrearUsuarioRequest;
import com.colegio.usuario.dto.UsuarioDTO;
import com.colegio.usuario.model.Usuario;
import com.colegio.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioDTO> listarPorEstablecimiento(Integer idEstablecimiento) {
        return usuarioRepository.findByIdEstablecimiento(idEstablecimiento)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioDTO> buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .map(this::convertirADTO);
    }

    public Optional<UsuarioDTO> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .map(this::convertirADTO);
    }

    public UsuarioDTO crear(CrearUsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        usuario.setIdEstablecimiento(request.getIdEstablecimiento());
        usuario.setIdRol(request.getIdRol());
        usuario.setCorreoElectronico(request.getCorreoElectronico());
        usuario.setEstado(request.getEstado() != null ? request.getEstado() : "activo");
        usuario.setBloqueado(false);
        usuario.setIntentosFallidos(0);
        usuario.setFechaCreacion(LocalDateTime.now());
        return convertirADTO(usuarioRepository.save(usuario));
    }

    public Optional<UsuarioDTO> actualizar(Integer id, ActualizarUsuarioRequest request) {
        return usuarioRepository.findById(id).map(usuario -> {
            if (request.getIdRol() != null) usuario.setIdRol(request.getIdRol());
            if (request.getCorreoElectronico() != null) usuario.setCorreoElectronico(request.getCorreoElectronico());
            if (request.getBloqueado() != null) usuario.setBloqueado(request.getBloqueado());
            if (request.getEstado() != null) usuario.setEstado(request.getEstado());
            return convertirADTO(usuarioRepository.save(usuario));
        });
    }

    public boolean cambiarPassword(Integer id, CambiarPasswordRequest request) {
        return usuarioRepository.findById(id).map(usuario -> {
            if (!passwordEncoder.matches(request.getPasswordActual(), usuario.getPasswordHash())) {
                return false;
            }
            usuario.setPasswordHash(passwordEncoder.encode(request.getPasswordNueva()));
            usuarioRepository.save(usuario);
            return true;
        }).orElse(false);
    }

    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }

    public boolean existeUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    public Optional<UsuarioDTO> login(String username,
                                      String password,
                                      Integer idEstablecimiento) {
        return usuarioRepository
                .findByUsernameAndIdEstablecimiento(username, idEstablecimiento)
                .filter(usuario -> passwordEncoder.matches(password, usuario.getPasswordHash()))
                .map(this::convertirADTO);
    }

    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setIdEstablecimiento(usuario.getIdEstablecimiento());
        dto.setIdRol(usuario.getIdRol());
        dto.setUsername(usuario.getUsername());
        dto.setCorreoElectronico(usuario.getCorreoElectronico());
        dto.setUltimoAcceso(usuario.getUltimoAcceso());
        dto.setBloqueado(usuario.getBloqueado());
        dto.setEstado(usuario.getEstado());
        return dto;
    }
}
