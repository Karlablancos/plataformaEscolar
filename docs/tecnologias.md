# Tecnologias Utilizadas

## Frontend

Declarado en `frontend/package.json`.

| Tecnologia | Version declarada | Uso observado |
| --- | --- | --- |
| Vue | `^3.5.32` | Framework frontend. |
| Vite | `^8.0.8` | Servidor de desarrollo y build. |
| TypeScript | `~6.0.0` | Soporte TS del proyecto. |
| Pinia | `^3.0.4` | Stores de estado. |
| Vue Router | `^5.0.6` | Rutas frontend. |
| Axios | `^1.16.0` | Cliente HTTP hacia el BFF. |
| Bootstrap | `^5.3.8` | Estilos UI. |
| Bootstrap Icons | `^1.13.1` | Iconografia. |
| ESLint | `^10.2.1` | Linting. |
| Oxlint | `~1.60.0` | Linting adicional. |
| Prettier | `3.8.3` | Formato. |

Engine Node declarado:

- `^20.19.0 || >=22.12.0`

Scripts declarados:

- `npm run dev`: ejecuta Vite.
- `npm run build`: ejecuta type-check y build.
- `npm run preview`: previsualizacion Vite.
- `npm run lint`: ejecuta linting.
- `npm run format`: formatea `src/`.

## BFF Gateway

Declarado en `bff-gateway/bff-gateway/pom.xml` y `application.yaml`.

| Tecnologia | Version/configuracion | Uso observado |
| --- | --- | --- |
| Java | `21` | Runtime configurado en Maven. |
| Spring Boot | `3.4.5` | Base del BFF. |
| Spring Cloud | `2024.0.1` | Gateway y circuit breaker. |
| Spring Cloud Gateway | dependencia Maven | Enrutamiento hacia microservicios. |
| WebFlux | `web-application-type: reactive` | Aplicacion reactiva del BFF. |
| Spring Security | dependencia Maven | Configuracion CORS y seguridad. |
| JJWT | `0.12.3` | Generacion y validacion JWT. |
| Resilience4j Reactor | dependencia Maven | Circuit breakers. |
| Spring Actuator | dependencia Maven | Endpoints de monitoreo. |
| Lombok | dependencia Maven | Reduccion de boilerplate. |

## Microservicio Usuario

Declarado en `usuario/usuario/pom.xml` y `application.properties`.

| Tecnologia | Version/configuracion | Uso observado |
| --- | --- | --- |
| Java | `21` | Runtime configurado en Maven. |
| Spring Boot | `3.5.14` | Base del microservicio. |
| Spring Web | dependencia Maven | API REST. |
| Spring Data JPA | dependencia Maven | Persistencia. |
| Spring Validation | dependencia Maven | Validacion. |
| Spring Security | dependencia Maven | Password encoding y seguridad. |
| Spring Cloud CircuitBreaker Resilience4j | dependencia Maven | Circuit breaker disponible. |
| PostgreSQL Driver | runtime | Conexion a PostgreSQL. |
| Lombok | dependencia Maven | DTOs y servicios. |

Configuracion observada:

- `server.port=8081`
- `spring.jpa.hibernate.ddl-auto=update`
- `spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration`

## Microservicio Establecimiento

Declarado en `establecimiento/establecimiento/pom.xml` y `application.properties`.

| Tecnologia | Version/configuracion | Uso observado |
| --- | --- | --- |
| Java | `22` | Runtime configurado en Maven. |
| Spring Boot | `4.0.6` | Base del microservicio. |
| Spring Web | dependencia Maven | API REST. |
| Spring Data JPA | dependencia Maven | Persistencia. |
| Spring Actuator | dependencia Maven | Monitoreo. |
| PostgreSQL Driver | runtime | Conexion a PostgreSQL. |
| Lombok | dependencia Maven | DTOs y servicios. |

Configuracion observada:

- `server.port=8082`
- `spring.jpa.hibernate.ddl-auto=validate`

## Microservicio Asistencia

Declarado en `asistencia/asistencia/pom.xml` y `application.properties`.

| Tecnologia | Version/configuracion | Uso observado |
| --- | --- | --- |
| Java | `22` | Runtime configurado en Maven. |
| Spring Boot | `4.0.6` | Base del microservicio. |
| Spring Web | dependencia Maven | API REST. |
| Spring Data JPA | dependencia Maven | Persistencia. |
| Spring Actuator | dependencia Maven | Monitoreo. |
| PostgreSQL Driver | runtime | Conexion a PostgreSQL. |
| Lombok | dependencia Maven | Dependencia declarada. |

Configuracion observada:

- `server.port=8083`
- `spring.jpa.hibernate.ddl-auto=validate`

## Base de datos e infraestructura

| Tecnologia | Version/configuracion | Uso observado |
| --- | --- | --- |
| PostgreSQL | imagen Docker `postgres:16` | Base `colegio_db`. |
| Docker Compose | `docker-compose.yml` | Orquestacion local del stack. |
| PL/pgSQL | scripts SQL | Triggers y funciones almacenadas. |

## Fuentes

- `frontend/package.json`
- `bff-gateway/bff-gateway/pom.xml`
- `usuario/usuario/pom.xml`
- `establecimiento/establecimiento/pom.xml`
- `asistencia/asistencia/pom.xml`
- `docker-compose.yml`
- `*/src/main/resources/application.*`
