package com.colegio.establecimiento.repository;

import com.colegio.establecimiento.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {

    List<Curso> findByIdEstablecimiento(Integer idEstablecimiento);

    Optional<Curso> findByIdCursoAndIdEstablecimiento(Integer idCurso, Integer idEstablecimiento);
}
