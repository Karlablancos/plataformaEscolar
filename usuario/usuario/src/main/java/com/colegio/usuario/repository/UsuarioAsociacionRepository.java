package com.colegio.usuario.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UsuarioAsociacionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean tieneAsociaciones(Integer idUsuario) {
        Boolean resultado = (Boolean) entityManager.createNativeQuery("""
                SELECT (
                    EXISTS (SELECT 1 FROM docente WHERE id_usuario = :idUsuario)
                    OR EXISTS (SELECT 1 FROM estudiante WHERE id_usuario = :idUsuario)
                    OR EXISTS (SELECT 1 FROM apoderado WHERE id_usuario = :idUsuario)
                    OR EXISTS (
                        SELECT 1 FROM mensaje
                        WHERE id_remitente = :idUsuario OR id_destinatario = :idUsuario
                    )
                    OR EXISTS (SELECT 1 FROM mensaje_destinatario WHERE id_usuario = :idUsuario)
                    OR EXISTS (SELECT 1 FROM documento_estudiante WHERE subido_por = :idUsuario)
                )
                """)
                .setParameter("idUsuario", idUsuario)
                .getSingleResult();

        return Boolean.TRUE.equals(resultado);
    }

    @Transactional
    public void desactivarRegistrosAsociados(Integer idUsuario) {
        entityManager.createNativeQuery(
                        "UPDATE docente SET estado = 'INACTIVO' WHERE id_usuario = :idUsuario")
                .setParameter("idUsuario", idUsuario)
                .executeUpdate();

        entityManager.createNativeQuery(
                        "UPDATE estudiante SET estado = 'INACTIVO' WHERE id_usuario = :idUsuario")
                .setParameter("idUsuario", idUsuario)
                .executeUpdate();

        entityManager.createNativeQuery(
                        "UPDATE apoderado SET estado = 'INACTIVO' WHERE id_usuario = :idUsuario")
                .setParameter("idUsuario", idUsuario)
                .executeUpdate();
    }
}
