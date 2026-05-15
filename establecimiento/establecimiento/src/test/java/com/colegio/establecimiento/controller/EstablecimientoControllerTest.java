package com.colegio.establecimiento.controller;

import com.colegio.establecimiento.dto.EstablecimientoDTO;
import com.colegio.establecimiento.model.Establecimiento;
import com.colegio.establecimiento.service.EstablecimientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstablecimientoControllerTest {

    @Mock
    private EstablecimientoService establecimientoService;

    @InjectMocks
    private EstablecimientoController establecimientoController;

    private Establecimiento establecimientoMock;

    @BeforeEach
    void setUp() {
        establecimientoMock = new Establecimiento();
        establecimientoMock.setRbd("12345");
        establecimientoMock.setNombre("Colegio Bernardo O'Higgins");
        establecimientoMock.setEstado("ACTIVO");
    }

    @Test
    void buscarPorRbd_cuandoExiste_retorna200() {
        when(establecimientoService.buscarPorRbd("12345"))
                .thenReturn(Optional.of(establecimientoMock));

        ResponseEntity<EstablecimientoDTO> response =
                establecimientoController.buscarPorRbd("12345");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("12345", response.getBody().getRbd());
    }

    @Test
    void buscarPorRbd_cuandoNoExiste_retorna404() {
        when(establecimientoService.buscarPorRbd("99999"))
                .thenReturn(Optional.empty());

        ResponseEntity<EstablecimientoDTO> response =
                establecimientoController.buscarPorRbd("99999");

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void existeRbd_cuandoExiste_retornaTrue() {
        when(establecimientoService.existeRbd("12345")).thenReturn(true);

        ResponseEntity<Boolean> response =
                establecimientoController.existeRbd("12345");

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody());
    }

    @Test
    void existeRbd_cuandoNoExiste_retornaFalse() {
        when(establecimientoService.existeRbd("00000")).thenReturn(false);

        ResponseEntity<Boolean> response =
                establecimientoController.existeRbd("00000");

        assertEquals(200, response.getStatusCode().value());
        assertFalse(response.getBody());
    }
}