package com.colegio.usuario.service;

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
