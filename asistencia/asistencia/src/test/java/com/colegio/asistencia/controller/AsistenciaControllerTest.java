package com.colegio.asistencia.controller;

import com.colegio.asistencia.dto.AsistenciaDTO;
import com.colegio.asistencia.service.AsistenciaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsistenciaControllerTest {

    @Mock
    private AsistenciaService service;

    @InjectMocks
    private AsistenciaController controller;

    private AsistenciaDTO dtoEjemplo() {
        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setIdAsistencia(1);
        dto.setIdEstudiante(5);
        dto.setIdHorario(2);
        dto.setFecha(LocalDate.of(2026, 5, 14));
        dto.setEstadoAsistencia("PRESENTE");
        dto.setJustificada(false);
        return dto;
    }

    // ── obtenerPorEstudiante ──────────────────────────────────────

    @Test
    void obtenerPorEstudiante_debeRetornar200ConLista() {
        when(service.obtenerPorEstudiante(5)).thenReturn(List.of(dtoEjemplo()));

        ResponseEntity<List<AsistenciaDTO>> respuesta = controller.obtenerPorEstudiante(5);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(1, respuesta.getBody().size());
        assertEquals("PRESENTE", respuesta.getBody().get(0).getEstadoAsistencia());
        verify(service).obtenerPorEstudiante(5);
    }

    @Test
    void obtenerPorEstudiante_debeRetornar200ConListaVaciaSiNoHayRegistros() {
        when(service.obtenerPorEstudiante(99)).thenReturn(List.of());

        ResponseEntity<List<AsistenciaDTO>> respuesta = controller.obtenerPorEstudiante(99);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertTrue(respuesta.getBody().isEmpty());
    }

    // ── obtenerPorFecha ───────────────────────────────────────────

    @Test
    void obtenerPorFecha_debeRetornar200ConLista() {
        LocalDate fecha = LocalDate.of(2026, 5, 14);
        when(service.obtenerPorEstudianteYFecha(5, fecha)).thenReturn(List.of(dtoEjemplo()));

        ResponseEntity<List<AsistenciaDTO>> respuesta = controller.obtenerPorFecha(5, fecha);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(1, respuesta.getBody().size());
        assertEquals(fecha, respuesta.getBody().get(0).getFecha());
        verify(service).obtenerPorEstudianteYFecha(5, fecha);
    }

    @Test
    void obtenerPorFecha_debeRetornar200ConListaVaciaSiFechaNoTieneRegistros() {
        LocalDate fecha = LocalDate.of(2026, 1, 1);
        when(service.obtenerPorEstudianteYFecha(5, fecha)).thenReturn(List.of());

        ResponseEntity<List<AsistenciaDTO>> respuesta = controller.obtenerPorFecha(5, fecha);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertTrue(respuesta.getBody().isEmpty());
    }

    // ── registrar ─────────────────────────────────────────────────

    @Test
    void registrar_debeRetornar200ConDTOGuardado() {
        AsistenciaDTO dto = dtoEjemplo();
        when(service.registrar(dto)).thenReturn(dto);

        ResponseEntity<AsistenciaDTO> respuesta = controller.registrar(dto);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(5, respuesta.getBody().getIdEstudiante());
        verify(service).registrar(dto);
    }

    @Test
    void registrar_debeDelegarAlServicioSinModificarElDTO() {
        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setIdEstudiante(3);
        dto.setEstadoAsistencia("AUSENTE");
        dto.setJustificada(true);

        AsistenciaDTO guardado = new AsistenciaDTO();
        guardado.setIdAsistencia(42);
        guardado.setIdEstudiante(3);
        guardado.setEstadoAsistencia("AUSENTE");
        guardado.setJustificada(true);

        when(service.registrar(dto)).thenReturn(guardado);

        ResponseEntity<AsistenciaDTO> respuesta = controller.registrar(dto);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(42, respuesta.getBody().getIdAsistencia());
        assertEquals("AUSENTE", respuesta.getBody().getEstadoAsistencia());
        verify(service, times(1)).registrar(dto);
    }
}
