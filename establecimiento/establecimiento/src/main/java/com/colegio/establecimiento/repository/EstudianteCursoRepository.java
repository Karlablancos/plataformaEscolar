package com.colegio.establecimiento.repository;

import com.colegio.establecimiento.model.EstudianteCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteCursoRepository extends JpaRepository<EstudianteCurso, Integer> {

    List<EstudianteCurso> findByIdCursoAndEstado(Integer idCurso, String estado);

    Optional<EstudianteCurso> findByIdEstudianteAndEstado(Integer idEstudiante, String estado);

    Optional<EstudianteCurso> findByIdEstudianteAndIdCurso(Integer idEstudiante, Integer idCurso);

    List<EstudianteCurso> findByIdEstudianteInAndEstado(List<Integer> idEstudiantes, String estado);
}
