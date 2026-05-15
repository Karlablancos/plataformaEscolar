package com.colegio.usuario.repository;

import com.colegio.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);
    boolean existsByUsername(String username);
    Optional<Usuario> findByUsernameAndIdEstablecimiento(
            String username,
            Integer idEstablecimiento
    );
}