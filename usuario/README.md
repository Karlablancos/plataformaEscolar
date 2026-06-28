# usuario-service

Microservicio de gestión de usuarios y autenticación para la plataforma escolar. Administra usuarios, roles y el proceso de login con credenciales por establecimiento.

## Tecnologías

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven

## Puerto

`8081`

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/usuarios/login` | Autenticación de usuario por establecimiento |
| GET | `/usuarios` | Listar todos los usuarios |
| GET | `/usuarios?idEstablecimiento={id}` | Listar usuarios por establecimiento |
| GET | `/usuarios/{id}` | Obtener usuario por ID |
| POST | `/usuarios` | Crear nuevo usuario |
| PUT | `/usuarios/{id}` | Actualizar usuario |
| PATCH | `/usuarios/{id}/estado` | Cambiar estado del usuario (Activo/Inactivo) |
| PUT | `/usuarios/{id}/password` | Cambiar contraseña |
| DELETE | `/usuarios/{id}` | Eliminar usuario |
| GET | `/usuarios/roles` | Listar roles disponibles |
| POST | `/usuarios/roles` | Crear nuevo rol |

## Ejecución local

**Requisitos:** Java 21, Maven, PostgreSQL corriendo en `localhost:5432` con base de datos `colegio_db`.

```bash
cd usuario/usuario
./mvnw spring-boot:run
```

El servicio estará disponible en `http://localhost:8081`.

## Ejecución con Docker

Desde la raíz del proyecto:

```bash
docker compose up usuario-service
```

O construir la imagen manualmente:

```bash
cd usuario/usuario
docker build -t usuario-service .
docker run -p 8081:8081 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/colegio_db \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  usuario-service
```

## Variables de entorno

| Variable | Valor por defecto | Descripción |
|----------|------------------|-------------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/colegio_db` | URL de conexión a PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Usuario de la base de datos |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` | Contraseña de la base de datos |
