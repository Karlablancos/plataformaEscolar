package com.colegio.establecimiento.repository;

import com.colegio.establecimiento.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    Optional<Rol> findByNombreRolIgnoreCase(String nombreRol);
}
