# Descripción de Persistencia de Datos

## plataformaEscolar - Colegio Bernardo O'Higgins (RBD 646)

## DSY1106 Desarrollo Fullstack III - DuocUC 2026

## Tecnología utilizada

- **Motor:** PostgreSQL 16 en contenedor Docker
- **ORM:** Spring Data JPA con Hibernate 7
- **Pool de conexiones:** HikariCP
- **Cobertura:** JaCoCo 0.8.11 (mínimo 60%)

## Estructura de la base de datos

La base de datos `colegio_db` contiene 27 tablas normalizadas en 3FN:

- Gestión institucional: establecimiento, region, comuna, tipo_establecimiento
- Usuarios y roles: usuario, rol
- Académico: curso, asignatura, evaluacion, nota, tipo_evaluacion, periodo_academico
- Personas: docente, estudiante, apoderado, estudiante_apoderado
- Operacional: asistencia, anotacion, sala, horario, curso_asignatura
- Documentos: tipo_documento, documento_estudiante, antecedente_salud, antecedente_familiar

## Cómo se garantiza la persistencia

1. **Docker Volumes:** Los datos persisten en el volumen `colegio_postgres_data`
2. **Scripts de inicialización:** Al crear el volumen se ejecutan automáticamente:
   - `01-script.sql` — DDL completo
   - `02-triggers.sql` — Triggers de auditoría
   - `03-indices.sql` — Índices y procedimientos almacenados
   - `04-data.sql` — Datos iniciales (roles, usuarios, catálogos)
3. **JPA/Hibernate:** Cada microservicio usa `spring.jpa.hibernate.ddl-auto=none`
   para no modificar el esquema en producción
4. **Multitenancy:** El campo `id_establecimiento` en cada tabla garantiza
   el aislamiento de datos por colegio (RBD)
5. **JWT:** El RBD viaja embebido en el token JWT para identificar
   el establecimiento en cada petición sin re-consultar la BD

## Patrones aplicados

- **Repository Pattern:** Spring Data JPA repositories por entidad
- **DTO Pattern:** Separación entre entidades JPA y objetos de transferencia
- **Factory Method:** Registro de establecimientos por RBD (multitenant)
