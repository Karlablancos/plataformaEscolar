package com.colegio.usuario.service;

import com.colegio.usuario.dto.UsuarioDTO;
import com.colegio.usuario.model.Usuario;
import com.colegio.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll()
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

    public UsuarioDTO guardar(Usuario usuario) {
        return convertirADTO(usuarioRepository.save(usuario));
    }

    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }

    public boolean existeUsername(String username) {
        return usuarioRepository.existsByUsername(username);
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