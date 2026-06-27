package com.colegio.establecimiento.repository;

import com.colegio.establecimiento.model.Comuna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Integer> {

    List<Comuna> findByIdRegion(Integer idRegion);
}
