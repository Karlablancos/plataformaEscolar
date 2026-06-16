# Decisiones Tecnicas Observadas

Este documento registra decisiones presentes en el codigo fuente y configuracion. No interpreta decisiones no expresadas en el repositorio.

## Arquitectura por componentes

El repositorio separa frontend, BFF Gateway, microservicios y base de datos:

- `frontend`
- `bff-gateway/bff-gateway`
- `usuario/usuario`
- `establecimiento/establecimiento`
- `asistencia/asistencia`
- `database`

`docker-compose.yml` orquesta los servicios y conecta los microservicios a PostgreSQL.

## BFF/API Gateway

El BFF usa Spring Cloud Gateway y expone:

- endpoints propios bajo `/auth`.
- rutas proxy hacia `usuario-service`, `establecimiento-service` y `asistencia-service`.

`AuthController` usa `WebClient` para llamar a microservicios. `application.yaml` define URLs internas:

- `microservicios.usuario-url`
- `microservicios.establecimiento-url`

## Login con RBD

El login del BFF requiere:

- `rbd`
- `username`
- `password`

Antes de autenticar credenciales, el BFF consulta el establecimiento por RBD para obtener `idEstablecimiento`. Luego envia `username`, `password` e `idEstablecimiento` a `usuario-service`.

## JWT centralizado en BFF

`JwtUtil` esta en el BFF. El token incluye:

- subject con `username`.
- claim `rbd`.
- claim `rol`.
- expiracion.

El frontend almacena el token en `localStorage` y lo envia en cada request mediante interceptor Axios.

## Roles en frontend y BFF

El frontend controla navegacion con `meta.roles`.

Roles observados en rutas:

- `ADMIN`
- `UTP`
- `DIRECTOR`
- `PROFESOR`
- `SOSTENEDOR`

El BFF normaliza roles de base de datos:

- `ADMINISTRADOR` a `ADMIN`.
- `DOCENTE` a `PROFESOR`.
- `SOSTENEDOR` a `SOSTENEDOR`.

Tambien existe fallback por `idRol`.

## Persistencia con JPA y PostgreSQL

Los servicios de dominio usan Spring Data JPA y repositorios.

Configuracion observada:

- `usuario`: `ddl-auto=update`.
- `establecimiento`: `ddl-auto=validate`.
- `asistencia`: `ddl-auto=validate`.

La base se inicializa por scripts SQL montados en PostgreSQL desde Docker Compose.

## DTOs por servicio

Los controladores exponen DTOs y requests especificos:

- BFF: `LoginRequest`, `LoginResponse`.
- Usuario: `UsuarioDTO`, `CrearUsuarioRequest`, `ActualizarUsuarioRequest`, `CambiarEstadoUsuarioRequest`, `CambiarPasswordRequest`, `LoginRequest`.
- Establecimiento: `EstablecimientoDTO`, `CursoDTO`, `AsignaturaDTO`, `EstudianteDTO`.
- Asistencia: `AsistenciaDTO`.

## Patron Factory en usuarios

`UsuarioService` recibe una lista de `UsuarioFactory` y construye un mapa por `idRol`.

Observaciones desde codigo:

- `factories` se inicializa en `@PostConstruct`.
- Roles `3` y `4` usan la misma factory de apoderado si aplica.
- Si no existe factory para un rol, se usa la factory del rol `1`.

## Validaciones en base de datos

`triggers_colegio.sql` implementa validaciones en PostgreSQL:

- notas entre `1.0` y `7.0`.
- asistencia solo en dias habiles del calendario escolar.

`indices_y_procedimientos.sql` agrega indices para consultas de login, RBD, asistencia, notas, horarios y RUT.

## Circuit breakers

El BFF usa Resilience4j con circuit breakers para:

- `usuario-service`
- `establecimiento-service`

`AuthController` usa `@CircuitBreaker` en:

- `login`
- `validarRbd`

## CORS

`SecurityConfig` permite origen:

- `http://localhost:5173`

Metodos permitidos:

- `GET`
- `POST`
- `PUT`
- `PATCH`
- `DELETE`
- `OPTIONS`

## Estado frontend local

El frontend usa Pinia y `localStorage`.

Usos observados:

- token y usuario autenticado.
- establecimiento activo.
- RBD.
- datos persistidos o fallback en stores de alumnos, cursos, asignaturas y docentes.

Tambien existen archivos `frontend/src/data/*Mock.js`.

## Observaciones de consistencia

Estas observaciones provienen de diferencias entre archivos existentes:

- BFF README: puerto `8086`; configuracion real: `8080`.
- BFF README: Java 22 y Spring Boot 4.0.6; `pom.xml`: Java 21 y Spring Boot 3.4.5.
- Usuario README: Spring Boot 3.4.5; `pom.xml`: Spring Boot 3.5.14.
- Gateway configura `/asistencias/**`; controlador de asistencia expone `/asistencia`.
- Frontend llama `GET /establecimiento/{idEstab}` en `SostenedorDashboardView.vue`; el controlador observado no declara `GET /establecimiento/{id}`.

## Fuentes

- `docker-compose.yml`
- `frontend/src/router/index.ts`
- `frontend/src/api/axios.js`
- `frontend/src/stores/*.js`
- `bff-gateway/bff-gateway/src/main/resources/application.yaml`
- `bff-gateway/bff-gateway/src/main/java/com/colegio/bff`
- `usuario/usuario/src/main/java/com/colegio/usuario`
- `database/triggers_colegio.sql`
- `database/indices_y_procedimientos.sql`
