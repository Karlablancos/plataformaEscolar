-- Ejecutar en BD existente si curso no tiene aún la columna de profesor jefe:
-- docker exec -i colegio-postgres psql -U postgres -d colegio_db < database/migrations/001_curso_profesor_jefe.sql

ALTER TABLE curso
    ADD COLUMN IF NOT EXISTS id_docente_jefe INT;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_curso_profesor_jefe'
    ) THEN
        ALTER TABLE curso
            ADD CONSTRAINT fk_curso_profesor_jefe
            FOREIGN KEY (id_docente_jefe) REFERENCES docente(id_docente);
    END IF;
END $$;
