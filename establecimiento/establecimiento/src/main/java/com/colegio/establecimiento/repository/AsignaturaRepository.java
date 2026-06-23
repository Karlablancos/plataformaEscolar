package com.colegio.establecimiento.repository;

import com.colegio.establecimiento.model.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura, Integer> {

    List<Asignatura> findByIdEstablecimiento(Integer idEstablecimiento);

    Optional<Asignatura> findByIdAsignaturaAndIdEstablecimiento(
            Integer idAsignatura, Integer idEstablecimiento);
}
