package com.colegio.academico.repository;

import com.colegio.academico.model.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
    List<Evaluacion> findByIdCurso(Long idCurso);
}
