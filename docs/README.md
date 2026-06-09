# Plataforma Escolar - Documentacion Tecnica

Esta carpeta contiene documentacion generada a partir del codigo fuente existente del proyecto `plataformaEscolar`.

La documentacion no agrega funcionalidades ni define comportamiento no presente en el repositorio. Las discrepancias detectadas entre archivos de configuracion, README y codigo fuente se registran como observaciones.

## Indice

- [Arquitectura](./arquitectura.md)
- [Tecnologias](./tecnologias.md)
- [Flujo de autenticacion](./autenticacion.md)
- [Base de datos](./base-de-datos.md)
- [Rutas frontend](./frontend-rutas.md)
- [Endpoints backend](./backend-endpoints.md)
- [Decisiones tecnicas](./decisiones-tecnicas.md)
- [Changelog](./changelog/README.md)

## Componentes documentados

- `frontend`: aplicacion Vue 3.
- `bff-gateway/bff-gateway`: Backend For Frontend y API Gateway.
- `usuario/usuario`: microservicio de usuarios y autenticacion interna.
- `establecimiento/establecimiento`: microservicio de establecimiento, cursos, asignaturas y estudiantes.
- `asistencia/asistencia`: microservicio de asistencia.
- `database`: scripts SQL de estructura, triggers, indices, procedimientos y poblado.

## Fuentes principales

- `repositorios.txt`
- `docker-compose.yml`
- `frontend/package.json`
- `frontend/src/router/index.ts`
- `frontend/src/api/axios.js`
- `bff-gateway/bff-gateway/src/main/resources/application.yaml`
- `bff-gateway/bff-gateway/src/main/java/com/colegio/bff`
- `usuario/usuario/src/main/java/com/colegio/usuario`
- `establecimiento/establecimiento/src/main/java/com/colegio/establecimiento`
- `asistencia/asistencia/src/main/java/com/colegio/asistencia`
- `database/script_colegio.sql`
- `database/triggers_colegio.sql`
- `database/indices_y_procedimientos.sql`
