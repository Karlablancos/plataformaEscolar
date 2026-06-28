# establecimiento-service

Microservicio central de la plataforma escolar. Gestiona establecimientos, cursos, asignaturas, estudiantes, docentes, salas y anotaciones. Es el servicio con mayor cantidad de operaciones del sistema.

## Tecnologías

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven

## Puerto

`8082`

## Endpoints

### Establecimientos

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/establecimiento` | Listar establecimientos |
| GET | `/establecimiento/rbd/{rbd}` | Buscar por RBD |
| GET | `/establecimiento/existe/{rbd}` | Verificar si un RBD existe |
| GET | `/establecimiento/tipos-calificacion` | Listar tipos de calificación |
| GET | `/establecimiento/regiones` | Listar regiones |
| GET | `/establecimiento/comunas` | Listar comunas |

### Cursos

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/establecimiento/{id}/cursos` | Listar cursos del establecimiento |
| POST | `/establecimiento/{id}/cursos` | Crear curso |
| PUT | `/establecimiento/{id}/cursos/{idCurso}` | Actualizar curso |
| DELETE | `/establecimiento/{id}/cursos/{idCurso}` | Eliminar curso |
| PUT | `/establecimiento/{id}/cursos/{idCurso}/profesor-jefe` | Asignar profesor jefe |

### Asignaturas

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/establecimiento/{id}/asignaturas` | Listar asignaturas del establecimiento |
| POST | `/establecimiento/{id}/asignaturas` | Crear asignatura |
| PUT | `/establecimiento/{id}/asignaturas/{idAsignatura}` | Actualizar asignatura |
| DELETE | `/establecimiento/{id}/asignaturas/{idAsignatura}` | Eliminar asignatura |
| GET | `/establecimiento/{id}/cursos/{idCurso}/asignaturas` | Listar asignaturas de un curso |
| POST | `/establecimiento/{id}/cursos/{idCurso}/asignaturas` | Asignar asignatura a curso |
| DELETE | `/establecimiento/{id}/cursos/{idCurso}/asignaturas/{idAsignatura}` | Quitar asignatura de curso |

### Estudiantes

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/establecimiento/{id}/estudiantes` | Listar estudiantes del establecimiento |
| POST | `/establecimiento/{id}/estudiantes` | Crear estudiante |
| GET | `/establecimiento/{id}/cursos/{idCurso}/estudiantes` | Listar estudiantes de un curso |
| POST | `/establecimiento/{id}/cursos/{idCurso}/estudiantes/{idEstudiante}` | Matricular estudiante en curso |
| DELETE | `/establecimiento/{id}/cursos/{idCurso}/estudiantes/{idEstudiante}` | Desmatricular estudiante |

### Docentes

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/establecimiento/{id}/docentes` | Listar docentes del establecimiento |

### Salas

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/establecimiento/{id}/salas` | Listar salas del establecimiento |
| POST | `/establecimiento/{id}/salas` | Crear sala |
| PUT | `/establecimiento/{id}/salas/{idSala}` | Actualizar sala |
| DELETE | `/establecimiento/{id}/salas/{idSala}` | Eliminar sala |

### Anotaciones

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/anotaciones/estudiante/{idEstudiante}` | Anotaciones de un estudiante |
| GET | `/anotaciones/docente/{idDocente}` | Anotaciones de un docente |
| GET | `/anotaciones/citaciones-pendientes` | Citaciones pendientes |
| POST | `/anotaciones` | Crear anotación |
| PUT | `/anotaciones/{idAnotacion}` | Actualizar anotación |
| DELETE | `/anotaciones/{idAnotacion}` | Eliminar anotación |

## Ejecución local

**Requisitos:** Java 21, Maven, PostgreSQL corriendo en `localhost:5432` con base de datos `colegio_db` inicializada con los scripts de `database/`.

```bash
cd establecimiento/establecimiento
./mvnw spring-boot:run
```

El servicio estará disponible en `http://localhost:8082`.

## Ejecución con Docker

Desde la raíz del proyecto:

```bash
docker compose up establecimiento-service
```

O construir la imagen manualmente:

```bash
cd establecimiento/establecimiento
docker build -t establecimiento-service .
docker run -p 8082:8082 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/colegio_db \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  establecimiento-service
```

## Variables de entorno

| Variable | Valor por defecto | Descripción |
|----------|------------------|-------------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/colegio_db` | URL de conexión a PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Usuario de la base de datos |
| `SPRING_DATASOURCE_PASSWORD` | `123456` | Contraseña de la base de datos |
