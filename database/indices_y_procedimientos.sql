-- ============================================================
-- ÍNDICES Y PROCEDIMIENTOS ALMACENADOS
-- Sistema de Gestión Escolar - Colegio Bernardo O'Higgins
-- Fundamento: Optimización de consultas y lógica de negocio
-- Decreto N°67/2018 - Ley de Subvenciones MINEDUC
-- ============================================================


-- ============================================================
-- ÍNDICES ESTRATÉGICOS
-- Optimizan las consultas más frecuentes del sistema
-- ============================================================

-- Índice 1: Búsqueda de usuario por username y establecimiento
-- Fundamento: Primera validación del login (RBD + username)
CREATE INDEX IF NOT EXISTS idx_usuario_username
    ON usuario(username);

CREATE INDEX IF NOT EXISTS idx_usuario_establecimiento
    ON usuario(id_establecimiento);

-- Índice 2: Búsqueda de establecimiento por RBD
-- Fundamento: Validación del RBD en el Gateway (primera puerta del sistema)
CREATE INDEX IF NOT EXISTS idx_establecimiento_rbd
    ON establecimiento(rbd);

-- Índice 3: Consultas de asistencia por estudiante y fecha
-- Fundamento: Cálculo de porcentaje de asistencia (Ley de Subvenciones)
-- Con 500 alumnos y un año escolar = ~720.000 filas sin índice = full scan
CREATE INDEX IF NOT EXISTS idx_asistencia_estudiante_fecha
    ON asistencia(id_estudiante, fecha);

-- Índice 4: Consultas de notas por estudiante
-- Fundamento: Cálculo de promedios ponderados (Decreto 67/2018)
CREATE INDEX IF NOT EXISTS idx_nota_estudiante
    ON nota(id_estudiante);

CREATE INDEX IF NOT EXISTS idx_nota_evaluacion
    ON nota(id_evaluacion);

-- Índice 5: Consultas de horario por curso y día
-- Fundamento: Registro de asistencia diaria por bloque horario
CREATE INDEX IF NOT EXISTS idx_horario_curso_dia
    ON horario(id_curso, dia_semana);

-- Índice 6: Búsqueda de estudiante por RUT
-- Fundamento: Identificación única de estudiante en operaciones críticas
CREATE INDEX IF NOT EXISTS idx_estudiante_rut
    ON estudiante(rut);

CREATE INDEX IF NOT EXISTS idx_docente_rut
    ON docente(rut);


-- ============================================================
-- PROCEDIMIENTO 1: Calcular promedio ponderado de notas
-- Fundamento: Decreto N°67/2018 - Evaluación, Calificación y Promoción
-- Calcula el promedio ponderado de un estudiante en una asignatura
-- ============================================================

CREATE OR REPLACE FUNCTION calcular_promedio_estudiante(
    p_id_estudiante     INT,
    p_id_asignatura     INT,
    p_id_periodo        INT
)
RETURNS NUMERIC(5,2) AS $$
DECLARE
    v_promedio          NUMERIC(5,2);
    v_suma_ponderada    NUMERIC(10,2) := 0;
    v_suma_ponderacion  NUMERIC(10,2) := 0;
BEGIN
    -- Suma ponderada: (nota × ponderación) / suma de ponderaciones
    -- Según Decreto 67/2018: el promedio debe ser ponderado
    SELECT
        SUM(n.calificacion * e.ponderacion),
        SUM(e.ponderacion)
    INTO
        v_suma_ponderada,
        v_suma_ponderacion
    FROM nota n
    JOIN evaluacion e ON n.id_evaluacion = e.id_evaluacion
    WHERE n.id_estudiante   = p_id_estudiante
      AND e.id_asignatura   = p_id_asignatura
      AND e.id_periodo      = p_id_periodo
      AND n.calificacion    IS NOT NULL;

    -- Si no hay notas registradas retorna NULL
    IF v_suma_ponderacion = 0 OR v_suma_ponderacion IS NULL THEN
        RETURN NULL;
    END IF;

    v_promedio := ROUND(v_suma_ponderada / v_suma_ponderacion, 1);

    RETURN v_promedio;
END;
$$ LANGUAGE plpgsql;


-- ============================================================
-- PROCEDIMIENTO 2: Calcular porcentaje de asistencia
-- Fundamento: Ley de Subvenciones - mínimo 85% para promoción
-- Calcula el porcentaje de asistencia de un estudiante en un período
-- ============================================================

CREATE OR REPLACE FUNCTION calcular_asistencia_estudiante(
    p_id_estudiante     INT,
    p_id_establecimiento INT,
    p_anio              INT
)
RETURNS TABLE (
    total_clases        INT,
    clases_presentes    INT,
    clases_ausentes     INT,
    porcentaje          NUMERIC(5,2),
    en_riesgo           BOOLEAN
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        COUNT(a.id_asistencia)::INT                                         AS total_clases,
        COUNT(CASE WHEN a.estado_asistencia IN ('PRESENTE', 'ATRASADO')
                   THEN 1 END)::INT                                         AS clases_presentes,
        COUNT(CASE WHEN a.estado_asistencia = 'AUSENTE'
                   THEN 1 END)::INT                                         AS clases_ausentes,
        ROUND(
            COUNT(CASE WHEN a.estado_asistencia IN ('PRESENTE', 'ATRASADO')
                       THEN 1 END)::NUMERIC
            / NULLIF(COUNT(a.id_asistencia), 0) * 100
        , 2)                                                                AS porcentaje,
        -- En riesgo si asistencia < 85% (Ley de Subvenciones MINEDUC)
        (ROUND(
            COUNT(CASE WHEN a.estado_asistencia IN ('PRESENTE', 'ATRASADO')
                       THEN 1 END)::NUMERIC
            / NULLIF(COUNT(a.id_asistencia), 0) * 100
        , 2) < 85.00)                                                       AS en_riesgo
    FROM asistencia a
    JOIN horario h ON a.id_horario = h.id_horario
    JOIN curso c   ON h.id_curso = c.id_curso
    WHERE a.id_estudiante       = p_id_estudiante
      AND c.id_establecimiento  = p_id_establecimiento
      AND c.anio_academico      = p_anio;
END;
$$ LANGUAGE plpgsql;


-- ============================================================
-- VERIFICACIÓN - Ejecuta estas consultas para probar
-- ============================================================

-- Prueba 1: Promedio de Katherine (id=2) en Matemática (id=1) Primer Semestre (id=1)
-- SELECT calcular_promedio_estudiante(2, 1, 1);

-- Prueba 2: Asistencia de Karla (id=1) en Colegio Bernardo O'Higgins (id=1) año 2026
-- SELECT * FROM calcular_asistencia_estudiante(1, 1, 2026);
