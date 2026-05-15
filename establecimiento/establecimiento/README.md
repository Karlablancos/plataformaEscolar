# Microservicio Establecimiento

Microservicio REST para gestión y validación de establecimientos educacionales del sistema de gestión escolar Colegio Bernardo O'Higgins.

## Tecnologías
- Java 22
- Spring Boot 4.0.6
- Spring Data JPA
- PostgreSQL
- Maven

## Puerto 

8082

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | /establecimiento/rbd/{rbd} | Busca establecimiento por RBD |
| GET | /establecimiento/existe/{rbd} | Verifica si existe un RBD |

## Cómo ejecutar

**1. Requisitos previos**
- Java 22 instalado
- PostgreSQL corriendo en puerto 5432
- Base de datos `colegio` creada

**2. Configurar variables de entorno**

DB_PASSWORD=tu_contraseña

**3. Ejecutar**
```bash
./mvnw spring-boot:run
```

**4. Verificar**
GET http://localhost:8082/establecimiento/rbd/12345

## Patrones aplicados
- Repository Pattern
- DTO Pattern
- GlobalExceptionHandler