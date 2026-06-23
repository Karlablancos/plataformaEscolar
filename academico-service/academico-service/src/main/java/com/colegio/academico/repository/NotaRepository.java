package com.colegio.academico.repository;

import com.colegio.academico.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotaRepository extends JpaRepository<Nota, Long> {
    List<Nota> findByIdEstudiante(Long idEstudiante);
    List<Nota> findByIdEstudianteAndEvaluacion_IdAsignatura(Long idEstudiante, Long idAsignatura);
}
