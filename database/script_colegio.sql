-- ============================================================
-- SCRIPT DDL COMPLETO - PostgreSQL
-- Sistema de Gestión Escolar - Colegio Bernardo O'Higgins
-- Actualizado: Junio 2026
-- ============================================================
 
 
-- ============================================================
-- TIPO_ESTABLECIMIENTO
-- ============================================================
CREATE TABLE tipo_establecimiento (
    id_tipo_estab               SERIAL          PRIMARY KEY,
    dependencia                 VARCHAR(100)    NOT NULL,
    financiamiento              VARCHAR(100)    NOT NULL,
    puede_cobrar_mensualidad    BOOLEAN         NOT NULL,
    normativa_aplicable         VARCHAR(255),
    descripcion                 TEXT
);
 
-- ============================================================
-- REGION
-- ============================================================
CREATE TABLE region (
    id_region           SERIAL          PRIMARY KEY,
    nombre_region       VARCHAR(100)    NOT NULL
);
 
-- ============================================================
-- COMUNA
-- ============================================================
CREATE TABLE comuna (
    id_comuna           SERIAL          PRIMARY KEY,
    nombre_comuna       VARCHAR(100)    NOT NULL,
    id_region           INT             NOT NULL,
    CONSTRAINT fk_comuna_region FOREIGN KEY (id_region) REFERENCES region(id_region)
);
 
-- ============================================================
-- ESTABLECIMIENTO
-- ============================================================
CREATE TABLE establecimiento (
    id_establecimiento  SERIAL          PRIMARY KEY,
    rbd                 VARCHAR(20)     NOT NULL,
    nombre              VARCHAR(200)    NOT NULL,
    id_tipo_estab       INT             NOT NULL,
    sostenedor          VARCHAR(200)    NOT NULL,
    director            VARCHAR(200)    NOT NULL,
    calle               VARCHAR(200)    NOT NULL,
    numero              VARCHAR(20)     NOT NULL,
    id_comuna           INT             NOT NULL,
    telefono            VARCHAR(20),
    correo_electronico  VARCHAR(150)    NOT NULL,
    modo_aula           VARCHAR(50)     NOT NULL,
    estado              VARCHAR(20),
    CONSTRAINT fk_estab_tipo   FOREIGN KEY (id_tipo_estab) REFERENCES tipo_establecimiento(id_tipo_estab),
    CONSTRAINT fk_estab_comuna FOREIGN KEY (id_comuna)     REFERENCES comuna(id_comuna)
);
 
-- ============================================================
-- CALENDARIO_ESCOLAR
-- ============================================================
CREATE TABLE calendario_escolar (
    id_calendario               SERIAL          PRIMARY KEY,
    id_establecimiento          INT             NOT NULL,
    anio                        INT             NOT NULL,
    fecha_inicio_clases         DATE            NOT NULL,
    fecha_termino_clases        DATE            NOT NULL,
    inicio_vacaciones_invierno  DATE            NOT NULL,
    fin_vacaciones_invierno     DATE            NOT NULL,
    estado                      VARCHAR(20)     NOT NULL,
    CONSTRAINT fk_calendario_estab FOREIGN KEY (id_establecimiento) REFERENCES establecimiento(id_establecimiento)
);
 
-- ============================================================
-- CALENDARIO_FERIADO
-- ============================================================
CREATE TABLE calendario_feriado (
    id_feriado          SERIAL          PRIMARY KEY,
    id_calendario       INT             NOT NULL,
    fecha               DATE            NOT NULL,
    descripcion         VARCHAR(100),
    CONSTRAINT fk_feriado_calendario FOREIGN KEY (id_calendario) REFERENCES calendario_escolar(id_calendario)
);
 
-- ============================================================
-- PERIODO_ACADEMICO
-- ============================================================
CREATE TABLE periodo_academico (
    id_periodo          SERIAL          PRIMARY KEY,
    id_establecimiento  INT             NOT NULL,
    anio                INT             NOT NULL,
    nombre_periodo      VARCHAR(50)     NOT NULL,
    fecha_inicio        DATE            NOT NULL,
    fecha_termino       DATE            NOT NULL,
    estado              VARCHAR(20)     NOT NULL,
    CONSTRAINT fk_periodo_estab FOREIGN KEY (id_establecimiento) REFERENCES establecimiento(id_establecimiento)
);
 
-- ============================================================
-- ROL
-- ============================================================
CREATE TABLE rol (
    id_rol              SERIAL          PRIMARY KEY,
    nombre_rol          VARCHAR(100)    NOT NULL,
    descripcion         TEXT
);
 
-- ============================================================
-- USUARIO
-- ============================================================
CREATE TABLE usuario (
    id_usuario          SERIAL          PRIMARY KEY,
    id_establecimiento  INT             NOT NULL,
    id_rol              INT             NOT NULL,
    username            VARCHAR(100)    NOT NULL,
    password_hash       VARCHAR(255)    NOT NULL,
    correo_electronico  VARCHAR(150)    NOT NULL,
    ultimo_acceso       TIMESTAMP,
    intentos_fallidos   INT             NOT NULL,
    bloqueado           BOOLEAN         NOT NULL,
    fecha_creacion      TIMESTAMP       NOT NULL,
    estado              VARCHAR(20)     NOT NULL,
    CONSTRAINT fk_usuario_estab FOREIGN KEY (id_establecimiento) REFERENCES establecimiento(id_establecimiento),
    CONSTRAINT fk_usuario_rol   FOREIGN KEY (id_rol)             REFERENCES rol(id_rol)
);
 
-- ============================================================
-- CATEGORIA_SNED
-- ============================================================
CREATE TABLE categoria_sned (
    id_categoria_sned   SERIAL          PRIMARY KEY,
    nombre              VARCHAR(100)    NOT NULL,
    descripcion         TEXT            NOT NULL,
    porcentaje_bono     NUMERIC(5,2)    NOT NULL,
    tiene_bono          BOOLEAN         NOT NULL
);
 
-- ============================================================
-- TIPO_CALIFICACION
-- ============================================================
CREATE TABLE tipo_calificacion (
    id_tipo_calificacion SERIAL         PRIMARY KEY,
    nombre              VARCHAR(100)    NOT NULL,
    escala              VARCHAR(50)     NOT NULL,
    entra_promedio      BOOLEAN         NOT NULL,
    minimo_aprobacion   NUMERIC(5,2)    NOT NULL
);
 
-- ============================================================
-- ASIGNATURA
-- ============================================================
CREATE TABLE asignatura (
    id_asignatura           SERIAL          PRIMARY KEY,
    id_establecimiento      INT             NOT NULL,
    nombre                  VARCHAR(150)    NOT NULL,
    codigo                  VARCHAR(20)     NOT NULL,
    id_tipo_calificacion    INT             NOT NULL,
    estado                  VARCHAR(20)     NOT NULL,
    CONSTRAINT fk_asignatura_estab   FOREIGN KEY (id_establecimiento) REFERENCES establecimiento(id_establecimiento),
    CONSTRAINT fk_asignatura_tipocal FOREIGN KEY (id_tipo_calificacion) REFERENCES tipo_calificacion(id_tipo_calificacion)
);
 
-- ============================================================
-- PLAN_ESTUDIO
-- ============================================================
CREATE TABLE plan_estudio (
    id_plan_estudio     SERIAL          PRIMARY KEY,
    id_establecimiento  INT             NOT NULL,
    tipo_ensenanza      VARCHAR(100)    NOT NULL,
    nivel               INT             NOT NULL,
    anio                INT             NOT NULL,
    estado              VARCHAR(20)     NOT NULL,
    CONSTRAINT fk_plan_estab FOREIGN KEY (id_establecimiento) REFERENCES establecimiento(id_establecimiento)
);
 
-- ============================================================
-- PLAN_ESTUDIO_ASIGNATURA
-- ============================================================
CREATE TABLE plan_estudio_asignatura (
    id_plan_estudio_asignatura  SERIAL      PRIMARY KEY,
    id_plan_estudio             INT         NOT NULL,
    id_asignatura               INT         NOT NULL,
    horas_semanales             INT         NOT NULL,
    CONSTRAINT fk_pea_plan FOREIGN KEY (id_plan_estudio) REFERENCES plan_estudio(id_plan_estudio),
    CONSTRAINT fk_pea_asig FOREIGN KEY (id_asignatura)   REFERENCES asignatura(id_asignatura)
);
 
-- ============================================================
-- DOCENTE
-- ============================================================
CREATE TABLE docente (
    id_docente              SERIAL          PRIMARY KEY,
    id_establecimiento      INT             NOT NULL,
    id_usuario              INT             NOT NULL,
    id_categoria_sned       INT,
    anio_evaluacion_sned    INT             NOT NULL,
    rut                     CHAR(8)         NOT NULL,
    dv                      CHAR(1)         NOT NULL,
    nombres                 VARCHAR(100)    NOT NULL,
    apellido_paterno        VARCHAR(100)    NOT NULL,
    apellido_materno        VARCHAR(100)    NOT NULL,
    fecha_nacimiento        DATE            NOT NULL,
    correo_electronico      VARCHAR(150)    NOT NULL,
    telefono                VARCHAR(20)     NOT NULL,
    calle                   VARCHAR(200)    NOT NULL,
    numero                  VARCHAR(20)     NOT NULL,
    id_comuna               INT             NOT NULL,
    fecha_contratacion      DATE            NOT NULL,
    estado                  VARCHAR(20)     NOT NULL,
    CONSTRAINT fk_docente_estab   FOREIGN KEY (id_establecimiento) REFERENCES establecimiento(id_establecimiento),
    CONSTRAINT fk_docente_usuario FOREIGN KEY (id_usuario)         REFERENCES usuario(id_usuario),
    CONSTRAINT fk_docente_sned    FOREIGN KEY (id_categoria_sned)  REFERENCES categoria_sned(id_categoria_sned),
    CONSTRAINT fk_docente_comuna  FOREIGN KEY (id_comuna)          REFERENCES comuna(id_comuna)
);
 
-- ============================================================
-- TIPO_NEE
-- ============================================================
CREATE TABLE tipo_nee (
    id_tipo_nee             SERIAL          PRIMARY KEY,
    nombre                  VARCHAR(100)    NOT NULL,
    nombre_completo         VARCHAR(200)    NOT NULL,
    protocolo_evaluacion    TEXT,
    requiere_pie            BOOLEAN
);
 
-- ============================================================
-- ESTUDIANTE
-- ============================================================
CREATE TABLE estudiante (
    id_estudiante       SERIAL          PRIMARY KEY,
    id_establecimiento  INT             NOT NULL,
    id_usuario          INT             NOT NULL,
    rut                 CHAR(8)         NOT NULL,
    dv                  CHAR(1)         NOT NULL,
    nombres             VARCHAR(100)    NOT NULL,
    apellido_paterno    VARCHAR(100)    NOT NULL,
    apellido_materno    VARCHAR(100)    NOT NULL,
    fecha_nacimiento    DATE            NOT NULL,
    correo_electronico  VARCHAR(150)    NOT NULL,
    telefono            VARCHAR(20)     NOT NULL,
    calle               VARCHAR(200)    NOT NULL,
    numero              VARCHAR(20)     NOT NULL,
    id_comuna           INT             NOT NULL,
    colegio_procedente  VARCHAR(200)    NOT NULL,
    fecha_matricula     DATE            NOT NULL,
    prioritario         BOOLEAN         NOT NULL,
    preferente          BOOLEAN         NOT NULL,
    tiene_nee           BOOLEAN         NOT NULL,
    id_tipo_nee         INT,
    en_pie              BOOLEAN         NOT NULL,
    estado              VARCHAR(20)     NOT NULL,
    CONSTRAINT fk_est_estab   FOREIGN KEY (id_establecimiento) REFERENCES establecimiento(id_establecimiento),
    CONSTRAINT fk_est_usuario FOREIGN KEY (id_usuario)         REFERENCES usuario(id_usuario),
    CONSTRAINT fk_est_comuna  FOREIGN KEY (id_comuna)          REFERENCES comuna(id_comuna),
    CONSTRAINT fk_est_tiponee FOREIGN KEY (id_tipo_nee)        REFERENCES tipo_nee(id_tipo_nee)
);
 
-- ============================================================
-- APODERADO
-- ============================================================
CREATE TABLE apoderado (
    id_apoderado            SERIAL          PRIMARY KEY,
    id_usuario              INT             NOT NULL,
    rut                     CHAR(8)         NOT NULL,
    dv                      CHAR(1)         NOT NULL,
    nombres                 VARCHAR(100)    NOT NULL,
    apellido_paterno        VARCHAR(100)    NOT NULL,
    apellido_materno        VARCHAR(100)    NOT NULL,
    correo_electronico      VARCHAR(150),
    telefono_principal      VARCHAR(20)     NOT NULL,
    telefono_secundario     VARCHAR(20)     NOT NULL,
    calle                   VARCHAR(200)    NOT NULL,
    numero                  VARCHAR(20)     NOT NULL,
    id_comuna               INT             NOT NULL,
    parentesco              VARCHAR(50)     NOT NULL,
    fecha_registro          DATE            NOT NULL,
    estado                  VARCHAR(20)     NOT NULL,
    CONSTRAINT fk_apod_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_apod_comuna  FOREIGN KEY (id_comuna)  REFERENCES comuna(id_comuna)
);
 
-- ============================================================
-- ESTUDIANTE_APODERADO
-- ============================================================
CREATE TABLE estudiante_apoderado (
    id_estudiante_apoderado SERIAL          PRIMARY KEY,
    id_estudiante           INT             NOT NULL,
    id_apoderado            INT             NOT NULL,
    es_apoderado_titular    BOOLEAN         NOT NULL,
    fecha_asignacion        DATE            NOT NULL,
    CONSTRAINT fk_estapod_est  FOREIGN KEY (id_estudiante) REFERENCES estudiante(id_estudiante),
    CONSTRAINT fk_estapod_apod FOREIGN KEY (id_apoderado)  REFERENCES apoderado(id_apoderado)
);
 
-- ============================================================
-- CURSO
-- ============================================================
CREATE TABLE curso (
    id_curso            SERIAL          PRIMARY KEY,
    id_establecimiento  INT             NOT NULL,
    numero              INT             NOT NULL,
    letra               CHAR(1)         NOT NULL,
    tipo_ensenianza     VARCHAR(100)    NOT NULL,
    modalidad           VARCHAR(100)    NOT NULL,
    anio_academico      INT             NOT NULL,
    es_nivel_simce      BOOLEAN         NOT NULL,
    estado              VARCHAR(20)     NOT NULL,
    id_docente_jefe     INT,
    CONSTRAINT fk_curso_estab         FOREIGN KEY (id_establecimiento) REFERENCES establecimiento(id_establecimiento),
    CONSTRAINT fk_curso_profesor_jefe FOREIGN KEY (id_docente_jefe)    REFERENCES docente(id_docente)
);
 
-- ============================================================
-- SALA
-- ============================================================
CREATE TABLE sala (
    id_sala             SERIAL          PRIMARY KEY,
    numero              INT,
    nombre              VARCHAR(100)    NOT NULL,
    capacidad           INT             NOT NULL,
    tipo                VARCHAR(50)     NOT NULL,
    piso                INT             NOT NULL,
    estado              VARCHAR(20)     NOT NULL,
    id_establecimiento  INT             NOT NULL,
    CONSTRAINT fk_sala_estab FOREIGN KEY (id_establecimiento) REFERENCES establecimiento(id_establecimiento)
);
 
-- ============================================================
-- CURSO_ASIGNATURA
-- ============================================================
CREATE TABLE curso_asignatura (
    id_curso_asignatura     SERIAL          PRIMARY KEY,
    id_curso                INT             NOT NULL,
    id_asignatura           INT             NOT NULL,
    id_docente              INT             NOT NULL,
    id_periodo              INT             NOT NULL,
    horas_semanales         INT             NOT NULL,
    estado                  VARCHAR(20)     NOT NULL,
    id_sala                 INT,
    CONSTRAINT fk_ca_curso      FOREIGN KEY (id_curso)      REFERENCES curso(id_curso),
    CONSTRAINT fk_ca_asignatura FOREIGN KEY (id_asignatura) REFERENCES asignatura(id_asignatura),
    CONSTRAINT fk_ca_docente    FOREIGN KEY (id_docente)    REFERENCES docente(id_docente),
    CONSTRAINT fk_ca_periodo    FOREIGN KEY (id_periodo)    REFERENCES periodo_academico(id_periodo),
    CONSTRAINT fk_ca_sala       FOREIGN KEY (id_sala)       REFERENCES sala(id_sala)
);
 
-- ============================================================
-- HORARIO
-- ============================================================
CREATE TABLE horario (
    id_horario          SERIAL          PRIMARY KEY,
    id_curso            INT             NOT NULL,
    id_asignatura       INT             NOT NULL,
    id_docente          INT             NOT NULL,
    id_sala             INT             NOT NULL,
    id_periodo          INT,
    dia_semana          VARCHAR(15)     NOT NULL,
    hora_inicio         TIME            NOT NULL,
    hora_termino        TIME            NOT NULL,
    anio_academico      INT             NOT NULL,
    CONSTRAINT fk_horario_curso   FOREIGN KEY (id_curso)      REFERENCES curso(id_curso),
    CONSTRAINT fk_horario_asig    FOREIGN KEY (id_asignatura) REFERENCES asignatura(id_asignatura),
    CONSTRAINT fk_horario_doc     FOREIGN KEY (id_docente)    REFERENCES docente(id_docente),
    CONSTRAINT fk_horario_sala    FOREIGN KEY (id_sala)       REFERENCES sala(id_sala),
    CONSTRAINT fk_horario_periodo FOREIGN KEY (id_periodo)    REFERENCES periodo_academico(id_periodo)
);
 
-- ============================================================
-- ESTUDIANTE_CURSO
-- ============================================================
CREATE TABLE estudiante_curso (
    id_matricula        SERIAL          PRIMARY KEY,
    id_estudiante       INT             NOT NULL,
    id_curso            INT             NOT NULL,
    fecha_matricula     DATE            NOT NULL,
    estado              VARCHAR(20)     NOT NULL,
    CONSTRAINT fk_estcurso_est   FOREIGN KEY (id_estudiante) REFERENCES estudiante(id_estudiante),
    CONSTRAINT fk_estcurso_curso FOREIGN KEY (id_curso)      REFERENCES curso(id_curso)
);
 
-- ============================================================
-- ASISTENCIA
-- ============================================================
CREATE TABLE asistencia (
    id_asistencia       SERIAL          PRIMARY KEY,
    id_estudiante       INT             NOT NULL,
    id_horario          INT             NOT NULL,
    fecha               DATE            NOT NULL,
    estado_asistencia   VARCHAR(20)     NOT NULL,
    hora_llegada        TIME,
    justificada         BOOLEAN         NOT NULL,
    observacion         TEXT,
    CONSTRAINT fk_asist_est FOREIGN KEY (id_estudiante) REFERENCES estudiante(id_estudiante),
    CONSTRAINT fk_asist_hor FOREIGN KEY (id_horario)    REFERENCES horario(id_horario)
);
 
-- ============================================================
-- ANOTACION
-- ============================================================
CREATE TABLE anotacion (
    id_anotacion        SERIAL          PRIMARY KEY,
    id_estudiante       INT             NOT NULL,
    id_docente          INT             NOT NULL,
    tipo                VARCHAR(50)     NOT NULL,
    descripcion         TEXT            NOT NULL,
    fecha               DATE            NOT NULL,
    requiere_citacion   BOOLEAN         NOT NULL,
    fecha_citacion      DATE,
    estado              VARCHAR(20)     NOT NULL,
    CONSTRAINT fk_anot_est FOREIGN KEY (id_estudiante) REFERENCES estudiante(id_estudiante),
    CONSTRAINT fk_anot_doc FOREIGN KEY (id_docente)    REFERENCES docente(id_docente)
);
 
-- ============================================================
-- TIPO_EVALUACION
-- ============================================================
CREATE TABLE tipo_evaluacion (
    id_tipo_evaluacion  SERIAL          PRIMARY KEY,
    nombre              VARCHAR(100)    NOT NULL,
    puede_eliminarse    BOOLEAN,
    descripcion         TEXT            NOT NULL,
    peso_sugerido       NUMERIC(5,2)
);
 
-- ============================================================
-- EVALUACION
-- ============================================================
CREATE TABLE evaluacion (
    id_evaluacion       BIGSERIAL       PRIMARY KEY,
    id_curso            BIGINT          NOT NULL,
    id_asignatura       BIGINT          NOT NULL,
    id_docente          BIGINT          NOT NULL,
    id_tipo_evaluacion  INT             NOT NULL,
    id_periodo          INT,
    id_establecimiento  BIGINT          NOT NULL,
    nombre              VARCHAR(255)    NOT NULL,
    tipo                VARCHAR(255)    NOT NULL, 
    ponderacion         NUMERIC(5,2)    NOT NULL,
    fecha               DATE            NOT NULL,
    descripcion         TEXT,
    CONSTRAINT fk_eval_curso   FOREIGN KEY (id_curso)           REFERENCES curso(id_curso),
    CONSTRAINT fk_eval_asig    FOREIGN KEY (id_asignatura)      REFERENCES asignatura(id_asignatura),
    CONSTRAINT fk_eval_doc     FOREIGN KEY (id_docente)         REFERENCES docente(id_docente),
    CONSTRAINT fk_eval_tipo    FOREIGN KEY (id_tipo_evaluacion) REFERENCES tipo_evaluacion(id_tipo_evaluacion),
    CONSTRAINT fk_eval_periodo FOREIGN KEY (id_periodo)         REFERENCES periodo_academico(id_periodo)
);
 
-- ============================================================
-- NOTA
-- ============================================================
CREATE TABLE nota (
    id_nota                 BIGSERIAL       PRIMARY KEY,
    id_estudiante           BIGINT          NOT NULL,
    id_evaluacion           BIGINT          NOT NULL,
    id_establecimiento      BIGINT          NOT NULL,
    calificacion            NUMERIC(4,1)    NOT NULL,
    calificacion_conceptual VARCHAR(20)     NOT NULL,
    fecha_registro          DATE            NOT NULL,
    evaluacion_diferenciada BOOLEAN         NOT NULL,
    observacion             TEXT,
    CONSTRAINT fk_nota_est  FOREIGN KEY (id_estudiante) REFERENCES estudiante(id_estudiante),
    CONSTRAINT fk_nota_eval FOREIGN KEY (id_evaluacion) REFERENCES evaluacion(id_evaluacion)
);
 
-- ============================================================
-- MENSAJE
-- ============================================================
CREATE TABLE mensaje (
    id_mensaje          SERIAL          PRIMARY KEY,
    id_remitente        INT             NOT NULL,
    id_destinatario     INT             NOT NULL,
    asunto              VARCHAR(255)    NOT NULL,
    cuerpo              TEXT            NOT NULL,
    fecha_envio         TIMESTAMP       NOT NULL,
    fecha_lectura       TIMESTAMP,
    tipo                VARCHAR(50)     NOT NULL,
    leido               BOOLEAN         NOT NULL,
    CONSTRAINT fk_msj_remitente    FOREIGN KEY (id_remitente)    REFERENCES usuario(id_usuario),
    CONSTRAINT fk_msj_destinatario FOREIGN KEY (id_destinatario) REFERENCES usuario(id_usuario)
);
 
-- ============================================================
-- MENSAJE_DESTINATARIO
-- ============================================================
CREATE TABLE mensaje_destinatario (
    id_mensaje_des      SERIAL          PRIMARY KEY,
    id_mensaje          INT             NOT NULL,
    id_usuario          INT             NOT NULL,
    leido               BOOLEAN         NOT NULL,
    fecha_lectura       TIMESTAMP,
    CONSTRAINT fk_msjdes_msj FOREIGN KEY (id_mensaje) REFERENCES mensaje(id_mensaje),
    CONSTRAINT fk_msjdes_usr FOREIGN KEY (id_usuario)  REFERENCES usuario(id_usuario)
);
 
-- ============================================================
-- TIPO_DOCUMENTO
-- ============================================================
CREATE TABLE tipo_documento (
    id_tipo_documento       SERIAL          PRIMARY KEY,
    nombre                  VARCHAR(100)    NOT NULL,
    requiere_vencimiento    BOOLEAN         DEFAULT FALSE
);
 
-- ============================================================
-- ANTECEDENTE_SALUD
-- ============================================================
CREATE TABLE antecedente_salud (
    id_antecedente_salud    SERIAL          PRIMARY KEY,
    id_estudiante           INT             NOT NULL,
    grupo_sanguineo         VARCHAR(5),
    sistema_salud           VARCHAR(20)     CHECK (sistema_salud IN ('FONASA','ISAPRE','DIPRECA','CAPREDENA','NINGUNO')),
    alergias                TEXT,
    enfermedades            TEXT,
    medicamentos            TEXT,
    contacto_emergencia     VARCHAR(150),
    telefono_emergencia     VARCHAR(20),
    CONSTRAINT fk_antsalud_est FOREIGN KEY (id_estudiante) REFERENCES estudiante(id_estudiante)
);
 
-- ============================================================
-- ANTECEDENTE_FAMILIAR
-- ============================================================
CREATE TABLE antecedente_familiar (
    id_antecedente_familiar SERIAL          PRIMARY KEY,
    id_estudiante           INT             NOT NULL,
    vive_con                VARCHAR(50)     CHECK (vive_con IN ('Madre','Padre','Ambos padres','Abuelos','Tíos','Otro')),
    vive_con_otro           VARCHAR(150),
    numero_hermanos         INT             DEFAULT 0,
    lugar_entre_hermanos    INT             DEFAULT 1,
    hermanos_en_establecimiento BOOLEAN     DEFAULT FALSE,
    observacion             TEXT,
    CONSTRAINT fk_antfam_est FOREIGN KEY (id_estudiante) REFERENCES estudiante(id_estudiante)
);
 
-- ============================================================
-- DOCUMENTO_ESTUDIANTE
-- ============================================================
CREATE TABLE documento_estudiante (
    id_documento            SERIAL          PRIMARY KEY,
    id_estudiante           INT             NOT NULL,
    id_tipo_documento       INT             NOT NULL,
    tipo_documento_otro     VARCHAR(150),
    nombre_archivo          VARCHAR(255)    NOT NULL,
    url_archivo             VARCHAR(500),
    fecha_subida            DATE            DEFAULT CURRENT_DATE,
    fecha_vencimiento       DATE,
    subido_por              INT,
    estado                  VARCHAR(10)     DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO','INACTIVO')),
    CONSTRAINT fk_doc_est      FOREIGN KEY (id_estudiante)    REFERENCES estudiante(id_estudiante),
    CONSTRAINT fk_doc_tipo     FOREIGN KEY (id_tipo_documento) REFERENCES tipo_documento(id_tipo_documento),
    CONSTRAINT fk_doc_usuario  FOREIGN KEY (subido_por)       REFERENCES usuario(id_usuario)
);