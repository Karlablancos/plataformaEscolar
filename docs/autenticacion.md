# Flujo de Autenticacion

## Componentes involucrados

- Frontend: `frontend/src/stores/authStore.js`, `frontend/src/api/axios.js`, `frontend/src/router/index.ts`.
- BFF: `AuthController`, `JwtUtil`, `JwtFilter`, `SecurityConfig`.
- Usuario service: `UsuarioController`, `UsuarioService`.
- Establecimiento service: `EstablecimientoController`.

## Flujo de seleccion de establecimiento

1. La ruta inicial `/` carga `SeleccionarEstablecimientoView`.
2. El frontend valida el RBD con `GET /auth/validar-rbd/{rbd}`.
3. El BFF consulta `establecimiento-service` en `/establecimiento/rbd/{rbd}`.
4. Si existe, el BFF responde `idEstablecimiento` y `nombre`.
5. El frontend guarda datos del establecimiento en `localStorage`.

## Flujo de login

1. El frontend ejecuta `authStore.login(rbd, username, password)`.
2. El frontend envia `POST /auth/login` al BFF con:

```json
{
  "rbd": "string",
  "username": "string",
  "password": "string"
}
```

3. `AuthController.login` valida que `rbd`, `username` y `password` no sean `null`.
4. El BFF consulta `establecimiento-service`:

```text
GET /establecimiento/rbd/{rbd}
```

5. El BFF extrae `idEstablecimiento` y `nombre`.
6. El BFF consulta `usuario-service`:

```text
POST /usuarios/login
```

Payload enviado al microservicio:

```json
{
  "username": "string",
  "password": "string",
  "idEstablecimiento": 1
}
```

7. `UsuarioService.login` busca usuario por `username` e `idEstablecimiento`.
8. El login interno valida:

- password con `passwordEncoder.matches`.
- usuario con estado `ACTIVO`.
- usuario no bloqueado.

9. El BFF normaliza el rol.
10. El BFF genera un JWT con `JwtUtil.generarToken`.
11. El frontend recibe y persiste `token`, `user` y `rbd` en `localStorage`.

## Respuesta de login

`LoginResponse` del BFF contiene:

| Campo | Fuente |
| --- | --- |
| `token` | JWT generado por `JwtUtil`. |
| `username` | Request de login. |
| `rol` | Rol normalizado desde `nombreRol` o `idRol`. |
| `nombreColegio` | Nombre retornado por establecimiento. |
| `rbd` | Request de login. |
| `idEstablecimiento` | Usuario autenticado o establecimiento resuelto por RBD. |

## JWT

`JwtUtil` genera tokens con:

- `subject`: `username`.
- claim `rbd`.
- claim `rol`.
- `issuedAt`.
- `expiration`.
- firma con `jwt.secret`.

Expiracion configurada en codigo:

- `10800000` milisegundos.

El secreto se obtiene desde:

- propiedad `jwt.secret`.
- `application.yaml` usa `${JWT_SECRET:colegio-bernardo-ohiggins-secret-key-2026}`.

## Rutas publicas segun JwtFilter

`JwtFilter` define como publicas:

- `/auth/login`
- `/auth/validar-rbd`
- `/actuator`

Para rutas no publicas, el filtro:

1. Lee el header `Authorization`.
2. Requiere formato `Bearer <token>`.
3. Valida token con `JwtUtil.validarToken`.
4. Responde `401` si falta el token o no es valido.

## Configuracion de seguridad observada

`SecurityConfig`:

- deshabilita CSRF.
- habilita CORS para `http://localhost:5173`.
- permite metodos `GET`, `POST`, `PUT`, `PATCH`, `DELETE`, `OPTIONS`.
- permite headers `*`.
- permite credenciales.
- deshabilita HTTP Basic.
- deshabilita form login.
- declara `pathMatchers("/auth/**").permitAll()`.
- declara `pathMatchers("/actuator/**").permitAll()`.
- declara `anyExchange().permitAll()`.

## Manejo frontend del token

`frontend/src/api/axios.js`:

- usa `VITE_API_URL` o `http://localhost:8080/`.
- agrega `Authorization: Bearer <token>` si existe `localStorage.token`.
- ante `401` o `403`, elimina `token`, `user`, `establecimientoActivo` y `rbd`; luego redirige a `/login`.

`frontend/src/router/index.ts`:

- requiere `establecimientoActivo` para entrar a `/login`.
- requiere `token` para rutas con `requiresAuth`.
- valida roles mediante `meta.roles`.
- redirige usuarios autenticados desde rutas publicas hacia dashboard segun rol.

## Roles observados

Roles usados por frontend:

- `ADMIN`
- `UTP`
- `DIRECTOR`
- `PROFESOR`
- `SOSTENEDOR`

Normalizacion de roles en BFF:

| Valor origen | Valor normalizado |
| --- | --- |
| `ADMINISTRADOR` | `ADMIN` |
| `DOCENTE` | `PROFESOR` |
| `SOSTENEDOR` | `SOSTENEDOR` |

Fallback por `idRol`:

| `idRol` | Rol |
| ---: | --- |
| `1` | `ADMIN` |
| `2` | `PROFESOR` |
| `5` | `SOSTENEDOR` |
| Otro | `APODERADO` |

## Observaciones

- `JwtFilter` implementa validacion de token para rutas no publicas.
- `SecurityConfig` declara `anyExchange().permitAll()`. La documentacion no asume el efecto final de combinacion entre `SecurityConfig` y `JwtFilter`; solo registra ambas configuraciones.
- No se encontro endpoint de refresh token.
- No se encontro logout backend; el logout observado es limpieza de estado en frontend.

## Fuentes

- `frontend/src/stores/authStore.js`
- `frontend/src/api/axios.js`
- `frontend/src/router/index.ts`
- `bff-gateway/bff-gateway/src/main/java/com/colegio/bff/controller/AuthController.java`
- `bff-gateway/bff-gateway/src/main/java/com/colegio/bff/security/JwtUtil.java`
- `bff-gateway/bff-gateway/src/main/java/com/colegio/bff/security/JwtFilter.java`
- `bff-gateway/bff-gateway/src/main/java/com/colegio/bff/config/SecurityConfig.java`
- `usuario/usuario/src/main/java/com/colegio/usuario/service/UsuarioService.java`
- `usuario/usuario/src/main/java/com/colegio/usuario/controller/UsuarioController.java`
