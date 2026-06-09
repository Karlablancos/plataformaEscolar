# Arquitectura

## Vista general

El proyecto `plataformaEscolar` esta organizado como una plataforma escolar con frontend, BFF/API Gateway, microservicios backend y base de datos PostgreSQL.

Segun `repositorios.txt`, el repositorio principal contiene los componentes backend, BFF, frontend y base de datos del sistema de gestion escolar del Colegio Bernardo O'Higgins.

## Componentes

| Componente | Ruta | Puerto configurado | Responsabilidad observada |
| --- | --- | ---: | --- |
| Frontend | `frontend` | `5173` | Aplicacion web Vue para seleccion de establecimiento, login y vistas por rol. |
| BFF Gateway | `bff-gateway/bff-gateway` | `8080` | Gateway, login con RBD, emision de JWT y rutas hacia microservicios. |
| Usuario | `usuario/usuario` | `8081` | Gestion de usuarios y autenticacion interna con password hash. |
| Establecimiento | `establecimiento/establecimiento` | `8082` | Consulta de establecimientos, cursos, asignaturas y estudiantes. |
| Asistencia | `asistencia/asistencia` | `8083` | Consulta y registro de asistencia de estudiantes. |
| PostgreSQL | `database` | `5432` | Esquema relacional, datos iniciales, triggers, indices y funciones. |

## Orquestacion

`docker-compose.yml` define los siguientes servicios:

- `postgres`, con imagen `postgres:16`, base `colegio_db` y scripts de inicializacion desde `database`.
- `usuario-service`, construido desde `./usuario/usuario`.
- `establecimiento-service`, construido desde `./establecimiento/establecimiento`.
- `asistencia-service`, construido desde `./asistencia/asistencia`.
- `bff-gateway`, construido desde `./bff-gateway/bff-gateway`.
- `frontend`, construido desde `./frontend`.

Los microservicios dependen de `postgres` con `condition: service_healthy`. El BFF depende de los tres microservicios. El frontend declara dependencia de los tres microservicios.

## Comunicacion

El frontend usa Axios con `baseURL`:

- `import.meta.env.VITE_API_URL`, si existe.
- `http://localhost:8080/`, como valor por defecto.

El BFF Gateway configura rutas Spring Cloud Gateway:

| Ruta gateway | Servicio destino |
| --- | --- |
| `/usuarios`, `/usuarios/**` | `http://usuario-service:8081` |
| `/establecimiento`, `/establecimiento/**` | `http://establecimiento-service:8082` |
| `/asistencias`, `/asistencias/**` | `http://asistencia-service:8083` |

El controlador de asistencia expone `/asistencia`, mientras que el gateway configura `/asistencias`. Esta diferencia esta documentada como discrepancia observada.

## Persistencia

Los servicios `usuario`, `establecimiento` y `asistencia` usan PostgreSQL mediante Spring Data JPA.

Configuracion observada:

- `usuario`: `spring.jpa.hibernate.ddl-auto=update`.
- `establecimiento`: `spring.jpa.hibernate.ddl-auto=validate`.
- `asistencia`: `spring.jpa.hibernate.ddl-auto=validate`.
- `docker-compose.yml`: `establecimiento-service` define `SPRING_JPA_HIBERNATE_DDL_AUTO=none`.

## Observaciones

- El README de `bff-gateway` indica puerto `8086`, pero `application.yaml` y `docker-compose.yml` configuran `8080`.
- El README de `bff-gateway` indica Java 22 y Spring Boot 4.0.6, pero el `pom.xml` del BFF declara Java 21 y Spring Boot 3.4.5.
- El README de `usuario` indica Spring Boot 3.4.5, pero el `pom.xml` de `usuario` declara Spring Boot 3.5.14.
- La ruta gateway de asistencia esta configurada como `/asistencias/**`; el controlador usa `/asistencia`.

## Fuentes

- `repositorios.txt`
- `docker-compose.yml`
- `frontend/src/api/axios.js`
- `bff-gateway/bff-gateway/src/main/resources/application.yaml`
- `asistencia/asistencia/src/main/java/com/colegio/asistencia/controller/AsistenciaController.java`
- `usuario/usuario/src/main/resources/application.properties`
- `establecimiento/establecimiento/src/main/resources/application.properties`
- `asistencia/asistencia/src/main/resources/application.properties`
