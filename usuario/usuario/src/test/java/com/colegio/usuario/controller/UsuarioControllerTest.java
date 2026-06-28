package com.colegio.usuario.controller;

import com.colegio.usuario.dto.ActualizarUsuarioRequest;
import com.colegio.usuario.dto.CambiarPasswordRequest;
import com.colegio.usuario.dto.CrearUsuarioRequest;
import com.colegio.usuario.dto.EliminarUsuarioResponse;
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
import static org.mockito.ArgumentMatchers.eq;
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
    void listar_sinFiltro_debeRetornar200ConListaTodos() {
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
        assertEquals(1, respuesta.getBody().size());
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
        assertEquals(1, respuesta.getBody().getIdUsuario());
        verify(usuarioService).buscarPorId(1);
    }

    @Test
    void buscarPorId_debeRetornar404SiNoExiste() {
        when(usuarioService.buscarPorId(99)).thenReturn(Optional.empty());

        ResponseEntity<UsuarioDTO> respuesta = usuarioController.buscarPorId(99);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
    }

    // ── POST /usuarios ────────────────────────────────────────────

    @Test
    void crear_debeRetornar201ConDTOCreado() {
        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setUsername("jperez");
        request.setIdRol(1);
        request.setPassword("secreto123");
        request.setIdEstablecimiento(10);

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

    // ── PUT /usuarios/{id} ────────────────────────────────────────

    @Test
    void actualizar_debeRetornar200SiExiste() {
        ActualizarUsuarioRequest request = new ActualizarUsuarioRequest();
        request.setCorreoElectronico("nuevo@colegio.cl");

        when(usuarioService.actualizar(eq(1), any(ActualizarUsuarioRequest.class)))
                .thenReturn(Optional.of(dtoEjemplo()));

        ResponseEntity<UsuarioDTO> respuesta = usuarioController.actualizar(1, request);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        verify(usuarioService).actualizar(eq(1), any(ActualizarUsuarioRequest.class));
    }

    @Test
    void actualizar_debeRetornar404SiNoExiste() {
        when(usuarioService.actualizar(eq(99), any(ActualizarUsuarioRequest.class)))
                .thenReturn(Optional.empty());

        ResponseEntity<UsuarioDTO> respuesta = usuarioController.actualizar(99, new ActualizarUsuarioRequest());

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
    }

    // ── PUT /usuarios/{id}/password ───────────────────────────────

    @Test
    void cambiarPassword_debeRetornar204SiPasswordCorrecta() {
        CambiarPasswordRequest request = new CambiarPasswordRequest();
        request.setPasswordActual("actual123");
        request.setPasswordNueva("nueva456");

        when(usuarioService.cambiarPassword(eq(1), any(CambiarPasswordRequest.class))).thenReturn(true);

        ResponseEntity<Void> respuesta = usuarioController.cambiarPassword(1, request);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
    }

    @Test
    void cambiarPassword_debeRetornar401SiPasswordIncorrecta() {
        when(usuarioService.cambiarPassword(eq(1), any(CambiarPasswordRequest.class))).thenReturn(false);

        ResponseEntity<Void> respuesta = usuarioController.cambiarPassword(1, new CambiarPasswordRequest());

        assertEquals(HttpStatus.UNAUTHORIZED, respuesta.getStatusCode());
    }

    // ── DELETE /usuarios/{id} ─────────────────────────────────────

    @Test
    void eliminar_debeRetornar200ConResultado() {
        EliminarUsuarioResponse response = new EliminarUsuarioResponse(
                "ELIMINADO",
                "Usuario eliminado correctamente.",
                null);
        when(usuarioService.eliminar(1)).thenReturn(response);

        ResponseEntity<EliminarUsuarioResponse> respuesta = usuarioController.eliminar(1);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("ELIMINADO", respuesta.getBody().getAccion());
        verify(usuarioService, times(1)).eliminar(1);
    }
}
