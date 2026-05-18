import { defineStore } from 'pinia'
import api from '@/api/axios'

const mapUsuario = (u) => ({
  id_usuario: u.idUsuario,
  id_establecimiento: u.idEstablecimiento,
  id_rol: u.idRol,
  username: u.username,
  correo_electronico: u.correoElectronico,
  ultimo_acceso: u.ultimoAcceso,
  bloqueado: u.bloqueado,
  estado: u.estado,
})

const toCreatePayload = (u) => ({
  idEstablecimiento: Number(u.id_establecimiento),
  idRol: Number(u.id_rol),
  username: u.username,
  password: u.password,
  correoElectronico: u.correo_electronico,
  estado: u.estado || 'ACTIVO',
})

export const useUsuariosStore = defineStore('usuarios', {
  state: () => ({
    usuarios: [],
    loading: false,
    error: null,
  }),

  getters: {
    usuariosOrdenados: (state) => {
      return [...state.usuarios].sort((a, b) => a.id_usuario - b.id_usuario)
    },
  },

  actions: {
    async cargarUsuarios() {
      try {
        this.loading = true
        this.error = null

        const { data } = await api.get('/usuarios')
        this.usuarios = data.map(mapUsuario)
      } catch (error) {
        this.error = error
        console.error('Error cargando usuarios:', error)
      } finally {
        this.loading = false
      }
    },
    async actualizarUsuario(idUsuario, usuario) {
    try {
        this.loading = true
        this.error = null

        const payload = {
        username: usuario.username,
        correoElectronico: usuario.correo_electronico,
        idRol: Number(usuario.id_rol),
        estado: usuario.estado,
        }

        const { data } = await api.put(`/usuarios/${idUsuario}`, payload)

        const usuarioActualizado = mapUsuario(data)

        const index = this.usuarios.findIndex(
        (u) => u.id_usuario === idUsuario,
        )

        if (index !== -1) {
        this.usuarios[index] = usuarioActualizado
        }

        return usuarioActualizado
    } catch (error) {
        this.error = error
        console.error('Error actualizando usuario:', error)
        throw error
    } finally {
        this.loading = false
    }
    },
    async cambiarEstadoUsuario(idUsuario, estado) {
    try {
        this.loading = true
        this.error = null

        const { data } = await api.patch(`/usuarios/${idUsuario}/estado`, {
        estado,
        })

        const usuarioActualizado = mapUsuario(data)

        const index = this.usuarios.findIndex(
        (usuario) => usuario.id_usuario === idUsuario,
        )

        if (index !== -1) {
        this.usuarios[index] = usuarioActualizado
        }

        return usuarioActualizado
    } catch (error) {
        this.error = error
        console.error('Error cambiando estado usuario:', error)
        throw error
    } finally {
        this.loading = false
    }
    },

    async crearUsuario(usuario) {
      try {
        this.loading = true
        this.error = null

        const payload = toCreatePayload(usuario)
        const { data } = await api.post('/usuarios', payload)
        const usuarioCreado = mapUsuario(data)

        this.usuarios.push(usuarioCreado)

        return usuarioCreado
      } catch (error) {
        this.error = error
        console.error('Error creando usuario:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async eliminarUsuario(idUsuario) {
      try {
        this.loading = true
        this.error = null

        await api.delete(`/usuarios/${idUsuario}`)

        this.usuarios = this.usuarios.filter(
          (usuario) => usuario.id_usuario !== idUsuario,
        )
      } catch (error) {
        this.error = error
        console.error('Error eliminando usuario:', error)
        throw error
      } finally {
        this.loading = false
      }
    },
    
  },
})