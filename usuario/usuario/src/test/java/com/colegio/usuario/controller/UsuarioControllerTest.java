package com.colegio.usuario.controller;

import com.colegio.usuario.dto.UsuarioDTO;
import com.colegio.usuario.model.Usuario;
import com.colegio.usuario.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private UsuarioDTO dtoEjemplo() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(1);
        dto.setIdEstablecimiento(10);
        dto.setIdRol(2);
        dto.setUsername("jperez");
        dto.setCorreoElectronico("jperez@colegio.cl");
        dto.setUltimoAcceso(LocalDateTime.of(2026, 5, 14, 8, 0));
        dto.setBloqueado(false);
        dto.setEstado("ACTIVO");
        return dto;
    }

    private Usuario usuarioEjemplo() {
        Usuario u = new Usuario();
        u.setIdUsuario(1);
        u.setIdEstablecimiento(10);
        u.setIdRol(2);
        u.setUsername("jperez");
        u.setPasswordHash("$2a$10$hashSecreto");
        u.setCorreoElectronico("jperez@colegio.cl");
        u.setIntentosFallidos(0);
        u.setBloqueado(false);
        u.setFechaCreacion(LocalDateTime.of(2026, 1, 1, 0, 0));
        u.setEstado("ACTIVO");
        return u;
    }

    // ── GET /usuarios ─────────────────────────────────────────────

    @Test
    void listarTodos_debeRetornar200ConLista() {
        when(usuarioService.listarTodos()).thenReturn(List.of(dtoEjemplo()));

        ResponseEntity<List<UsuarioDTO>> respuesta = usuarioController.listarTodos();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(1, respuesta.getBody().size());
        assertEquals("jperez", respuesta.getBody().get(0).getUsername());
        verify(usuarioService).listarTodos();
    }

    @Test
    void listarTodos_debeRetornar200ConListaVaciaSiNoHayUsuarios() {
        when(usuarioService.listarTodos()).thenReturn(List.of());

        ResponseEntity<List<UsuarioDTO>> respuesta = usuarioController.listarTodos();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertTrue(respuesta.getBody().isEmpty());
    }

    // ── GET /usuarios/{id} ────────────────────────────────────────

    @Test
    void buscarPorId_debeRetornar200SiExiste() {
        when(usuarioService.buscarPorId(1)).thenReturn(Optional.of(dtoEjemplo()));

        ResponseEntity<UsuarioDTO> respuesta = usuarioController.buscarPorId(1);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(1, respuesta.getBody().getIdUsuario());
        assertEquals("jperez", respuesta.getBody().getUsername());
        verify(usuarioService).buscarPorId(1);
    }

    @Test
    void buscarPorId_debeRetornar404SiNoExiste() {
        when(usuarioService.buscarPorId(99)).thenReturn(Optional.empty());

        ResponseEntity<UsuarioDTO> respuesta = usuarioController.buscarPorId(99);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(usuarioService).buscarPorId(99);
    }

    // ── POST /usuarios ────────────────────────────────────────────

    @Test
    void guardar_debeRetornar201ConDTOCreado() {
        Usuario u = usuarioEjemplo();
        when(usuarioService.guardar(u)).thenReturn(dtoEjemplo());

        ResponseEntity<UsuarioDTO> respuesta = usuarioController.guardar(u);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals("jperez", respuesta.getBody().getUsername());
        assertEquals("ACTIVO", respuesta.getBody().getEstado());
        verify(usuarioService).guardar(u);
    }

    @Test
    void guardar_debeRetornar201SinExponerPasswordHash() {
        Usuario u = usuarioEjemplo();
        when(usuarioService.guardar(u)).thenReturn(dtoEjemplo());

        ResponseEntity<UsuarioDTO> respuesta = usuarioController.guardar(u);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertThrows(NoSuchMethodException.class,
                () -> respuesta.getBody().getClass().getMethod("getPasswordHash"));
    }

    // ── DELETE /usuarios/{id} ─────────────────────────────────────

    @Test
    void eliminar_debeRetornar204SinContenido() {
        doNothing().when(usuarioService).eliminar(1);

        ResponseEntity<Void> respuesta = usuarioController.eliminar(1);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(usuarioService, times(1)).eliminar(1);
    }

    @Test
    void eliminar_debeDelegarAlServicioSinRetornarCuerpo() {
        doNothing().when(usuarioService).eliminar(5);

        usuarioController.eliminar(5);

        verify(usuarioService).eliminar(5);
        verifyNoMoreInteractions(usuarioService);
    }
}
