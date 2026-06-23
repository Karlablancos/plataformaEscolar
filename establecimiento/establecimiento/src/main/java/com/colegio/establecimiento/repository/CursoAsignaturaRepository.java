package com.colegio.establecimiento.repository;

import com.colegio.establecimiento.model.CursoAsignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CursoAsignaturaRepository extends JpaRepository<CursoAsignatura, Integer> {

    List<CursoAsignatura> findByIdCursoAndEstado(Integer idCurso, String estado);

    Optional<CursoAsignatura> findByIdCursoAndIdAsignatura(Integer idCurso, Integer idAsignatura);
}
