# BFF Gateway

Backend For Frontend con API Gateway, JWT y seguridad centralizada para el sistema de gestión escolar Colegio Bernardo O'Higgins.

## Tecnologías
- Java 22
- Spring Boot 4.0.6
- Spring Cloud Gateway
- Spring Security
- JWT (JJWT 0.12.3)
- Maven

## Puerto
8086

## Endpoints públicos

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | /auth/login | Login con RBD + username + password |
| GET | /auth/validar-rbd/{rbd} | Valida si un RBD existe |

## Rutas protegidas (requieren JWT)

| Prefijo | Redirige a |
|---------|-----------|
| /establecimiento/** | localhost:8082 |
| /usuarios/** | localhost:8081 |
| /asistencia/** | localhost:8083 |

## Cómo ejecutar

**1. Requisitos previos**
- Java 22 instalado
- Microservicios corriendo en sus puertos

**2. Ejecutar**
```bash
./mvnw spring-boot:run
```

**3. Login de prueba**
POST http://localhost:8086/auth/login
{
"rbd": "12345",
"username": "admin.bohiggins",
"password": "admin123"
}

## Patrones aplicados
- API Gateway Pattern
- JWT Authentication
- CORS Configuration