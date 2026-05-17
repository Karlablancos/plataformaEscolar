// src/stores/authStore.js

import { defineStore } from 'pinia'
import axios from 'axios'

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
    async login(rbd, username, password) {
      try {
        const rbdLimpio = (rbd || '').split('-')[0]

        const response = await axios.post('http://localhost:8080/auth/login', {
          rbd: rbdLimpio,
          username,
          password,
        })

        const data = response.data

        this.token = data.token
        this.user = {
          username: data.username,
          rol: data.rol,
          establecimientoNombre: data.establecimientoNombre,
          rbd: data.rbd,
        }

        localStorage.setItem('token', this.token)
        localStorage.setItem('user', JSON.stringify(this.user))

        return true
      } catch (error) {
        return false
      }
    },

    logout() {
      this.token = null
      this.user = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    },
  },
})
