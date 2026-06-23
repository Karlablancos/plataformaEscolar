package com.colegio.establecimiento.service;

import com.colegio.establecimiento.dto.AnotacionDTO;
import com.colegio.establecimiento.model.Anotacion;
import com.colegio.establecimiento.repository.AnotacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnotacionServiceTest {

    @Mock
    private AnotacionRepository anotacionRepository;

    @InjectMocks
    private AnotacionService anotacionService;

    private Anotacion anotacionMock;

    @BeforeEach
    void setUp() {
        anotacionMock = new Anotacion();
        anotacionMock.setIdAnotacion(1);
        anotacionMock.setIdEstudiante(5);
        anotacionMock.setIdDocente(2);
        anotacionMock.setTipo("POSITIVA");
        anotacionMock.setDescripcion("Buena participación en clase");
        anotacionMock.setFecha(LocalDate.of(2026, 6, 20));
        anotacionMock.setRequiereCitacion(false);
        anotacionMock.setFechaCitacion(null);
        anotacionMock.setEstado("ACTIVA");
    }

    //TEST: estudiante con anotaciones registradas
    @Test
    void listarPorEstudiante_conRegistros_debeRetornarListaDTOs() {
        when(anotacionRepository.findByIdEstudiante(5)).thenReturn(List.of(anotacionMock));

        List<AnotacionDTO> resultado = anotacionService.listarPorEstudiante(5);

        assertEquals(1, resultado.size());
        assertEquals("POSITIVA", resultado.get(0).getTipo());
        assertEquals(5, resultado.get(0).getIdEstudiante());
        verify(anotacionRepository).findByIdEstudiante(5);
    }

    // TEST: estudiante sin anotaciones registradas
    @Test
    void listarPorEstudiante_sinRegistros_debeRetornarListaVacia() {
        when(anotacionRepository.findByIdEstudiante(99)).thenReturn(List.of());

        List<AnotacionDTO> resultado = anotacionService.listarPorEstudiante(99);

        assertTrue(resultado.isEmpty());
        verify(anotacionRepository).findByIdEstudiante(99);
    }

    // TEST: docente con anotaciones registradas
    @Test
    void listarPorDocente_conRegistros_debeRetornarListaDTOs() {
        when(anotacionRepository.findByIdDocente(2)).thenReturn(List.of(anotacionMock));

        List<AnotacionDTO> resultado = anotacionService.listarPorDocente(2);

        assertEquals(1, resultado.size());
        assertEquals(2, resultado.get(0).getIdDocente());
        verify(anotacionRepository).findByIdDocente(2);
    }

    // TEST: docente sin anotaciones registradas
    @Test
    void listarPorDocente_sinRegistros_debeRetornarListaVacia() {
        when(anotacionRepository.findByIdDocente(99)).thenReturn(List.of());

        List<AnotacionDTO> resultado = anotacionService.listarPorDocente(99);

        assertTrue(resultado.isEmpty());
    }

    // ── listarCitacionesPendientes ────────────────────────────────

    // TEST: existen citaciones pendientes (requiereCitacion=true y fechaCitacion=null)
    @Test
    void listarCitacionesPendientes_conRegistros_debeRetornarListaDTOs() {
        Anotacion conCitacionPendiente = new Anotacion();
        conCitacionPendiente.setIdAnotacion(2);
        conCitacionPendiente.setIdEstudiante(7);
        conCitacionPendiente.setIdDocente(1);
        conCitacionPendiente.setTipo("NEGATIVA");
        conCitacionPendiente.setDescripcion("Conducta inapropiada");
        conCitacionPendiente.setFecha(LocalDate.of(2026, 6, 20));
        conCitacionPendiente.setRequiereCitacion(true);
        conCitacionPendiente.setFechaCitacion(null);
        conCitacionPendiente.setEstado("ACTIVA");

        when(anotacionRepository.findByRequiereCitacionTrueAndFechaCitacionIsNull())
                .thenReturn(List.of(conCitacionPendiente));

        List<AnotacionDTO> resultado = anotacionService.listarCitacionesPendientes();

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getRequiereCitacion());
        assertNull(resultado.get(0).getFechaCitacion());
        verify(anotacionRepository).findByRequiereCitacionTrueAndFechaCitacionIsNull();
    }

    // TEST: no hay citaciones pendientes
    @Test
    void listarCitacionesPendientes_sinRegistros_debeRetornarListaVacia() {
        when(anotacionRepository.findByRequiereCitacionTrueAndFechaCitacionIsNull())
                .thenReturn(List.of());

        List<AnotacionDTO> resultado = anotacionService.listarCitacionesPendientes();

        assertTrue(resultado.isEmpty());
    }

    // ── crear ─────────────────────────────────────────────────────

    // TEST: crear anotación con todos los campos enviados
    @Test
    void crear_conDatosCompletos_debeGuardarYRetornarDTO() {
        AnotacionDTO request = new AnotacionDTO();
        request.setIdEstudiante(5);
        request.setIdDocente(2);
        request.setTipo("POSITIVA");
        request.setDescripcion("Buena participación en clase");
        request.setFecha(LocalDate.of(2026, 6, 20));
        request.setRequiereCitacion(false);
        request.setEstado("ACTIVA");

        when(anotacionRepository.save(any(Anotacion.class))).thenReturn(anotacionMock);

        AnotacionDTO resultado = anotacionService.crear(request);

        assertEquals("POSITIVA", resultado.getTipo());
        assertEquals("ACTIVA", resultado.getEstado());
        verify(anotacionRepository).save(any(Anotacion.class));
    }

    // TEST: crear anotación sin requiereCitacion ni estado, deben tomar valores por defecto
    @Test
    void crear_sinRequiereCitacionNiEstado_debeUsarValoresPorDefecto() {
        AnotacionDTO request = new AnotacionDTO();
        request.setIdEstudiante(3);
        request.setIdDocente(1);
        request.setTipo("NEGATIVA");
        request.setDescripcion("Atraso reiterado");
        request.setFecha(LocalDate.of(2026, 6, 20));
        // requiereCitacion y estado quedan null intencionalmente

        Anotacion guardada = new Anotacion();
        guardada.setIdAnotacion(10);
        guardada.setIdEstudiante(3);
        guardada.setIdDocente(1);
        guardada.setTipo("NEGATIVA");
        guardada.setRequiereCitacion(false);
        guardada.setEstado("ACTIVA");

        when(anotacionRepository.save(any(Anotacion.class))).thenReturn(guardada);

        AnotacionDTO resultado = anotacionService.crear(request);

        assertFalse(resultado.getRequiereCitacion());
        assertEquals("ACTIVA", resultado.getEstado());
    }


    // TEST: actualizar anotación existente
    @Test
    void actualizar_cuandoExiste_debeActualizarYRetornarDTO() {
        AnotacionDTO request = new AnotacionDTO();
        request.setEstado("ANULADA");
        request.setDescripcion("Descripción corregida");

        when(anotacionRepository.findById(1)).thenReturn(Optional.of(anotacionMock));
        when(anotacionRepository.save(any(Anotacion.class))).thenReturn(anotacionMock);

        Optional<AnotacionDTO> resultado = anotacionService.actualizar(1, request);

        assertTrue(resultado.isPresent());
        verify(anotacionRepository).findById(1);
        verify(anotacionRepository).save(any(Anotacion.class));
    }

    // TEST: actualizar anotación que no existe
    @Test
    void actualizar_cuandoNoExiste_debeRetornarVacio() {
        AnotacionDTO request = new AnotacionDTO();
        request.setEstado("ANULADA");

        when(anotacionRepository.findById(999)).thenReturn(Optional.empty());

        Optional<AnotacionDTO> resultado = anotacionService.actualizar(999, request);

        assertFalse(resultado.isPresent());
        verify(anotacionRepository, never()).save(any(Anotacion.class));
    }

    // TEST: eliminar anotación delega correctamente al repository
    @Test
    void eliminar_debeLlamarADeleteById() {
        anotacionService.eliminar(1);

        verify(anotacionRepository, times(1)).deleteById(1);
    }
}