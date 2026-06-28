package com.colegio.establecimiento.repository;

import com.colegio.establecimiento.model.PeriodoAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodoAcademicoRepository extends JpaRepository<PeriodoAcademico, Integer> {

    List<PeriodoAcademico> findByIdEstablecimientoAndAnioOrderByFechaInicioAsc(
            Integer idEstablecimiento, Integer anio);

    Optional<PeriodoAcademico> findByIdPeriodoAndIdEstablecimiento(
            Integer idPeriodo, Integer idEstablecimiento);
}
