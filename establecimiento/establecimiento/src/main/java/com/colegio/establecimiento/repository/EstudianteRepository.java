package com.colegio.establecimiento.repository;

import com.colegio.establecimiento.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {

    List<Estudiante> findByIdEstablecimiento(Integer idEstablecimiento);
}
