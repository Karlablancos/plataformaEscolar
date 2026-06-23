package com.colegio.establecimiento.repository;

import com.colegio.establecimiento.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Integer> {

    List<Docente> findByIdEstablecimiento(Integer idEstablecimiento);

    Optional<Docente> findByIdDocenteAndIdEstablecimiento(Integer idDocente, Integer idEstablecimiento);
}
