# Plataforma Escolar — Frontend

Aplicación web del sistema de gestión escolar. Desarrollada con Vue 3, Pinia y Vue Router. Permite gestionar alumnos, docentes, cursos, asignaturas, asistencia, notas y usuarios según el rol del usuario autenticado (Administrador, Director o Docente).

## Tecnologías

- Vue 3 (Composition API)
- TypeScript
- Pinia (estado global)
- Vue Router
- Vite
- Axios

## Puerto

`5173` (desarrollo) · `5173` (Docker)

## Roles y acceso

| Rol | Rutas disponibles |
|-----|------------------|
| ADMINISTRADOR | `/admin/*` — acceso completo |
| DIRECTOR | `/admin/*` — sin gestión de usuarios ni salas |
| DOCENTE | `/profesor/*` — cursos, asistencia, libro de notas |

## Ejecución local

**Requisitos:** Node.js 18+, el BFF Gateway corriendo en `http://localhost:8080`.

```bash
cd frontend
npm install
npm run dev
```

La aplicación estará disponible en `http://localhost:5173`.

## Ejecución con Docker

Desde la raíz del proyecto (levanta todos los servicios):

```bash
# Crear archivo .env en la raíz con:
# POSTGRES_PASSWORD=postgres
# JWT_SECRET=clave_secreta_segura

docker compose up --build
```

El frontend estará disponible en `http://localhost:5173`.

## Scripts disponibles

| Comando | Descripción |
|---------|-------------|
| `npm run dev` | Servidor de desarrollo con hot-reload |
| `npm run build` | Compilar para producción |
| `npm run preview` | Vista previa del build de producción |
| `npm run type-check` | Verificación de tipos TypeScript |
| `npm run lint` | Linting con ESLint |

## Variables de entorno

La URL base de la API se configura en `src/api/axios.ts` apuntando al BFF Gateway (`http://localhost:8080` por defecto).
