package com.colegio.establecimiento.repository;

import com.colegio.establecimiento.model.Establecimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EstablecimientoRepository extends JpaRepository<Establecimiento, Integer> {

    Optional<Establecimiento> findByRbd(String rbd);

    List<Establecimiento> findByEstado(String estado);

}