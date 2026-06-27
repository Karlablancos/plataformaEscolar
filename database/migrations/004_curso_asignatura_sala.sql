ALTER TABLE curso_asignatura
    ADD COLUMN IF NOT EXISTS id_sala INT,
    ADD CONSTRAINT fk_ca_sala FOREIGN KEY (id_sala) REFERENCES sala(id_sala);
