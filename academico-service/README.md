# academico-service

Microservicio académico de la plataforma escolar. Gestiona evaluaciones, notas de estudiantes, cálculo de promedios y determinación de promoción de curso.

## Tecnologías

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- JaCoCo (cobertura de pruebas)

## Puerto

`8084`

## Endpoints

### Evaluaciones

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/evaluacion` | Crear una evaluación |
| GET | `/evaluacion/{id}` | Obtener evaluación por ID |
| GET | `/evaluacion/curso/{idCurso}` | Listar evaluaciones de un curso |
| PUT | `/evaluacion/{id}` | Actualizar evaluación |
| DELETE | `/evaluacion/{id}` | Eliminar evaluación |

### Notas

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/notas` | Registrar nota de un estudiante |
| GET | `/notas/{id}` | Obtener nota por ID |
| GET | `/notas/promedio?idEstudiante={id}&idAsignatura={id}` | Calcular promedio de un estudiante en una asignatura |
| GET | `/notas/promocion?idEstudiante={id}` | Determinar si un estudiante es promovido |

## Pruebas

El servicio cuenta con pruebas unitarias y cobertura configurada con JaCoCo.

```bash
cd academico-service/academico-service

# Ejecutar pruebas
./mvnw test

# Generar reporte de cobertura
./mvnw verify
```

El reporte HTML de cobertura queda en `target/site/jacoco/index.html`.

## Ejecución local

**Requisitos:** Java 21, Maven, PostgreSQL corriendo en `localhost:5432` con base de datos `colegio_db`.

```bash
cd academico-service/academico-service
./mvnw spring-boot:run
```

El servicio estará disponible en `http://localhost:8084`.

## Ejecución con Docker

Desde la raíz del proyecto:

```bash
docker compose up academico-service
```

O construir la imagen manualmente:

```bash
cd academico-service/academico-service
docker build -t academico-service .
docker run -p 8084:8084 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/colegio_db \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  academico-service
```

## Variables de entorno

| Variable | Valor por defecto | Descripción |
|----------|------------------|-------------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/colegio_db` | URL de conexión a PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Usuario de la base de datos |
| `SPRING_DATASOURCE_PASSWORD` | `123456` | Contraseña de la base de datos |
