# Base de Datos

La base de datos observada es PostgreSQL. `docker-compose.yml` configura la base `colegio_db` y monta scripts desde `database`.

## Scripts

| Archivo | Contenido observado |
| --- | --- |
| `database/script_colegio.sql` | DDL principal con tablas y claves foraneas. |
| `database/triggers_colegio.sql` | Funciones PL/pgSQL y triggers criticos. |
| `database/indices_y_procedimientos.sql` | Indices y funciones almacenadas. |
| `database/poblado_tablas.sql` | Datos iniciales. |

## Tablas

`script_colegio.sql` define 27 tablas.

### Establecimiento y ubicacion

| Tabla | Clave primaria | Relaciones observadas |
| --- | --- | --- |
| `tipo_establecimiento` | `id_tipo_estab` | Referenciada por `establecimiento`. |
| `region` | `id_region` | Referenciada por `comuna`. |
| `comuna` | `id_comuna` | FK a `region`; referenciada por `establecimiento`, `docente`, `estudiante`, `apoderado`. |
| `establecimiento` | `id_establecimiento` | FK a `tipo_establecimiento` y `comuna`; referenciada por multiples tablas. |

Campos destacados de `establecimiento`:

- `rbd`
- `nombre`
- `sostenedor`
- `director`
- `correo_electronico`
- `modo_aula`
- `estado`

### Calendario academico

| Tabla | Clave primaria | Relaciones observadas |
| --- | --- | --- |
| `calendario_escolar` | `id_calendario` | FK a `establecimiento`. |
| `calendario_feriado` | `id_feriado` | FK a `calendario_escolar`. |
| `periodo_academico` | `id_periodo` | FK a `establecimiento`; referenciada por `horario` y `evaluacion`. |

### Usuarios y roles

| Tabla | Clave primaria | Relaciones observadas |
| --- | --- | --- |
| `rol` | `id_rol` | Referenciada por `usuario`. |
| `usuario` | `id_usuario` | FK a `establecimiento` y `rol`; referenciada por `docente`, `estudiante`, `apoderado`, `mensaje`. |

Campos destacados de `usuario`:

- `id_establecimiento`
- `id_rol`
- `username`
- `password_hash`
- `correo_electronico`
- `intentos_fallidos`
- `bloqueado`
- `estado`

### Configuracion academica

| Tabla | Clave primaria | Relaciones observadas |
| --- | --- | --- |
| `categoria_sned` | `id_categoria_sned` | Referenciada por `docente`. |
| `tipo_calificacion` | `id_tipo_calificacion` | Referenciada por `asignatura`. |
| `asignatura` | `id_asignatura` | FK a `establecimiento` y `tipo_calificacion`. |
| `plan_estudio` | `id_plan_estudio` | FK a `establecimiento`. |
| `plan_estudio_asignatura` | `id_plan_estudio_asignatura` | FK a `plan_estudio` y `asignatura`. |

### Personas

| Tabla | Clave primaria | Relaciones observadas |
| --- | --- | --- |
| `docente` | `id_docente` | FK a `establecimiento`, `usuario`, `categoria_sned`, `comuna`. |
| `tipo_nee` | `id_tipo_nee` | Referenciada por `estudiante`. |
| `estudiante` | `id_estudiante` | FK a `establecimiento`, `usuario`, `comuna`, `tipo_nee`. |
| `apoderado` | `id_apoderado` | FK a `usuario` y `comuna`. |
| `estudiante_apoderado` | `id_estudiante_apoderado` | FK a `estudiante` y `apoderado`. |

### Cursos, salas y horario

| Tabla | Clave primaria | Relaciones observadas |
| --- | --- | --- |
| `curso` | `id_curso` | FK a `establecimiento`. |
| `sala` | `id_sala` | FK a `establecimiento`. |
| `curso_asignatura` | `id_curso_asignatura` | FK a `curso`, `asignatura`, `docente`. |
| `horario` | `id_horario` | FK a `curso`, `asignatura`, `docente`, `sala`, `periodo_academico`. |
| `estudiante_curso` | `id_matricula` | FK a `estudiante` y `curso`. |

### Asistencia, evaluacion y convivencia

| Tabla | Clave primaria | Relaciones observadas |
| --- | --- | --- |
| `asistencia` | `id_asistencia` | FK a `estudiante` y `horario`. |
| `anotacion` | `id_anotacion` | FK a `estudiante` y `docente`. |
| `tipo_evaluacion` | `id_tipo_evaluacion` | Referenciada por `evaluacion`. |
| `evaluacion` | `id_evaluacion` | FK a `curso`, `asignatura`, `docente`, `tipo_evaluacion`, `periodo_academico`. |
| `nota` | `id_nota` | FK a `estudiante` y `evaluacion`. |

### Mensajeria

| Tabla | Clave primaria | Relaciones observadas |
| --- | --- | --- |
| `mensaje` | `id_mensaje` | FK a `usuario` como remitente y destinatario. |
| `mensaje_destinatario` | `id_mensaje_des` | FK a `mensaje` y `usuario`. |

## Triggers

Declarados en `database/triggers_colegio.sql`.

| Trigger | Tabla | Evento | Funcion | Regla observada |
| --- | --- | --- | --- | --- |
| `trg_validar_nota` | `nota` | `BEFORE INSERT OR UPDATE` | `validar_nota()` | Rechaza calificacion menor a `1.0` o mayor a `7.0`. |
| `trg_validar_dia_habil` | `asistencia` | `BEFORE INSERT OR UPDATE` | `validar_dia_habil()` | Rechaza asistencia fuera del calendario escolar, fin de semana o feriado. |

## Indices

Declarados en `database/indices_y_procedimientos.sql`.

| Indice | Tabla | Columnas |
| --- | --- | --- |
| `idx_usuario_username` | `usuario` | `username` |
| `idx_usuario_establecimiento` | `usuario` | `id_establecimiento` |
| `idx_establecimiento_rbd` | `establecimiento` | `rbd` |
| `idx_asistencia_estudiante_fecha` | `asistencia` | `id_estudiante`, `fecha` |
| `idx_nota_estudiante` | `nota` | `id_estudiante` |
| `idx_nota_evaluacion` | `nota` | `id_evaluacion` |
| `idx_horario_curso_dia` | `horario` | `id_curso`, `dia_semana` |
| `idx_estudiante_rut` | `estudiante` | `rut` |
| `idx_docente_rut` | `docente` | `rut` |

## Funciones almacenadas

| Funcion | Retorno | Proposito observado |
| --- | --- | --- |
| `calcular_promedio_estudiante(p_id_estudiante, p_id_asignatura, p_id_periodo)` | `NUMERIC(5,2)` | Calcula promedio ponderado desde `nota` y `evaluacion`. |
| `calcular_asistencia_estudiante(p_id_estudiante, p_id_establecimiento, p_anio)` | Tabla | Calcula total de clases, presentes, ausentes, porcentaje y `en_riesgo`. |

## Uso desde servicios

Entidades consultadas por APIs existentes:

- `usuario`: usado por `usuario-service`.
- `establecimiento`: usado por `establecimiento-service` y BFF para validar RBD.
- `curso`: usado por `establecimiento-service`.
- `asignatura`: usado por `establecimiento-service`.
- `estudiante`: usado por `establecimiento-service`.
- `asistencia`: usado por `asistencia-service`.
- `evaluacion`: usado por `academico-service`.
- `nota`: usado por `academico-service`.

Tablas sin controlador REST observado:

- `calendario_escolar`
- `calendario_feriado`
- `periodo_academico`
- `categoria_sned`
- `tipo_calificacion`
- `plan_estudio`
- `plan_estudio_asignatura`
- `docente`
- `tipo_nee`
- `apoderado`
- `estudiante_apoderado`
- `sala`
- `curso_asignatura`
- `horario`
- `estudiante_curso`
- `anotacion`
- `tipo_evaluacion`
- `mensaje`
- `mensaje_destinatario`

## Fuentes

- `docker-compose.yml`
- `database/script_colegio.sql`
- `database/triggers_colegio.sql`
- `database/indices_y_procedimientos.sql`
- Controladores y repositorios Java en `usuario`, `establecimiento` y `asistencia`
