# Endpoints Backend

Esta documentacion lista los endpoints declarados en controladores Java y rutas configuradas en el BFF.

## BFF Gateway

Base observada:

- Puerto configurado: `8080`.
- Controlador: `@RequestMapping("/auth")`.

### Endpoints de autenticacion

| Metodo | Ruta | Request | Response | Comportamiento observado |
| --- | --- | --- | --- | --- |
| `POST` | `/auth/login` | `LoginRequest` | `LoginResponse` | Valida RBD, autentica usuario contra `usuario-service` y genera JWT. |
| `GET` | `/auth/validar-rbd/{rbd}` | Path `rbd` | `Map<String,Object>` | Consulta establecimiento por RBD y retorna `idEstablecimiento` y `nombre`. |

`LoginRequest`:

| Campo | Tipo |
| --- | --- |
| `rbd` | `String` |
| `username` | `String` |
| `password` | `String` |

`LoginResponse`:

| Campo | Tipo |
| --- | --- |
| `token` | `String` |
| `username` | `String` |
| `rol` | `String` |
| `nombreColegio` | `String` |
| `rbd` | `String` |
| `idEstablecimiento` | `Integer` |

### Rutas Gateway

Declaradas en `application.yaml`:

| Gateway path | Destino |
| --- | --- |
| `/usuarios`, `/usuarios/**` | `http://usuario-service:8081` |
| `/establecimiento`, `/establecimiento/**` | `http://establecimiento-service:8082` |
| `/asistencia`, `/asistencia/**` | `http://asistencia-service:8083` |
| `/evaluacion`, `/evaluacion/**`, `/notas`, `/notas/**` | `http://academico-service:8084` |

## Microservicio Usuario

Base observada:

- Puerto configurado: `8081`.
- Controlador: `@RequestMapping("/usuarios")`.

| Metodo | Ruta | Request | Response | Comportamiento observado |
| --- | --- | --- | --- | --- |
| `GET` | `/usuarios` | Query opcional `idEstablecimiento` | `List<UsuarioDTO>` | Lista todos los usuarios o filtra por establecimiento. |
| `GET` | `/usuarios/{id}` | Path `id` | `UsuarioDTO` | Busca usuario por ID. |
| `PATCH` | `/usuarios/{id}/estado` | `CambiarEstadoUsuarioRequest` | `UsuarioDTO` | Cambia estado del usuario. |
| `POST` | `/usuarios` | `CrearUsuarioRequest` | `UsuarioDTO` | Crea usuario. |
| `PUT` | `/usuarios/{id}` | `ActualizarUsuarioRequest` | `UsuarioDTO` | Actualiza datos de usuario. |
| `PUT` | `/usuarios/{id}/password` | `CambiarPasswordRequest` | `204` o `401` | Cambia password si la password actual coincide. |
| `DELETE` | `/usuarios/{id}` | Path `id` | `204` | Elimina usuario por ID. |
| `POST` | `/usuarios/login` | `LoginRequest` | `UsuarioDTO` | Autenticacion interna por username, password e idEstablecimiento. |

`CrearUsuarioRequest`:

| Campo | Tipo |
| --- | --- |
| `username` | `String` |
| `password` | `String` |
| `idEstablecimiento` | `Integer` |
| `idRol` | `Integer` |
| `correoElectronico` | `String` |
| `estado` | `String` |

`ActualizarUsuarioRequest`:

| Campo | Tipo |
| --- | --- |
| `username` | `String` |
| `idRol` | `Integer` |
| `correoElectronico` | `String` |
| `bloqueado` | `Boolean` |
| `estado` | `String` |

`CambiarEstadoUsuarioRequest`:

| Campo | Tipo |
| --- | --- |
| `estado` | `String` |

`CambiarPasswordRequest`:

| Campo | Tipo |
| --- | --- |
| `passwordActual` | `String` |
| `passwordNueva` | `String` |

`LoginRequest` interno:

| Campo | Tipo |
| --- | --- |
| `username` | `String` |
| `password` | `String` |
| `idEstablecimiento` | `Integer` |

`UsuarioDTO`:

| Campo | Tipo |
| --- | --- |
| `idUsuario` | `Integer` |
| `idEstablecimiento` | `Integer` |
| `idRol` | `Integer` |
| `nombreRol` | `String` |
| `username` | `String` |
| `correoElectronico` | `String` |
| `ultimoAcceso` | `LocalDateTime` |
| `bloqueado` | `Boolean` |
| `estado` | `String` |

## Microservicio Establecimiento

Base observada:

- Puerto configurado: `8082`.
- Controlador: `@RequestMapping("/establecimiento")`.

| Metodo | Ruta | Request | Response | Comportamiento observado |
| --- | --- | --- | --- | --- |
| `GET` | `/establecimiento` | Query opcional `idEstablecimiento` | `List<EstablecimientoDTO>` | Lista establecimientos activos o retorna uno por ID si se envia query. |
| `GET` | `/establecimiento/rbd/{rbd}` | Path `rbd` | `EstablecimientoDTO` | Busca establecimiento por RBD. |
| `GET` | `/establecimiento/existe/{rbd}` | Path `rbd` | `Boolean` | Indica si existe un RBD. |
| `GET` | `/establecimiento/{id}/cursos` | Path `id` | `List<CursoDTO>` | Lista cursos por establecimiento. |
| `GET` | `/establecimiento/{id}/asignaturas` | Path `id` | `List<AsignaturaDTO>` | Lista asignaturas por establecimiento. |
| `GET` | `/establecimiento/{id}/estudiantes` | Path `id` | `List<EstudianteDTO>` | Lista estudiantes por establecimiento. |

`EstablecimientoDTO`:

| Campo | Tipo |
| --- | --- |
| `idEstablecimiento` | `Integer` |
| `rbd` | `String` |
| `nombre` | `String` |
| `idTipoEstab` | `Integer` |
| `sostenedor` | `String` |
| `director` | `String` |
| `calle` | `String` |
| `numero` | `String` |
| `idComuna` | `Integer` |
| `telefono` | `String` |
| `correoElectronico` | `String` |
| `modoAula` | `String` |
| `estado` | `String` |

`CursoDTO`:

| Campo | Tipo |
| --- | --- |
| `idCurso` | `Integer` |
| `idEstablecimiento` | `Integer` |
| `numero` | `Integer` |
| `letra` | `String` |
| `tipoEnsenanza` | `String` |
| `modalidad` | `String` |
| `anioAcademico` | `Integer` |
| `esNivelSimce` | `Boolean` |
| `estado` | `String` |
| `nombre` | `String` |

`AsignaturaDTO`:

| Campo | Tipo |
| --- | --- |
| `idAsignatura` | `Integer` |
| `idEstablecimiento` | `Integer` |
| `nombre` | `String` |
| `codigo` | `String` |
| `idTipoCalificacion` | `Integer` |
| `estado` | `String` |

`EstudianteDTO`:

| Campo | Tipo |
| --- | --- |
| `idEstudiante` | `Integer` |
| `idEstablecimiento` | `Integer` |
| `rut` | `String` |
| `dv` | `String` |
| `nombres` | `String` |
| `apellidoPaterno` | `String` |
| `apellidoMaterno` | `String` |
| `nombreCompleto` | `String` |
| `fechaNacimiento` | `LocalDate` |
| `correoElectronico` | `String` |
| `telefono` | `String` |
| `fechaMatricula` | `LocalDate` |
| `prioritario` | `Boolean` |
| `preferente` | `Boolean` |
| `tieneNee` | `Boolean` |
| `enPie` | `Boolean` |
| `estado` | `String` |

## Microservicio Asistencia

Base observada:

- Puerto configurado: `8083`.
- Controlador: `@RequestMapping("/asistencia")`.

| Metodo | Ruta | Request | Response | Comportamiento observado |
| --- | --- | --- | --- | --- |
| `GET` | `/asistencia/estudiante/{idEstudiante}` | Path `idEstudiante` | `List<AsistenciaDTO>` | Obtiene asistencias por estudiante. |
| `GET` | `/asistencia/estudiante/{idEstudiante}/fecha/{fecha}` | Path `idEstudiante`, `fecha` | `List<AsistenciaDTO>` | Obtiene asistencias por estudiante y fecha. |
| `POST` | `/asistencia` | `AsistenciaDTO` | `AsistenciaDTO` | Registra asistencia. |

`AsistenciaDTO`:

| Campo | Tipo |
| --- | --- |
| `idAsistencia` | `Integer` |
| `idEstudiante` | `Integer` |
| `idHorario` | `Integer` |
| `fecha` | `LocalDate` |
| `estadoAsistencia` | `String` |
| `horaLlegada` | `LocalTime` |
| `justificada` | `Boolean` |
| `observacion` | `String` |

## Microservicio Academico

Base observada:

- Puerto configurado: `8084`.
- Controladores: `@RequestMapping("/evaluacion")` y `@RequestMapping("/notas")`.

### Evaluaciones

| Metodo | Ruta | Request | Response | Comportamiento observado |
| --- | --- | --- | --- | --- |
| `POST` | `/evaluacion` | `EvaluacionDTO` | `EvaluacionDTO` | Crea una evaluacion. |
| `GET` | `/evaluacion/{id}` | Path `id` | `EvaluacionDTO` | Busca evaluacion por ID. |
| `GET` | `/evaluacion/curso/{idCurso}` | Path `idCurso` | `List<EvaluacionDTO>` | Lista evaluaciones por curso. |
| `PUT` | `/evaluacion/{id}` | `EvaluacionDTO` | `EvaluacionDTO` | Actualiza una evaluacion. |
| `DELETE` | `/evaluacion/{id}` | Path `id` | `204` | Elimina una evaluacion. |

`EvaluacionDTO`:

| Campo | Tipo |
| --- | --- |
| `id` | `Long` |
| `nombre` | `String` |
| `tipo` | `TipoEvaluacion` (enum) |
| `ponderacion` | `BigDecimal` |
| `fecha` | `LocalDate` |
| `idCurso` | `Long` |
| `idAsignatura` | `Long` |
| `idDocente` | `Long` |
| `idEstablecimiento` | `Long` |

### Notas

| Metodo | Ruta | Request | Response | Comportamiento observado |
| --- | --- | --- | --- | --- |
| `POST` | `/notas` | `NotaDTO` | `NotaDTO` | Registra una nota. |
| `GET` | `/notas/{id}` | Path `id` | `NotaDTO` | Busca nota por ID. |
| `GET` | `/notas/promedio` | Query `idEstudiante`, `idAsignatura` | `Map<String,Object>` | Calcula promedio ponderado del estudiante en una asignatura. |
| `GET` | `/notas/promocion` | Query `idEstudiante` | `Map<String,Object>` | Determina si el estudiante es promovido segun Decreto 67/2018. |

`NotaDTO`:

| Campo | Tipo |
| --- | --- |
| `id` | `Long` |
| `calificacion` | `BigDecimal` |
| `idEvaluacion` | `Long` |
| `idEstudiante` | `Long` |
| `idEstablecimiento` | `Long` |

## Observaciones

- No se documentan endpoints para mensajes, horarios, docentes o apoderados porque no se encontraron controladores REST para esos dominios.

## Fuentes

- `bff-gateway/bff-gateway/src/main/java/com/colegio/bff/controller/AuthController.java`
- `bff-gateway/bff-gateway/src/main/resources/application.yaml`
- `usuario/usuario/src/main/java/com/colegio/usuario/controller/UsuarioController.java`
- `establecimiento/establecimiento/src/main/java/com/colegio/establecimiento/controller/EstablecimientoController.java`
- `asistencia/asistencia/src/main/java/com/colegio/asistencia/controller/AsistenciaController.java`
- `academico-service/academico-service/src/main/java/com/colegio/academico/controller/EvaluacionController.java`
- `academico-service/academico-service/src/main/java/com/colegio/academico/controller/NotaController.java`
- DTOs en `*/src/main/java/**/dto/*.java`
