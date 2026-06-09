# Rutas Frontend

Esta documentacion se basa en `frontend/src/router/index.ts` y llamadas HTTP observadas en `frontend/src`.

## Configuracion general

- Router: `createRouter`.
- History: `createWebHistory`.
- Guard global: `router.beforeEach`.
- Layout principal para rutas admin y profesor: `@/components/AppLayout.vue`.

## Rutas publicas

| Ruta | Nombre | Componente | Meta |
| --- | --- | --- | --- |
| `/` | `seleccionar-establecimiento` | `SeleccionarEstablecimientoView` | `publicOnly: true` |
| `/login` | `login` | `LoginView` | `requiresEstablecimiento: true`, `publicOnly: true` |

## Rutas admin

Ruta padre:

- Path: `/admin`
- Layout: `AppLayout`
- Meta: `requiresAuth: true`
- Roles: `ADMIN`, `UTP`, `DIRECTOR`

| Ruta final | Nombre | Componente |
| --- | --- | --- |
| `/admin` | - | Redirect a `/admin/dashboard` |
| `/admin/dashboard` | `dashboard` | `DashboardView` |
| `/admin/establecimiento` | `establecimiento` | `EstablecimientoView` |
| `/admin/usuarios` | `usuarios` | `UsuariosView` |
| `/admin/alumnos` | `alumnos` | `AlumnosListView` |
| `/admin/alumnos/nuevo` | `alumnos-nuevo` | `AlumnoCreateView` |
| `/admin/alumnos/:id` | `alumnos-detalle` | `AlumnoDetailView` |
| `/admin/alumnos/:id/editar` | `alumnos-editar` | `AlumnoDetailView` |
| `/admin/cursos` | `cursos` | `CursosView` |
| `/admin/cursos/:id` | `curso-detalle` | `CursoDetalleView` |
| `/admin/profesores` | `profesores` | `ProfesoresView` |
| `/admin/profesores/nuevo` | `profesores-nuevo` | `ProfesorFormView` |
| `/admin/profesores/:id` | `profesores-detalle` | `ProfesorDetailView` |
| `/admin/profesores/:id/editar` | `profesores-editar` | `ProfesorFormView` |
| `/admin/asignaturas` | `asignaturas` | `AsignaturasView` |

## Rutas sostenedor

| Ruta | Nombre | Componente | Meta |
| --- | --- | --- | --- |
| `/sostenedor/dashboard` | `sostenedor-dashboard` | `SostenedorDashboardView` | `requiresAuth: true`, roles `SOSTENEDOR` |

## Rutas profesor

Ruta padre:

- Path: `/profesor`
- Layout: `AppLayout`
- Meta: `requiresAuth: true`
- Roles: `PROFESOR`

| Ruta final | Nombre | Componente |
| --- | --- | --- |
| `/profesor` | - | Redirect a `/profesor/dashboard` |
| `/profesor/dashboard` | `profesor-dashboard` | `DashboardView` |
| `/profesor/cursos` | `profesor-cursos` | `CursosView` |
| `/profesor/cursos/:id` | `profesor-curso-detalle` | `CursoDetalleView` |

## Ruta fallback

| Ruta | Comportamiento |
| --- | --- |
| `/:pathMatch(.*)*` | Redirect a `/` |

## Guard de navegacion

El guard global:

- Lee `token` desde `localStorage`.
- Lee `user` desde `localStorage`.
- Lee `establecimientoActivo` desde `localStorage`.
- Redirige a `/` si una ruta requiere establecimiento y no existe `establecimientoActivo`.
- Redirige a `/login` o `/` si una ruta requiere autenticacion y no existe token.
- Limpia `token` y `user` si hay token sin usuario.
- Redirige usuarios autenticados fuera de rutas `publicOnly`.
- Valida `meta.roles` contra `user.rol`.

Redirecciones por rol observadas:

| Rol | Redireccion |
| --- | --- |
| `PROFESOR` | `/profesor/dashboard` |
| `SOSTENEDOR` | `/sostenedor/dashboard` |
| Otro usuario autenticado | `/admin/dashboard` |

## Cliente HTTP

`frontend/src/api/axios.js` configura:

- `baseURL`: `import.meta.env.VITE_API_URL || 'http://localhost:8080/'`.
- `timeout`: `10000`.
- Request interceptor: agrega `Authorization: Bearer <token>` si existe `localStorage.token`.
- Response interceptor: ante `401` o `403`, elimina datos de sesion y redirige a `/login`.

## Llamadas HTTP observadas

| Archivo | Metodo y ruta |
| --- | --- |
| `stores/authStore.js` | `POST /auth/login` |
| `views/establecimiento/SeleccionarEstablecimientoView.vue` | `GET /auth/validar-rbd/{rbd}` |
| `stores/establecimientoStore.js` | `GET /establecimiento/rbd/{rbd}` |
| `views/DashboardView.vue` | `GET /establecimiento/{idEstablecimiento}/estudiantes` |
| `views/DashboardView.vue` | `GET /usuarios?idEstablecimiento={idEstablecimiento}` |
| `views/DashboardView.vue` | `GET /establecimiento/{idEstablecimiento}/cursos` |
| `views/DashboardView.vue` | `GET /establecimiento/{idEstablecimiento}/asignaturas` |
| `stores/alumnosStore.js` | `GET /establecimiento/{idEstablecimiento}/estudiantes` |
| `stores/usuariosStore.js` | `GET /usuarios` con query `idEstablecimiento` |
| `stores/usuariosStore.js` | `PUT /usuarios/{idUsuario}` |
| `stores/usuariosStore.js` | `PATCH /usuarios/{idUsuario}/estado` |
| `stores/usuariosStore.js` | `POST /usuarios` |
| `stores/usuariosStore.js` | `DELETE /usuarios/{idUsuario}` |
| `views/sostenedor/SostenedorDashboardView.vue` | `GET /establecimiento` |
| `views/sostenedor/SostenedorDashboardView.vue` | `GET /establecimiento/{idEstab}` |

## Uso de mocks y localStorage observado

Se observaron imports o usos de datos mock/localStorage en:

- `frontend/src/data/*`
- `frontend/src/stores/asignaturasStore.js`
- `frontend/src/stores/cursosStore.js`
- `frontend/src/stores/docentesStore.js`
- `frontend/src/stores/alumnosStore.js`
- vistas de alumnos y profesores que importan `comunasMock`.

## Fuentes

- `frontend/src/router/index.ts`
- `frontend/src/api/axios.js`
- `frontend/src/stores/authStore.js`
- `frontend/src/stores/establecimientoStore.js`
- Busqueda de `api.get`, `api.post`, `api.put`, `api.patch`, `api.delete` en `frontend/src`
- Busqueda de `Mock`, `mock` y `localStorage` en `frontend/src`
