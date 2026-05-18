package com.colegio.usuario.controller;

import com.colegio.usuario.dto.CrearUsuarioRequest;
import com.colegio.usuario.dto.UsuarioDTO;
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
import static org.mockito.ArgumentMatchers.any;
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

    // ── GET /usuarios (sin filtro) ────────────────────────────────

    @Test
    void listar_sinFiltro_debeRetornar200ConTodos() {
        when(usuarioService.listarTodos()).thenReturn(List.of(dtoEjemplo()));

        ResponseEntity<List<UsuarioDTO>> respuesta = usuarioController.listar(null);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(usuarioService).listarTodos();
        verify(usuarioService, never()).listarPorEstablecimiento(any());
    }

    @Test
    void listar_sinFiltro_debeRetornar200ConListaVacia() {
        when(usuarioService.listarTodos()).thenReturn(List.of());

        ResponseEntity<List<UsuarioDTO>> respuesta = usuarioController.listar(null);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertTrue(respuesta.getBody().isEmpty());
    }

    // ── GET /usuarios?idEstablecimiento=10 ────────────────────────

    @Test
    void listar_conFiltro_debeRetornar200FiltradoPorEstablecimiento() {
        when(usuarioService.listarPorEstablecimiento(10)).thenReturn(List.of(dtoEjemplo()));

        ResponseEntity<List<UsuarioDTO>> respuesta = usuarioController.listar(10);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(10, respuesta.getBody().get(0).getIdEstablecimiento());
        verify(usuarioService).listarPorEstablecimiento(10);
        verify(usuarioService, never()).listarTodos();
    }

    // ── GET /usuarios/{id} ────────────────────────────────────────

    @Test
    void buscarPorId_debeRetornar200SiExiste() {
        when(usuarioService.buscarPorId(1)).thenReturn(Optional.of(dtoEjemplo()));

        ResponseEntity<UsuarioDTO> respuesta = usuarioController.buscarPorId(1);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("jperez", respuesta.getBody().getUsername());
    }

    @Test
    void buscarPorId_debeRetornar404SiNoExiste() {
        when(usuarioService.buscarPorId(99)).thenReturn(Optional.empty());

        ResponseEntity<UsuarioDTO> respuesta = usuarioController.buscarPorId(99);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
    }

    // ── POST /usuarios ────────────────────────────────────────────

    @Test
    void crear_debeRetornar201ConDTOCreado() {
        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setUsername("jperez");
        request.setIdRol(1);

        when(usuarioService.crear(any(CrearUsuarioRequest.class))).thenReturn(dtoEjemplo());

        ResponseEntity<UsuarioDTO> respuesta = usuarioController.crear(request);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals("jperez", respuesta.getBody().getUsername());
        verify(usuarioService).crear(any(CrearUsuarioRequest.class));
    }

    @Test
    void crear_nuncaDebeExponerPasswordHashEnElDTO() {
        when(usuarioService.crear(any(CrearUsuarioRequest.class))).thenReturn(dtoEjemplo());

        ResponseEntity<UsuarioDTO> respuesta = usuarioController.crear(new CrearUsuarioRequest());

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
        verify(usuarioService).eliminar(1);
    }
}
