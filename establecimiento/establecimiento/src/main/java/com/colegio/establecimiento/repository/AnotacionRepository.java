package com.colegio.establecimiento.repository;

import com.colegio.establecimiento.model.Anotacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnotacionRepository extends JpaRepository<Anotacion, Integer> {

    List<Anotacion> findByIdEstudiante(Integer idEstudiante);

    List<Anotacion> findByIdDocente(Integer idDocente);


    List<Anotacion> findByRequiereCitacionTrueAndFechaCitacionIsNull();
}