<template>
  <div class="container-fluid py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <div>
        <h1 class="h3 mb-1">Usuarios</h1>
        <p class="text-muted mb-0">
          Administración de usuarios del establecimiento.
        </p>
      </div>

      <button
        class="btn btn-primary rounded-pill"
        data-bs-toggle="modal"
        data-bs-target="#crearUsuarioModal"
        @click="abrirModalCrear"
      >
        <i class="bi bi-plus-lg me-1"></i>
        Nuevo usuario
      </button>
    </div>

    <div v-if="mensajeExito" class="alert alert-success alert-dismissible fade show">
      {{ mensajeExito }}
      <button type="button" class="btn-close" @click="mensajeExito = ''"></button>
    </div>

    <div v-if="mensajeError" class="alert alert-danger alert-dismissible fade show">
      {{ mensajeError }}
      <button type="button" class="btn-close" @click="mensajeError = ''"></button>
    </div>

    <div class="card border-0 shadow-sm rounded-4">
      <div class="card-body">
        <div class="row g-3 mb-3">
          <div class="col-12 col-md-5">
            <label class="form-label">Buscar</label>
            <input
              v-model="busqueda"
              type="text"
              class="form-control"
              placeholder="Buscar por usuario o correo"
            />
          </div>

          <div class="col-12 col-md-3">
            <label class="form-label">Rol</label>
            <select
              class="form-select"
              :value="filtroRol"
              @change="seleccionarOpcionRol"
            >
              <option value="">Todos</option>
              <option v-for="rol in roles" :key="rol.idRol" :value="String(rol.idRol)">
                {{ rol.nombreRol }}
              </option>
              <option value="__nuevo__">+ Agregar nuevo rol</option>
            </select>

            <div v-if="mostrarFormularioRol" class="border rounded-3 p-3 mt-2 bg-light">
              <div class="mb-2">
                <label class="form-label mb-1">Nombre del rol</label>
                <input
                  v-model="nuevoRol.nombreRol"
                  type="text"
                  class="form-control form-control-sm"
                  placeholder="ej: COORDINADOR"
                  required
                  @input="(e) => (nuevoRol.nombreRol = e.target.value.toUpperCase())"
                />
              </div>
              <div class="mb-2">
                <label class="form-label mb-1">Descripción</label>
                <input
                  v-model="nuevoRol.descripcion"
                  type="text"
                  class="form-control form-control-sm"
                  placeholder="Descripción del rol"
                  required
                />
              </div>
              <div v-if="errorNuevoRol" class="text-danger small mb-2">{{ errorNuevoRol }}</div>
              <div class="d-flex gap-2">
                <button
                  type="button"
                  class="btn btn-success btn-sm rounded-pill"
                  :disabled="!nuevoRol.nombreRol || !nuevoRol.descripcion"
                  @click="guardarNuevoRol"
                >
                  Guardar
                </button>
                <button
                  type="button"
                  class="btn btn-outline-secondary btn-sm rounded-pill"
                  @click="cancelarNuevoRol"
                >
                  Cancelar
                </button>
              </div>
            </div>
          </div>

          <div class="col-12 col-md-3">
            <label class="form-label">Estado</label>
            <select v-model="filtroEstado" class="form-select">
              <option value="">Todos</option>
              <option value="ACTIVO">Activo</option>
              <option value="INACTIVO">Inactivo</option>
            </select>
          </div>

          <div class="col-12 col-md-1 d-flex align-items-end">
            <button class="btn btn-outline-secondary w-100" @click="limpiarFiltros">
              <i class="bi bi-x-lg"></i>
            </button>
          </div>
        </div>

        <div v-if="usuariosStore.loading" class="text-center py-5">
          <div class="spinner-border text-primary" role="status"></div>
          <p class="text-muted mt-3 mb-0">Cargando usuarios...</p>
        </div>

        <div v-else class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
              <tr>
                <th>ID</th>
                <th>Usuario</th>
                <th>Correo</th>
                <th>Rol</th>
                <th>Estado</th>
                <th>Bloqueado</th>
                <th class="text-end">Acciones</th>
              </tr>
            </thead>

            <tbody>
              <tr v-if="usuariosFiltrados.length === 0">
                <td colspan="7" class="text-center text-muted py-4">
                  No hay usuarios para mostrar.
                </td>
              </tr>

              <tr v-for="usuario in usuariosFiltrados" :key="usuario.id_usuario">
                <td>{{ usuario.id_usuario }}</td>

                <td>
                  <strong>{{ usuario.username }}</strong>
                </td>

                <td>{{ usuario.correo_electronico }}</td>

                <td>
                  <span class="badge text-bg-light border">
                    {{ nombreRol(usuario.id_rol) }}
                  </span>
                </td>

                <td>
                  <div class="d-flex align-items-center gap-2">
                    <div class="form-check form-switch mb-0">
                      <input
                        class="form-check-input"
                        type="checkbox"
                        role="switch"
                        :checked="usuario.estado === 'ACTIVO'"
                        :disabled="usuariosStore.loading"
                        @change="toggleEstadoUsuario(usuario)"
                      />
                    </div>

                    <span
                      class="badge"
                      :class="usuario.estado === 'ACTIVO' ? 'text-bg-success' : 'text-bg-secondary'"
                    >
                      {{ usuario.estado }}
                    </span>
                  </div>
                </td>

                <td>
                  <span
                    class="badge"
                    :class="usuario.bloqueado ? 'text-bg-danger' : 'text-bg-light border'"
                  >
                    {{ usuario.bloqueado ? 'Sí' : 'No' }}
                  </span>
                </td>

                <td class="text-end">
                  <div class="dropdown">
                    <button
                      class="btn btn-outline-secondary btn-sm dropdown-toggle rounded-pill"
                      type="button"
                      data-bs-toggle="dropdown"
                    >
                      Acciones
                    </button>

                    <ul class="dropdown-menu dropdown-menu-end">
                      <li>
                        <button
                          class="dropdown-item"
                          data-bs-toggle="modal"
                          data-bs-target="#crearUsuarioModal"
                          @click="abrirModalEditar(usuario)"
                        >
                          <i class="bi bi-pencil-square me-2"></i>
                          Editar
                        </button>
                      </li>

                      <li><hr class="dropdown-divider" /></li>

                      <li>
                        <button
                          class="dropdown-item text-danger"
                          data-bs-toggle="modal"
                          data-bs-target="#eliminarUsuarioModal"
                          @click="abrirModalEliminar(usuario)"
                        >
                          <i class="bi bi-trash me-2"></i>
                          Eliminar
                        </button>
                      </li>
                    </ul>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Modal crear / editar usuario -->
    <div
      class="modal fade"
      id="crearUsuarioModal"
      tabindex="-1"
      aria-labelledby="crearUsuarioModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content rounded-4 border-0">
          <div class="modal-header">
            <h5 class="modal-title" id="crearUsuarioModalLabel">
              {{ modoEdicion ? 'Editar usuario' : 'Crear usuario' }}
            </h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>

          <form @submit.prevent="guardarUsuario">
            <div class="modal-body">
              <div class="row g-3">
                <div class="col-12 col-md-6">
                  <label class="form-label">Username</label>
                  <input
                    v-model.trim="form.username"
                    type="text"
                    class="form-control"
                    required
                    placeholder="ej: jefe.utp"
                  />
                </div>

                <div class="col-12 col-md-6">
                  <label class="form-label">Correo electrónico</label>
                  <input
                    v-model.trim="form.correo_electronico"
                    type="email"
                    class="form-control"
                    required
                    placeholder="correo@colegio.cl"
                  />
                </div>

                <div v-if="!modoEdicion" class="col-12 col-md-6">
                  <label class="form-label">Contraseña</label>
                  <input
                    v-model="form.password"
                    type="password"
                    class="form-control"
                    required
                    minlength="4"
                  />
                </div>

                <div class="col-12 col-md-6">
                  <label class="form-label">Rol</label>
                  <select
                    class="form-select"
                    :value="form.id_rol ?? ''"
                    required
                    @change="seleccionarOpcionRolModal"
                  >
                    <option value="" disabled>Seleccionar rol</option>
                    <option v-for="rol in roles" :key="rol.idRol" :value="rol.idRol">
                      {{ rol.nombreRol }}
                    </option>
                    <option value="__nuevo_modal__">+ Agregar nuevo rol</option>
                  </select>

                  <div v-if="mostrarFormularioRolModal" class="border rounded-3 p-3 mt-2 bg-light">
                    <div class="mb-2">
                      <label class="form-label mb-1">Nombre del rol</label>
                      <input
                        v-model="nuevoRolModal.nombreRol"
                        type="text"
                        class="form-control form-control-sm"
                        placeholder="ej: COORDINADOR"
                        required
                        @input="(e) => (nuevoRolModal.nombreRol = e.target.value.toUpperCase())"
                      />
                    </div>
                    <div class="mb-2">
                      <label class="form-label mb-1">Descripción</label>
                      <input
                        v-model="nuevoRolModal.descripcion"
                        type="text"
                        class="form-control form-control-sm"
                        placeholder="Descripción del rol"
                        required
                      />
                    </div>
                    <div v-if="errorNuevoRolModal" class="text-danger small mb-2">
                      {{ errorNuevoRolModal }}
                    </div>
                    <div class="d-flex gap-2">
                      <button
                        type="button"
                        class="btn btn-success btn-sm rounded-pill"
                        :disabled="!nuevoRolModal.nombreRol || !nuevoRolModal.descripcion"
                        @click="guardarNuevoRolModal"
                      >
                        Guardar
                      </button>
                      <button
                        type="button"
                        class="btn btn-outline-secondary btn-sm rounded-pill"
                        @click="cancelarNuevoRolModal"
                      >
                        Cancelar
                      </button>
                    </div>
                  </div>
                </div>

                <div class="col-12 col-md-6">
                  <label class="form-label">ID establecimiento</label>
                  <input
                    v-model.number="form.id_establecimiento"
                    type="number"
                    class="form-control"
                    required
                    min="1"
                    :disabled="modoEdicion"
                  />
                </div>

                <div class="col-12 col-md-6">
                  <label class="form-label">Estado</label>
                  <select v-model="form.estado" class="form-select" required>
                    <option value="ACTIVO">Activo</option>
                    <option value="INACTIVO">Inactivo</option>
                  </select>
                </div>
              </div>
            </div>

            <div class="modal-footer">
              <button
                type="button"
                class="btn btn-outline-secondary rounded-pill"
                data-bs-dismiss="modal"
              >
                Cancelar
              </button>

              <button
                type="submit"
                class="btn btn-success rounded-pill"
                :disabled="usuariosStore.loading"
              >
                <span
                  v-if="usuariosStore.loading"
                  class="spinner-border spinner-border-sm me-2"
                ></span>
                {{ modoEdicion ? 'Guardar cambios' : 'Guardar usuario' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Modal eliminar usuario -->
    <div
      class="modal fade"
      id="eliminarUsuarioModal"
      tabindex="-1"
      aria-labelledby="eliminarUsuarioModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content rounded-4 border-0">
          <div class="modal-header">
            <h5 class="modal-title" id="eliminarUsuarioModalLabel">
              Confirmar eliminación
            </h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>

          <div class="modal-body">
            <p class="mb-2">¿Seguro que quieres eliminar este usuario?</p>

            <div v-if="usuarioSeleccionado" class="alert alert-light border mb-0">
              <strong>{{ usuarioSeleccionado.username }}</strong><br />
              <small class="text-muted">
                {{ usuarioSeleccionado.correo_electronico }}
              </small>
            </div>

            <p class="text-muted small mt-3 mb-0">
              Si el usuario está asociado a docentes, estudiantes, apoderados o mensajes,
              el sistema puede impedir la eliminación.
            </p>
          </div>

          <div class="modal-footer">
            <button
              type="button"
              class="btn btn-outline-secondary rounded-pill"
              data-bs-dismiss="modal"
            >
              Cancelar
            </button>

            <button
              type="button"
              class="btn btn-danger rounded-pill"
              :disabled="usuariosStore.loading"
              @click="confirmarEliminarUsuario"
            >
              <span
                v-if="usuariosStore.loading"
                class="spinner-border spinner-border-sm me-2"
              ></span>
              Eliminar
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { Modal } from 'bootstrap'
import { useUsuariosStore } from '@/stores/usuariosStore'
import api from '@/api/axios'

const usuariosStore = useUsuariosStore()

const busqueda = ref('')
const filtroRol = ref('')
const filtroEstado = ref('')

const roles = ref([])
const mostrarFormularioRol = ref(false)
const nuevoRol = reactive({ nombreRol: '', descripcion: '' })
const errorNuevoRol = ref('')

const mostrarFormularioRolModal = ref(false)
const nuevoRolModal = reactive({ nombreRol: '', descripcion: '' })
const errorNuevoRolModal = ref('')

const cargarRoles = async () => {
  const { data } = await api.get('/usuarios/roles')
  roles.value = data
}

const seleccionarOpcionRol = (event) => {
  if (event.target.value === '__nuevo__') {
    mostrarFormularioRol.value = true
    filtroRol.value = ''
  } else {
    filtroRol.value = event.target.value
    mostrarFormularioRol.value = false
  }
}

const guardarNuevoRol = async () => {
  errorNuevoRol.value = ''
  try {
    const { data } = await api.post('/usuarios/roles', {
      nombreRol: nuevoRol.nombreRol,
      descripcion: nuevoRol.descripcion,
    })
    await cargarRoles()
    filtroRol.value = String(data.idRol)
    mostrarFormularioRol.value = false
    nuevoRol.nombreRol = ''
    nuevoRol.descripcion = ''
  } catch {
    errorNuevoRol.value = 'No se pudo guardar el rol.'
  }
}

const cancelarNuevoRol = () => {
  mostrarFormularioRol.value = false
  nuevoRol.nombreRol = ''
  nuevoRol.descripcion = ''
  errorNuevoRol.value = ''
}

const seleccionarOpcionRolModal = (event) => {
  if (event.target.value === '__nuevo_modal__') {
    mostrarFormularioRolModal.value = true
    form.id_rol = null
  } else {
    form.id_rol = event.target.value === '' ? null : Number(event.target.value)
    mostrarFormularioRolModal.value = false
  }
}

const guardarNuevoRolModal = async () => {
  errorNuevoRolModal.value = ''
  try {
    const { data } = await api.post('/usuarios/roles', {
      nombreRol: nuevoRolModal.nombreRol,
      descripcion: nuevoRolModal.descripcion,
    })
    await cargarRoles()
    form.id_rol = data.idRol
    mostrarFormularioRolModal.value = false
    nuevoRolModal.nombreRol = ''
    nuevoRolModal.descripcion = ''
  } catch {
    errorNuevoRolModal.value = 'No se pudo guardar el rol.'
  }
}

const cancelarNuevoRolModal = () => {
  mostrarFormularioRolModal.value = false
  nuevoRolModal.nombreRol = ''
  nuevoRolModal.descripcion = ''
  errorNuevoRolModal.value = ''
}
const mensajeExito = ref('')
const mensajeError = ref('')
const usuarioSeleccionado = ref(null)
const modoEdicion = ref(false)
const usuarioEditandoId = ref(null)

const form = reactive({
  id_establecimiento: Number(localStorage.getItem('idEstablecimiento')) || 1,
  id_rol: 1,
  username: '',
  password: '',
  correo_electronico: '',
  estado: 'ACTIVO',
})

onMounted(async () => {
  await Promise.all([usuariosStore.cargarUsuarios(), cargarRoles()])
})

const usuariosFiltrados = computed(() => {
  const texto = busqueda.value.toLowerCase().trim()

  return usuariosStore.usuariosOrdenados.filter((usuario) => {
    const coincideBusqueda =
      !texto ||
      usuario.username?.toLowerCase().includes(texto) ||
      usuario.correo_electronico?.toLowerCase().includes(texto)

    const coincideRol =
      !filtroRol.value || String(usuario.id_rol) === String(filtroRol.value)

    const coincideEstado =
      !filtroEstado.value || usuario.estado === filtroEstado.value

    return coincideBusqueda && coincideRol && coincideEstado
  })
})

const nombreRol = (idRol) => {
  const rol = roles.value.find((r) => r.idRol === idRol)
  return rol ? rol.nombreRol : `ROL ${idRol}`
}

const limpiarFiltros = () => {
  busqueda.value = ''
  filtroRol.value = ''
  filtroEstado.value = ''
}

const resetForm = () => {
  form.id_establecimiento = Number(localStorage.getItem('idEstablecimiento')) || 1
  form.id_rol = 1
  form.username = ''
  form.password = ''
  form.correo_electronico = ''
  form.estado = 'ACTIVO'
}

const abrirModalCrear = () => {
  mensajeExito.value = ''
  mensajeError.value = ''
  modoEdicion.value = false
  usuarioEditandoId.value = null
  resetForm()
}

const abrirModalEditar = (usuario) => {
  mensajeExito.value = ''
  mensajeError.value = ''
  modoEdicion.value = true
  usuarioEditandoId.value = usuario.id_usuario

  form.id_establecimiento = usuario.id_establecimiento
  form.id_rol = usuario.id_rol
  form.username = usuario.username
  form.password = ''
  form.correo_electronico = usuario.correo_electronico
  form.estado = usuario.estado
}

const guardarUsuario = async () => {
  try {
    mensajeExito.value = ''
    mensajeError.value = ''

    if (modoEdicion.value) {
      await usuariosStore.actualizarUsuario(usuarioEditandoId.value, { ...form })
      mensajeExito.value = 'Usuario actualizado correctamente.'
    } else {
      await usuariosStore.crearUsuario({ ...form })
      mensajeExito.value = 'Usuario creado correctamente.'
    }

    cerrarModal('crearUsuarioModal')
    resetForm()
    modoEdicion.value = false
    usuarioEditandoId.value = null
  } catch (error) {
    mensajeError.value = obtenerMensajeError(
      error,
      modoEdicion.value
        ? 'No se pudo actualizar el usuario.'
        : 'No se pudo crear el usuario.',
    )
  }
}

const abrirModalEliminar = (usuario) => {
  mensajeExito.value = ''
  mensajeError.value = ''
  usuarioSeleccionado.value = usuario
}

const confirmarEliminarUsuario = async () => {
  if (!usuarioSeleccionado.value) return

  try {
    mensajeExito.value = ''
    mensajeError.value = ''

    await usuariosStore.eliminarUsuario(usuarioSeleccionado.value.id_usuario)

    mensajeExito.value = 'Usuario eliminado correctamente.'
    cerrarModal('eliminarUsuarioModal')
    usuarioSeleccionado.value = null
  } catch (error) {
    mensajeError.value = obtenerMensajeError(
      error,
      'No se pudo eliminar el usuario. Puede estar asociado a otro registro.',
    )
  }
}

const toggleEstadoUsuario = async (usuario) => {
  try {
    mensajeExito.value = ''
    mensajeError.value = ''

    const nuevoEstado = usuario.estado === 'ACTIVO' ? 'INACTIVO' : 'ACTIVO'

    await usuariosStore.cambiarEstadoUsuario(usuario.id_usuario, nuevoEstado)

    mensajeExito.value =
      nuevoEstado === 'ACTIVO'
        ? 'Usuario activado correctamente.'
        : 'Usuario desactivado correctamente.'
  } catch (error) {
    mensajeError.value = obtenerMensajeError(
      error,
      'No se pudo cambiar el estado del usuario.',
    )
  }
}

const cerrarModal = (idModal) => {
  const modalElement = document.getElementById(idModal)
  const modalInstance = Modal.getInstance(modalElement)

  if (modalInstance) {
    modalInstance.hide()
  }
}

const obtenerMensajeError = (error, fallback) => {
  const status = error?.response?.status
  const data = error?.response?.data

  if (status === 409) {
    return 'No se puede eliminar este usuario porque está asociado a otros registros.'
  }

  if (status === 403) {
    return 'No tienes permisos para realizar esta acción.'
  }

  if (status === 401) {
    return 'Tu sesión expiró. Vuelve a iniciar sesión.'
  }

  if (typeof data === 'string' && data.trim()) {
    return data
  }

  if (data?.message) {
    return data.message
  }

  if (data?.error) {
    return data.error
  }

  return fallback
}
</script>