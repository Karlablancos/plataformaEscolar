package com.colegio.usuario.service;

import com.colegio.usuario.dto.ActualizarUsuarioRequest;
import com.colegio.usuario.dto.CambiarPasswordRequest;
import com.colegio.usuario.dto.CrearUsuarioRequest;
import com.colegio.usuario.dto.UsuarioDTO;
import com.colegio.usuario.factory.AdminUsuarioFactory;
import com.colegio.usuario.factory.ApoderadoUsuarioFactory;
import com.colegio.usuario.factory.ProfesorUsuarioFactory;
import com.colegio.usuario.factory.UsuarioFactory;
import com.colegio.usuario.model.Usuario;
import com.colegio.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        List<UsuarioFactory> factories = List.of(
                new AdminUsuarioFactory(),
                new ProfesorUsuarioFactory(),
                new ApoderadoUsuarioFactory()
        );
        usuarioService = new UsuarioService(usuarioRepository, passwordEncoder, factories);
        usuarioService.initFactories();
    }

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

        assertTrue(usuarioService.listarTodos().isEmpty());
    }

    // ── listarPorEstablecimiento ──────────────────────────────────

    @Test
    void listarPorEstablecimiento_debeRetornarSoloUsuariosDelEstablecimiento() {
        when(usuarioRepository.findByIdEstablecimiento(10))
                .thenReturn(List.of(usuarioEjemplo()));

        List<UsuarioDTO> resultado = usuarioService.listarPorEstablecimiento(10);

        assertEquals(1, resultado.size());
        assertEquals(10, resultado.get(0).getIdEstablecimiento());
        verify(usuarioRepository).findByIdEstablecimiento(10);
    }

    // ── buscarPorId ───────────────────────────────────────────────

    @Test
    void buscarPorId_debeRetornarDTOSiExiste() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEjemplo()));

        Optional<UsuarioDTO> resultado = usuarioService.buscarPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals("jperez", resultado.get().getUsername());
        verify(usuarioRepository).findById(1);
    }

    @Test
    void buscarPorId_debeRetornarVacioSiNoExiste() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        assertFalse(usuarioService.buscarPorId(99).isPresent());
    }

    // ── crear con Factory Method ──────────────────────────────────

    @Test
    void crear_adminFactory_debeCrearUsuarioActivo() {
        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setUsername("admin1");
        request.setPassword("pass123");
        request.setIdEstablecimiento(10);
        request.setIdRol(1);
        request.setCorreoElectronico("admin@colegio.cl");

        Usuario guardado = usuarioEjemplo();
        guardado.setIdRol(1);
        guardado.setEstado("ACTIVO");

        when(passwordEncoder.encode("pass123")).thenReturn("$2a$10$hash");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(guardado);

        UsuarioDTO resultado = usuarioService.crear(request);

        assertEquals("ACTIVO", resultado.getEstado());
        verify(passwordEncoder).encode("pass123");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void crear_apoderadoFactory_debeCrearUsuarioPendiente() {
        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setUsername("apod1");
        request.setPassword("pass123");
        request.setIdEstablecimiento(10);
        request.setIdRol(3);
        request.setCorreoElectronico("apod@colegio.cl");

        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$hash");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setIdUsuario(5);
            return u;
        });

        UsuarioDTO resultado = usuarioService.crear(request);

        assertEquals("PENDIENTE", resultado.getEstado());
    }

    @Test
    void crear_nuncaDebeExponerPasswordHashEnElDTO() {
        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setIdRol(1);
        request.setPassword("pass");

        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$hash");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEjemplo());

        UsuarioDTO resultado = usuarioService.crear(request);

        assertThrows(NoSuchMethodException.class,
                () -> resultado.getClass().getMethod("getPasswordHash"));
    }

    // ── actualizar ────────────────────────────────────────────────

    @Test
    void actualizar_debeModificarCamposPresentes() {
        Usuario u = usuarioEjemplo();
        ActualizarUsuarioRequest request = new ActualizarUsuarioRequest();
        request.setCorreoElectronico("nuevo@colegio.cl");
        request.setBloqueado(true);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(u));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(u);

        Optional<UsuarioDTO> resultado = usuarioService.actualizar(1, request);

        assertTrue(resultado.isPresent());
        verify(usuarioRepository).findById(1);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void actualizar_debeRetornarVacioSiNoExiste() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        Optional<UsuarioDTO> resultado = usuarioService.actualizar(99, new ActualizarUsuarioRequest());

        assertFalse(resultado.isPresent());
    }

    // ── cambiarPassword ───────────────────────────────────────────

    @Test
    void cambiarPassword_debeRetornarTrueSiPasswordActualCorrecta() {
        Usuario u = usuarioEjemplo();
        CambiarPasswordRequest request = new CambiarPasswordRequest();
        request.setPasswordActual("secreto123");
        request.setPasswordNueva("nueva456");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("secreto123", "$2a$10$hashSecreto")).thenReturn(true);
        when(passwordEncoder.encode("nueva456")).thenReturn("$2a$10$nuevoHash");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(u);

        boolean resultado = usuarioService.cambiarPassword(1, request);

        assertTrue(resultado);
        verify(passwordEncoder).encode("nueva456");
    }

    @Test
    void cambiarPassword_debeRetornarFalseSiPasswordActualIncorrecta() {
        Usuario u = usuarioEjemplo();
        CambiarPasswordRequest request = new CambiarPasswordRequest();
        request.setPasswordActual("incorrecta");
        request.setPasswordNueva("nueva456");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("incorrecta", "$2a$10$hashSecreto")).thenReturn(false);

        boolean resultado = usuarioService.cambiarPassword(1, request);

        assertFalse(resultado);
        verify(passwordEncoder, never()).encode(anyString());
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

        assertTrue(usuarioService.existeUsername("jperez"));
    }

    @Test
    void existeUsername_debeRetornarFalseSiNoExiste() {
        when(usuarioRepository.existsByUsername("noexiste")).thenReturn(false);

        assertFalse(usuarioService.existeUsername("noexiste"));
    }

    // ── login ─────────────────────────────────────────────────────

    @Test
    void login_debeRetornarDTOSiCredencialesCorrectas() {
        Usuario u = usuarioEjemplo();
        when(usuarioRepository.findByUsernameAndIdEstablecimiento("jperez", 10))
                .thenReturn(Optional.of(u));
        when(passwordEncoder.matches("secreto", "$2a$10$hashSecreto")).thenReturn(true);

        Optional<UsuarioDTO> resultado = usuarioService.login("jperez", "secreto", 10);

        assertTrue(resultado.isPresent());
        assertEquals("jperez", resultado.get().getUsername());
    }

    @Test
    void login_debeRetornarVacioSiPasswordIncorrecta() {
        Usuario u = usuarioEjemplo();
        when(usuarioRepository.findByUsernameAndIdEstablecimiento("jperez", 10))
                .thenReturn(Optional.of(u));
        when(passwordEncoder.matches("mala", "$2a$10$hashSecreto")).thenReturn(false);

        assertFalse(usuarioService.login("jperez", "mala", 10).isPresent());
    }
}
