package com.colegio.establecimiento.repository;

import com.colegio.establecimiento.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Integer> {

    List<Sala> findByIdEstablecimiento(Integer idEstablecimiento);

    Optional<Sala> findByIdSalaAndIdEstablecimiento(Integer idSala, Integer idEstablecimiento);
}
