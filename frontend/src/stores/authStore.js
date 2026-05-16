// src/stores/authStore.js

import { defineStore } from 'pinia'
import { usuariosMock } from '../data/mockData'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token'),
    user: JSON.parse(localStorage.getItem('user') || 'null'),
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,

    isAdmin: (state) => {
      return ['ADMIN', 'UTP', 'DIRECTOR'].includes(state.user?.rol)
    },

    isProfesor: (state) => {
      return state.user?.rol === 'PROFESOR'
    },

    establecimientoId: (state) => {
      return state.user?.establecimientoId || null
    },
  },

  actions: {
    login(email, password) {
      const usuario = usuariosMock.find((u) => u.email === email && u.password === password)

      if (!usuario) {
        return false
      }

      this.token = 'fake-jwt-token'

      this.user = {
        id: usuario.id,
        establecimientoId: usuario.establecimientoId,
        nombre: usuario.nombre,
        email: usuario.email,
        rol: usuario.rol,
        docenteId: usuario.docenteId || null,
        estado: usuario.estado,
      }

      localStorage.setItem('token', this.token)
      localStorage.setItem('user', JSON.stringify(this.user))

      return true
    },

    logout() {
      this.token = null
      this.user = null

      localStorage.removeItem('token')
      localStorage.removeItem('user')
    },
  },
})
