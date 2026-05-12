-- ============================================================
-- SCRIPT PL/pgSQL - Triggers Críticos
-- Sistema de Gestión Escolar
-- ============================================================


-- ============================================================
-- TRIGGER 1: Validación de nota entre 1.0 y 7.0
-- ============================================================
CREATE OR REPLACE FUNCTION validar_nota()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.calificacion < 1.0 OR NEW.calificacion > 7.0 THEN
        RAISE EXCEPTION 'La calificación % no es válida. Debe estar entre 1.0 y 7.0 (Decreto 67/2018).', NEW.calificacion;
    END IF;

    -- Si pasa la validación, permite la operación
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_validar_nota
BEFORE INSERT OR UPDATE ON nota
FOR EACH ROW
EXECUTE FUNCTION validar_nota();


-- ============================================================
-- TRIGGER 2: Bloqueo de asistencia en días no hábiles
-- ============================================================
CREATE OR REPLACE FUNCTION validar_dia_habil()
RETURNS TRIGGER AS $$
DECLARE
    v_id_establecimiento    INT;
    v_id_calendario         INT;
    v_es_feriado            BOOLEAN;
    v_fecha_inicio          DATE;
    v_fecha_termino         DATE;
    v_dia_semana            INT;
BEGIN
    SELECT id_establecimiento
    INTO v_id_establecimiento
    FROM estudiante
    WHERE id_estudiante = NEW.id_estudiante;

    SELECT id_calendario, fecha_inicio_clases, fecha_termino_clases
    INTO v_id_calendario, v_fecha_inicio, v_fecha_termino
    FROM calendario_escolar
    WHERE id_establecimiento = v_id_establecimiento
      AND anio = EXTRACT(YEAR FROM NEW.fecha);

    IF NEW.fecha < v_fecha_inicio OR NEW.fecha > v_fecha_termino THEN
        RAISE EXCEPTION 'No se puede registrar asistencia el % porque está fuera del calendario escolar.', NEW.fecha;
    END IF;

    -- Verifica fin de semana (0 = domingo, 6 = sábado)
    v_dia_semana := EXTRACT(DOW FROM NEW.fecha);
    IF v_dia_semana = 0 OR v_dia_semana = 6 THEN
        RAISE EXCEPTION 'No se puede registrar asistencia el % porque es fin de semana.', NEW.fecha;
    END IF;

    SELECT EXISTS (
        SELECT 1
        FROM calendario_feriado
        WHERE id_calendario = v_id_calendario
          AND fecha = NEW.fecha
    ) INTO v_es_feriado;

    IF v_es_feriado THEN
        RAISE EXCEPTION 'No se puede registrar asistencia el % porque es un día feriado.', NEW.fecha;
    END IF;

    -- Si pasa todas las validaciones, permite la operación
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_validar_dia_habil
BEFORE INSERT OR UPDATE ON asistencia
FOR EACH ROW
EXECUTE FUNCTION validar_dia_habil();
