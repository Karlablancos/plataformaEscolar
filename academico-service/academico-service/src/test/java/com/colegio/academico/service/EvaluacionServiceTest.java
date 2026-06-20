package com.colegio.academico.service;

import com.colegio.academico.dto.EvaluacionDTO;
import com.colegio.academico.model.Evaluacion;
import com.colegio.academico.model.TipoEvaluacion;
import com.colegio.academico.repository.EvaluacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluacionServiceTest {

    @Mock
    private EvaluacionRepository evaluacionRepository;

    @InjectMocks
    private EvaluacionService evaluacionService;

    private Evaluacion evaluacionEjemplo() {
        Evaluacion e = new Evaluacion();
        e.setId(1L);
        e.setNombre("Prueba Unidad 1");
        e.setTipo(TipoEvaluacion.PRUEBA);
        e.setPonderacion(new BigDecimal("60.00"));
        e.setFecha(LocalDate.of(2026, 5, 15));
        e.setIdCurso(1L);
        e.setIdAsignatura(1L);
        e.setIdDocente(3L);
        e.setIdEstablecimiento(1L);
        return e;
    }

    private EvaluacionDTO dtoEjemplo() {
        EvaluacionDTO dto = new EvaluacionDTO();
        dto.setNombre("Prueba Unidad 1");
        dto.setTipo(TipoEvaluacion.PRUEBA);
        dto.setPonderacion(new BigDecimal("60.00"));
        dto.setFecha(LocalDate.of(2026, 5, 15));
        dto.setIdCurso(1L);
        dto.setIdAsignatura(1L);
        dto.setIdDocente(3L);
        dto.setIdEstablecimiento(1L);
        return dto;
    }

    @Test
    void crearEvaluacion_debeRetornarEvaluacionGuardada() {
        when(evaluacionRepository.save(any(Evaluacion.class))).thenReturn(evaluacionEjemplo());

        EvaluacionDTO resultado = evaluacionService.crear(dtoEjemplo());

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Prueba Unidad 1", resultado.getNombre());
        assertEquals(TipoEvaluacion.PRUEBA, resultado.getTipo());
        assertEquals(new BigDecimal("60.00"), resultado.getPonderacion());
        verify(evaluacionRepository).save(any(Evaluacion.class));
    }

    @Test
    void listarPorCurso_debeRetornarListaDeEvaluaciones() {
        when(evaluacionRepository.findByIdCurso(1L))
                .thenReturn(List.of(evaluacionEjemplo()));

        List<EvaluacionDTO> resultado = evaluacionService.listarPorCurso(1L);

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getIdCurso());
        verify(evaluacionRepository).findByIdCurso(1L);
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornarEvaluacion() {
        when(evaluacionRepository.findById(1L))
                .thenReturn(Optional.of(evaluacionEjemplo()));

        Optional<EvaluacionDTO> resultado = evaluacionService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("Prueba Unidad 1", resultado.get().getNombre());
        verify(evaluacionRepository).findById(1L);
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(evaluacionRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<EvaluacionDTO> resultado = evaluacionService.buscarPorId(99L);

        assertFalse(resultado.isPresent());
        verify(evaluacionRepository).findById(99L);
    }
}
