# asistencia-service

Microservicio de registro y consulta de asistencia escolar. Permite registrar la asistencia diaria de estudiantes y calcular resúmenes de presencia por estudiante.

## Tecnologías

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven

## Puerto

`8083`

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/asistencia` | Registrar asistencia de un estudiante |
| GET | `/asistencia/estudiante/{idEstudiante}` | Obtener todo el historial de asistencia de un estudiante |
| GET | `/asistencia/estudiante/{idEstudiante}/fecha/{fecha}` | Obtener asistencia de un estudiante en una fecha específica (`yyyy-MM-dd`) |
| GET | `/asistencia/estudiante/{idEstudiante}/resumen` | Calcular resumen de asistencia (total clases, presentes, porcentaje) |

## Ejecución local

**Requisitos:** Java 21, Maven, PostgreSQL corriendo en `localhost:5432` con base de datos `colegio_db`.

```bash
cd asistencia/asistencia
./mvnw spring-boot:run
```

El servicio estará disponible en `http://localhost:8083`.

## Ejecución con Docker

Desde la raíz del proyecto:

```bash
docker compose up asistencia-service
```

O construir la imagen manualmente:

```bash
cd asistencia/asistencia
docker build -t asistencia-service .
docker run -p 8083:8083 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/colegio_db \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  asistencia-service
```

## Variables de entorno

| Variable | Valor por defecto | Descripción |
|----------|------------------|-------------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/colegio_db` | URL de conexión a PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Usuario de la base de datos |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` | Contraseña de la base de datos |
