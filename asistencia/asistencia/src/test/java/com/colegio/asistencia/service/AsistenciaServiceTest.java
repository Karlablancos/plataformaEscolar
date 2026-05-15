package com.colegio.asistencia.service;

import com.colegio.asistencia.dto.AsistenciaDTO;
import com.colegio.asistencia.model.Asistencia;
import com.colegio.asistencia.repository.AsistenciaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsistenciaServiceTest {

    @Mock
    private AsistenciaRepository repository;

    @InjectMocks
    private AsistenciaService service;

    private Asistencia asistenciaEjemplo() {
        Asistencia a = new Asistencia();
        a.setIdAsistencia(1);
        a.setIdEstudiante(5);
        a.setIdHorario(2);
        a.setFecha(LocalDate.of(2026, 5, 14));
        a.setEstadoAsistencia("PRESENTE");
        a.setJustificada(false);
        return a;
    }

    @Test
    void obtenerPorEstudiante_debeRetornarListaDTOs() {
        when(repository.findByIdEstudiante(5))
                .thenReturn(List.of(asistenciaEjemplo()));

        List<AsistenciaDTO> resultado = service.obtenerPorEstudiante(5);

        assertEquals(1, resultado.size());
        assertEquals(5, resultado.get(0).getIdEstudiante());
        assertEquals("PRESENTE", resultado.get(0).getEstadoAsistencia());
        verify(repository).findByIdEstudiante(5);
    }

    @Test
    void obtenerPorEstudiante_debeRetornarListaVaciaSiNoHayRegistros() {
        when(repository.findByIdEstudiante(99)).thenReturn(List.of());

        List<AsistenciaDTO> resultado = service.obtenerPorEstudiante(99);

        assertTrue(resultado.isEmpty());
        verify(repository).findByIdEstudiante(99);
    }

    @Test
    void obtenerPorEstudianteYFecha_debeRetornarListaDTOs() {
        LocalDate fecha = LocalDate.of(2026, 5, 14);
        when(repository.findByIdEstudianteAndFecha(5, fecha))
                .thenReturn(List.of(asistenciaEjemplo()));

        List<AsistenciaDTO> resultado = service.obtenerPorEstudianteYFecha(5, fecha);

        assertEquals(1, resultado.size());
        assertEquals(fecha, resultado.get(0).getFecha());
        verify(repository).findByIdEstudianteAndFecha(5, fecha);
    }

    @Test
    void obtenerPorEstudianteYFecha_debeRetornarListaVaciaSiFechaNoTieneRegistros() {
        LocalDate fecha = LocalDate.of(2026, 1, 1);
        when(repository.findByIdEstudianteAndFecha(5, fecha)).thenReturn(List.of());

        List<AsistenciaDTO> resultado = service.obtenerPorEstudianteYFecha(5, fecha);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void registrar_debeGuardarYRetornarDTO() {
        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setIdEstudiante(5);
        dto.setIdHorario(2);
        dto.setFecha(LocalDate.of(2026, 5, 14));
        dto.setEstadoAsistencia("AUSENTE");
        dto.setJustificada(true);
        dto.setObservacion("Certificado médico");

        Asistencia guardada = asistenciaEjemplo();
        guardada.setEstadoAsistencia("AUSENTE");
        guardada.setJustificada(true);
        guardada.setObservacion("Certificado médico");

        when(repository.save(any(Asistencia.class))).thenReturn(guardada);

        AsistenciaDTO resultado = service.registrar(dto);

        assertEquals("AUSENTE", resultado.getEstadoAsistencia());
        assertTrue(resultado.getJustificada());
        assertEquals("Certificado médico", resultado.getObservacion());
        verify(repository).save(any(Asistencia.class));
    }

    @Test
    void registrar_debeMappearTodosLosCamposDelDTO() {
        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setIdEstudiante(3);
        dto.setIdHorario(7);
        dto.setFecha(LocalDate.of(2026, 5, 14));
        dto.setEstadoAsistencia("ATRASADO");
        dto.setJustificada(false);

        Asistencia guardada = new Asistencia();
        guardada.setIdAsistencia(10);
        guardada.setIdEstudiante(3);
        guardada.setIdHorario(7);
        guardada.setFecha(LocalDate.of(2026, 5, 14));
        guardada.setEstadoAsistencia("ATRASADO");
        guardada.setJustificada(false);

        when(repository.save(any(Asistencia.class))).thenReturn(guardada);

        AsistenciaDTO resultado = service.registrar(dto);

        assertEquals(10, resultado.getIdAsistencia());
        assertEquals(3, resultado.getIdEstudiante());
        assertEquals(7, resultado.getIdHorario());
        assertEquals("ATRASADO", resultado.getEstadoAsistencia());
        assertFalse(resultado.getJustificada());
    }
}
