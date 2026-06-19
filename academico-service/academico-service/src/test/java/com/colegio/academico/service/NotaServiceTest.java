package com.colegio.academico.service;

import com.colegio.academico.dto.NotaDTO;
import com.colegio.academico.model.Evaluacion;
import com.colegio.academico.model.Nota;
import com.colegio.academico.model.TipoEvaluacion;
import com.colegio.academico.repository.EvaluacionRepository;
import com.colegio.academico.repository.NotaRepository;
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
class NotaServiceTest {

    @Mock
    private NotaRepository notaRepository;

    @Mock
    private EvaluacionRepository evaluacionRepository;

    @InjectMocks
    private NotaService notaService;

    private Evaluacion evaluacionConPonderacion(Long id, Long idAsignatura, String ponderacion) {
        Evaluacion e = new Evaluacion();
        e.setId(id);
        e.setNombre("Evaluacion " + id);
        e.setTipo(TipoEvaluacion.PRUEBA);
        e.setPonderacion(new BigDecimal(ponderacion));
        e.setFecha(LocalDate.of(2026, 5, 15));
        e.setIdCurso(1L);
        e.setIdAsignatura(idAsignatura);
        e.setIdDocente(3L);
        e.setIdEstablecimiento(1L);
        return e;
    }

    private Nota notaConEvaluacion(Evaluacion evaluacion, String calificacion) {
        Nota n = new Nota();
        n.setId(1L);
        n.setCalificacion(new BigDecimal(calificacion));
        n.setEvaluacion(evaluacion);
        n.setIdEstudiante(1L);
        n.setIdEstablecimiento(1L);
        return n;
    }

    // ── registrarNota ─────────────────────────────────────────────

    @Test
    void registrarNota_conCalificacionValida_debeGuardar() {
        Evaluacion evaluacion = evaluacionConPonderacion(1L, 1L, "60.00");
        Nota notaGuardada = notaConEvaluacion(evaluacion, "6.5");
        notaGuardada.setId(10L);

        NotaDTO dto = new NotaDTO();
        dto.setCalificacion(new BigDecimal("6.5"));
        dto.setIdEvaluacion(1L);
        dto.setIdEstudiante(1L);
        dto.setIdEstablecimiento(1L);

        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));
        when(notaRepository.save(any())).thenReturn(notaGuardada);

        NotaDTO resultado = notaService.registrarNota(dto);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("6.5"), resultado.getCalificacion());
        assertEquals(1L, resultado.getIdEvaluacion());
        verify(notaRepository).save(any());
    }

    @Test
    void registrarNota_conCalificacionMenorA1_debeLanzarIllegalArgumentException() {
        NotaDTO dto = new NotaDTO();
        dto.setCalificacion(new BigDecimal("0.9"));
        dto.setIdEvaluacion(1L);
        dto.setIdEstudiante(1L);
        dto.setIdEstablecimiento(1L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> notaService.registrarNota(dto));

        assertTrue(ex.getMessage().contains("1.0"));
        verifyNoInteractions(notaRepository);
    }

    @Test
    void registrarNota_conCalificacionMayorA7_debeLanzarIllegalArgumentException() {
        NotaDTO dto = new NotaDTO();
        dto.setCalificacion(new BigDecimal("7.1"));
        dto.setIdEvaluacion(1L);
        dto.setIdEstudiante(1L);
        dto.setIdEstablecimiento(1L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> notaService.registrarNota(dto));

        assertTrue(ex.getMessage().contains("7.0"));
        verifyNoInteractions(notaRepository);
    }

    // ── calcularPromedio ──────────────────────────────────────────

    @Test
    void calcularPromedio_debeRetornarPromedioPonderadoCorrecto() {
        // Nota 1: 6.0 con ponderación 60  → aporte 360
        // Nota 2: 5.0 con ponderación 40  → aporte 200
        // Promedio = (360 + 200) / (60 + 40) = 560 / 100 = 5.6
        Evaluacion ev1 = evaluacionConPonderacion(1L, 1L, "60");
        Evaluacion ev2 = evaluacionConPonderacion(2L, 1L, "40");

        Nota n1 = notaConEvaluacion(ev1, "6.0");
        Nota n2 = notaConEvaluacion(ev2, "5.0");

        when(notaRepository.findByIdEstudianteAndEvaluacion_IdAsignatura(1L, 1L))
                .thenReturn(List.of(n1, n2));

        BigDecimal promedio = notaService.calcularPromedio(1L, 1L);

        assertEquals(new BigDecimal("5.6"), promedio);
        verify(notaRepository).findByIdEstudianteAndEvaluacion_IdAsignatura(1L, 1L);
    }

    // ── calcularPromocion ─────────────────────────────────────────

    @Test
    void calcularPromocion_conTodasAprobadas_debeRetornarAPROBADO() {
        // Asignatura 1: nota 6.0 con ponderación 100 → promedio 6.0 (aprobada)
        // Asignatura 2: nota 5.0 con ponderación 100 → promedio 5.0 (aprobada)
        Evaluacion evAsig1 = evaluacionConPonderacion(1L, 1L, "100");
        Evaluacion evAsig2 = evaluacionConPonderacion(2L, 2L, "100");

        Nota n1 = notaConEvaluacion(evAsig1, "6.0");
        n1.setIdEstudiante(1L);
        Nota n2 = notaConEvaluacion(evAsig2, "5.0");
        n2.setIdEstudiante(1L);

        when(notaRepository.findByIdEstudiante(1L)).thenReturn(List.of(n1, n2));

        String resultado = notaService.calcularPromocion(1L);

        assertEquals("APROBADO", resultado);
    }

    @Test
    void calcularPromocion_conPromedioMenorA4_debeRetornarREPROBADO() {
        // Asignatura 1: nota 3.0 → reprobada
        // Asignatura 2: nota 3.5 → reprobada
        // 2 reprobadas + promedio general < 4.5 → REPROBADO
        Evaluacion evAsig1 = evaluacionConPonderacion(1L, 1L, "100");
        Evaluacion evAsig2 = evaluacionConPonderacion(2L, 2L, "100");

        Nota n1 = notaConEvaluacion(evAsig1, "3.0");
        n1.setIdEstudiante(1L);
        Nota n2 = notaConEvaluacion(evAsig2, "3.5");
        n2.setIdEstudiante(1L);

        when(notaRepository.findByIdEstudiante(1L)).thenReturn(List.of(n1, n2));

        String resultado = notaService.calcularPromocion(1L);

        assertEquals("REPROBADO", resultado);
    }
}
