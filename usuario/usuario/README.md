# Microservicio Usuario

Microservicio REST para gestión de usuarios y autenticación del sistema de gestión escolar Colegio Bernardo O'Higgins.

## Tecnologías
- Java 21
- Spring Boot 3.4.5
- Spring Data JPA
- Spring Security (BCrypt)
- PostgreSQL
- Maven

## Puerto
8081

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | /usuarios | Lista todos los usuarios |
| GET | /usuarios/{id} | Busca usuario por ID |
| POST | /usuarios | Crea un usuario |
| POST | /usuarios/login | Autentica usuario con BCrypt |
| DELETE | /usuarios/{id} | Elimina un usuario |

## Cómo ejecutar

**1. Requisitos previos**
- Java 21 instalado
- PostgreSQL corriendo en puerto 5432
- Base de datos `colegio` creada

**2. Configurar variables de entorno**
DB_PASSWORD=tu_contraseña
**3. Ejecutar**
```bash
./mvnw spring-boot:run
```

**4. Verificar**
GET http://localhost:8081/usuarios
## Patrones aplicados
- Repository Pattern
- DTO Pattern
- BCrypt Password Encoding