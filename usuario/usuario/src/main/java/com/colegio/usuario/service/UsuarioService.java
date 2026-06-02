package com.colegio.usuario.service;

import com.colegio.usuario.dto.ActualizarUsuarioRequest;
import com.colegio.usuario.dto.CambiarPasswordRequest;
import com.colegio.usuario.dto.CrearUsuarioRequest;
import com.colegio.usuario.dto.UsuarioDTO;
import com.colegio.usuario.factory.UsuarioFactory;
import com.colegio.usuario.model.Usuario;
import com.colegio.usuario.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final List<UsuarioFactory> factoryList;

    private Map<Integer, UsuarioFactory> factories;

    @PostConstruct
    void initFactories() {
        factories = factoryList.stream()
                .collect(Collectors.toMap(UsuarioFactory::getIdRol, Function.identity()));
        // roles 3 y 4 usan la misma factory de Apoderado
        factories.putIfAbsent(4, factories.get(3));
    }

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
        UsuarioFactory factory = factories.getOrDefault(request.getIdRol(), factories.get(1));
        Usuario usuario = factory.crearUsuario(request, passwordEncoder);
        return convertirADTO(usuarioRepository.save(usuario));
    }

    public UsuarioDTO cambiarEstadoUsuario(Integer idUsuario, String estado) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!"ACTIVO".equalsIgnoreCase(estado) && !"INACTIVO".equalsIgnoreCase(estado)) {
            throw new RuntimeException("Estado inválido");
        }

        usuario.setEstado(estado.toUpperCase());
        return convertirADTO(usuarioRepository.save(usuario));
    }

    public Optional<UsuarioDTO> actualizar(Integer id, ActualizarUsuarioRequest request) {
        return usuarioRepository.findById(id).map(usuario -> {
            if (request.getUsername() != null && !request.getUsername().isBlank())
                usuario.setUsername(request.getUsername().trim());
            if (request.getIdRol() != null)
                usuario.setIdRol(request.getIdRol());
            if (request.getCorreoElectronico() != null)
                usuario.setCorreoElectronico(request.getCorreoElectronico());
            if (request.getBloqueado() != null)
                usuario.setBloqueado(request.getBloqueado());
            if (request.getEstado() != null)
                usuario.setEstado(request.getEstado());
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
                .filter(u -> passwordEncoder.matches(password, u.getPasswordHash()))
                .filter(u -> "ACTIVO".equalsIgnoreCase(u.getEstado()))
                .filter(u -> !Boolean.TRUE.equals(u.getBloqueado()))
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
        dto.setNombreRol(usuarioRepository.findNombreRolById(usuario.getIdRol()));
        return dto;
    }
}
