package com.colegio.asistencia.repository;

import com.colegio.asistencia.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AsistenciaRepository
        extends JpaRepository<Asistencia, Integer> {

    // Buscar todas las asistencias de un estudiante
    List<Asistencia> findByIdEstudiante(Integer idEstudiante);

    // Buscar asistencias de un estudiante en una fecha
    List<Asistencia> findByIdEstudianteAndFecha(
            Integer idEstudiante,
            LocalDate fecha
    );

}