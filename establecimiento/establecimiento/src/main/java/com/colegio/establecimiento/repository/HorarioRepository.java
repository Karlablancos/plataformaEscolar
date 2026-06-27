package com.colegio.establecimiento.repository;

import com.colegio.establecimiento.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Integer> {

    List<Horario> findByIdCurso(Integer idCurso);

    List<Horario> findByIdCursoAndIdPeriodo(Integer idCurso, Integer idPeriodo);

    Optional<Horario> findFirstByIdCursoAndIdAsignaturaAndIdPeriodo(
            Integer idCurso, Integer idAsignatura, Integer idPeriodo);
}
