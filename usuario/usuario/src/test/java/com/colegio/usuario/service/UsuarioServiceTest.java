package com.colegio.usuario.service;

import com.colegio.usuario.dto.UsuarioDTO;
import com.colegio.usuario.model.Usuario;
import com.colegio.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioEjemplo() {
        Usuario u = new Usuario();
        u.setIdUsuario(1);
        u.setIdEstablecimiento(10);
        u.setIdRol(2);
        u.setUsername("jperez");
        u.setPasswordHash("$2a$10$hashSecreto");
        u.setCorreoElectronico("jperez@colegio.cl");
        u.setUltimoAcceso(LocalDateTime.of(2026, 5, 14, 8, 0));
        u.setIntentosFallidos(0);
        u.setBloqueado(false);
        u.setFechaCreacion(LocalDateTime.of(2026, 1, 1, 0, 0));
        u.setEstado("ACTIVO");
        return u;
    }

    // ── listarTodos ───────────────────────────────────────────────

    @Test
    void listarTodos_debeRetornarListaDTOs() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEjemplo()));

        List<UsuarioDTO> resultado = usuarioService.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("jperez", resultado.get(0).getUsername());
        verify(usuarioRepository).findAll();
    }

    @Test
    void listarTodos_debeRetornarListaVaciaSiNoHayUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of());

        List<UsuarioDTO> resultado = usuarioService.listarTodos();

        assertTrue(resultado.isEmpty());
    }

    // ── buscarPorId ───────────────────────────────────────────────

    @Test
    void buscarPorId_debeRetornarDTOSiExiste() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEjemplo()));

        Optional<UsuarioDTO> resultado = usuarioService.buscarPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getIdUsuario());
        assertEquals("jperez", resultado.get().getUsername());
        verify(usuarioRepository).findById(1);
    }

    @Test
    void buscarPorId_debeRetornarVacioSiNoExiste() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        Optional<UsuarioDTO> resultado = usuarioService.buscarPorId(99);

        assertFalse(resultado.isPresent());
    }

    // ── buscarPorUsername ─────────────────────────────────────────

    @Test
    void buscarPorUsername_debeRetornarDTOSiExiste() {
        when(usuarioRepository.findByUsername("jperez"))
                .thenReturn(Optional.of(usuarioEjemplo()));

        Optional<UsuarioDTO> resultado = usuarioService.buscarPorUsername("jperez");

        assertTrue(resultado.isPresent());
        assertEquals("jperez@colegio.cl", resultado.get().getCorreoElectronico());
        verify(usuarioRepository).findByUsername("jperez");
    }

    @Test
    void buscarPorUsername_debeRetornarVacioSiNoExiste() {
        when(usuarioRepository.findByUsername("noexiste")).thenReturn(Optional.empty());

        Optional<UsuarioDTO> resultado = usuarioService.buscarPorUsername("noexiste");

        assertFalse(resultado.isPresent());
    }

    // ── guardar ───────────────────────────────────────────────────

    @Test
    void guardar_debeRetornarDTOConDatosCorrectos() {
        Usuario u = usuarioEjemplo();
        when(usuarioRepository.save(u)).thenReturn(u);

        UsuarioDTO resultado = usuarioService.guardar(u);

        assertEquals("jperez", resultado.getUsername());
        assertEquals("ACTIVO", resultado.getEstado());
        assertFalse(resultado.getBloqueado());
        verify(usuarioRepository).save(u);
    }

    @Test
    void guardar_nuncaDebeExponerPasswordHashEnElDTO() {
        // Test de seguridad crítico: el DTO nunca debe filtrar la contraseña
        Usuario u = usuarioEjemplo();
        when(usuarioRepository.save(u)).thenReturn(u);

        UsuarioDTO resultado = usuarioService.guardar(u);

        // UsuarioDTO no tiene campo passwordHash — verificamos que no existe el getter
        assertDoesNotThrow(() -> resultado.getClass().getMethod("getUsername"));
        assertThrows(NoSuchMethodException.class,
                () -> resultado.getClass().getMethod("getPasswordHash"));
    }

    // ── eliminar ──────────────────────────────────────────────────

    @Test
    void eliminar_debeInvocarDeleteById() {
        doNothing().when(usuarioRepository).deleteById(1);

        usuarioService.eliminar(1);

        verify(usuarioRepository, times(1)).deleteById(1);
    }

    // ── existeUsername ────────────────────────────────────────────

    @Test
    void existeUsername_debeRetornarTrueSiExiste() {
        when(usuarioRepository.existsByUsername("jperez")).thenReturn(true);

        boolean resultado = usuarioService.existeUsername("jperez");

        assertTrue(resultado);
        verify(usuarioRepository).existsByUsername("jperez");
    }

    @Test
    void existeUsername_debeRetornarFalseSiNoExiste() {
        when(usuarioRepository.existsByUsername("noexiste")).thenReturn(false);

        boolean resultado = usuarioService.existeUsername("noexiste");

        assertFalse(resultado);
    }

    // ── convertirADTO (verificación indirecta) ────────────────────

    @Test
    void convertirADTO_debeMappearTodosLosCamposEsperados() {
        Usuario u = usuarioEjemplo();
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(u));

        UsuarioDTO dto = usuarioService.buscarPorId(1).get();

        assertEquals(1,           dto.getIdUsuario());
        assertEquals(10,          dto.getIdEstablecimiento());
        assertEquals(2,           dto.getIdRol());
        assertEquals("jperez",    dto.getUsername());
        assertEquals("jperez@colegio.cl", dto.getCorreoElectronico());
        assertFalse(dto.getBloqueado());
        assertEquals("ACTIVO",    dto.getEstado());
        assertNotNull(dto.getUltimoAcceso());
    }
}
