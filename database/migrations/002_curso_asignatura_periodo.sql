-- Ejecutar en BD existente:
-- docker exec -i colegio-postgres psql -U postgres -d colegio_db < database/migrations/002_curso_asignatura_periodo.sql

ALTER TABLE curso_asignatura
    ADD COLUMN IF NOT EXISTS id_periodo INT;

UPDATE curso_asignatura ca
SET id_periodo = (
    SELECT pa.id_periodo
    FROM periodo_academico pa
    INNER JOIN curso c ON c.id_curso = ca.id_curso
    WHERE pa.id_establecimiento = c.id_establecimiento
      AND pa.anio = c.anio_academico
    ORDER BY pa.fecha_inicio
    LIMIT 1
)
WHERE ca.id_periodo IS NULL;

ALTER TABLE curso_asignatura
    ALTER COLUMN id_periodo SET NOT NULL;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_ca_periodo'
    ) THEN
        ALTER TABLE curso_asignatura
            ADD CONSTRAINT fk_ca_periodo
            FOREIGN KEY (id_periodo) REFERENCES periodo_academico(id_periodo);
    END IF;
END $$;

CREATE UNIQUE INDEX IF NOT EXISTS uq_curso_asignatura_periodo_activo
    ON curso_asignatura (id_curso, id_asignatura, id_periodo)
    WHERE estado = 'ACTIVO';
