
# Microservicio Asistencia

Microservicio REST para registro y consulta de asistencia de estudiantes del Colegio Bernardo O'Higgins.

## Tecnologías
- Java 22
- Spring Boot 4.0.6
- Spring Data JPA
- PostgreSQL
- Maven

## Puerto
8083

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | /asistencia/estudiante/{id} | Obtiene asistencia por estudiante |
| GET | /asistencia/estudiante/{id}/fecha/{fecha} | Obtiene asistencia por estudiante y fecha |
| POST | /asistencia | Registra una asistencia |

## Cómo ejecutar

**1. Requisitos previos**
- Java 22 instalado
- PostgreSQL corriendo en puerto 5432
- Base de datos `colegio` creada

**2. Configurar variables de entorno**
DB_PASSWORD=tu_contraseña
GET http://localhost:8083/asistencia/estudiante/1
## Patrones aplicados
- Repository Pattern
- DTO Pattern
- GlobalExceptionHandler