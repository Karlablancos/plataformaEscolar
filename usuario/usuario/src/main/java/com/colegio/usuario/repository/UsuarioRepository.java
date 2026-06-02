package com.colegio.usuario.repository;

import com.colegio.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByCorreoElectronico(String correoElectronico);

    boolean existsByUsername(String username);

    Optional<Usuario> findByUsernameAndIdEstablecimiento(
            String username,
            Integer idEstablecimiento);

    List<Usuario> findByIdEstablecimiento(Integer idEstablecimiento);

    @Query(value = "SELECT nombre_rol FROM rol WHERE id_rol = :idRol", nativeQuery = true)
    String findNombreRolById(@Param("idRol") Integer idRol);
}