package com.colegio.establecimiento.service;

import com.colegio.establecimiento.model.Establecimiento;
import com.colegio.establecimiento.repository.EstablecimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstablecimientoServiceTest {

    @Mock
    private EstablecimientoRepository establecimientoRepository;

    @InjectMocks
    private EstablecimientoService establecimientoService;

    private Establecimiento establecimientoMock;

    @BeforeEach
    void setUp() {
        establecimientoMock = new Establecimiento();
        establecimientoMock.setIdEstablecimiento(1);
        establecimientoMock.setRbd("12345");
        establecimientoMock.setNombre("Colegio Bernardo O'Higgins");
        establecimientoMock.setSostenedor("Municipal");
        establecimientoMock.setDirector("Juan Pérez");
        establecimientoMock.setCorreoElectronico("contacto@colegio.cl");
        establecimientoMock.setTelefono("51212345");
        establecimientoMock.setEstado("ACTIVO");
    }

    // ✅ TEST 1: buscar por RBD que SÍ existe
    @Test
    void buscarPorRbd_cuandoExiste_retornaEstablecimiento() {
        when(establecimientoRepository.findByRbd("12345"))
                .thenReturn(Optional.of(establecimientoMock));

        Optional<Establecimiento> resultado = establecimientoService.buscarPorRbd("12345");

        assertTrue(resultado.isPresent());
        assertEquals("Colegio Bernardo O'Higgins", resultado.get().getNombre());
        verify(establecimientoRepository, times(1)).findByRbd("12345");
    }

    // ❌ TEST 2: buscar por RBD que NO existe
    @Test
    void buscarPorRbd_cuandoNoExiste_retornaVacio() {
        when(establecimientoRepository.findByRbd("99999"))
                .thenReturn(Optional.empty());

        Optional<Establecimiento> resultado = establecimientoService.buscarPorRbd("99999");

        assertFalse(resultado.isPresent());
        verify(establecimientoRepository, times(1)).findByRbd("99999");
    }

    // ✅ TEST 3: existeRbd con RBD que SÍ existe
    @Test
    void existeRbd_cuandoExiste_retornaTrue() {
        when(establecimientoRepository.findByRbd("12345"))
                .thenReturn(Optional.of(establecimientoMock));

        boolean existe = establecimientoService.existeRbd("12345");

        assertTrue(existe);
    }

    // ❌ TEST 4: existeRbd con RBD que NO existe
    @Test
    void existeRbd_cuandoNoExiste_retornaFalse() {
        when(establecimientoRepository.findByRbd("00000"))
                .thenReturn(Optional.empty());

        boolean existe = establecimientoService.existeRbd("00000");

        assertFalse(existe);
    }
}