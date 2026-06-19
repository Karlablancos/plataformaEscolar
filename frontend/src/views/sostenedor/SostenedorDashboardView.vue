<template>
  <div class="sostenedor-layout">

    <!-- SIDEBAR -->
    <aside class="sidebar">
      <div class="sidebar-logo">
        <div class="logo-circle">S</div>
        <div class="logo-text">
          <span class="logo-title">GestEscolar</span>
          <span class="logo-badge">Sostenedor</span>
        </div>
      </div>

      <nav class="sidebar-nav">
        <a href="#" class="nav-item active">
          <i class="bi bi-grid-1x2-fill"></i>
          <span>Panel central</span>
        </a>
        <a href="#" class="nav-item">
          <i class="bi bi-building"></i>
          <span>Establecimientos</span>
        </a>
        <a href="#" class="nav-item">
          <i class="bi bi-people-fill"></i>
          <span>Administradores</span>
        </a>
        <a href="#" class="nav-item">
          <i class="bi bi-bar-chart-fill"></i>
          <span>Reportes globales</span>
        </a>
        <a href="#" class="nav-item">
          <i class="bi bi-journal-bookmark-fill"></i>
          <span>Directorio MINEDUC</span>
        </a>

        <div class="nav-divider"></div>

        <a href="#" class="nav-item">
          <i class="bi bi-shield-lock-fill"></i>
          <span>Roles y permisos</span>
        </a>
        <a href="#" class="nav-item">
          <i class="bi bi-gear-fill"></i>
          <span>Configuración</span>
        </a>
      </nav>

      <div class="sidebar-footer">
        <div class="user-info">
          <div class="user-avatar">{{ iniciales }}</div>
          <div class="user-data">
            <span class="user-name">{{ authStore.displayName }}</span>
            <span class="user-role">Sostenedor</span>
          </div>
        </div>
        <button class="btn-logout" @click="cerrarSesion" title="Cerrar sesión">
          <i class="bi bi-box-arrow-right"></i>
        </button>
      </div>
    </aside>

    <!-- CONTENIDO PRINCIPAL -->
    <div class="main-content">

      <!-- TOPBAR -->
      <header class="topbar">
        <div class="topbar-left">
          <h1 class="topbar-title">Panel del Sostenedor</h1>
          <p class="topbar-sub">Visión general de tu establecimiento</p>
        </div>
      </header>

      <!-- MÉTRICAS -->
      <section class="metrics-row">
        <div class="metric-card">
          <div class="metric-icon" style="background:#EBF0FB; color:#1B4F9C">
            <i class="bi bi-building fs-4"></i>
          </div>
          <div class="metric-data">
            <span class="metric-value">
              <span v-if="cargandoTotal" class="spinner-border spinner-border-sm text-secondary" />
              <span v-else>{{ totalEstablecimientos }}</span>
            </span>
            <span class="metric-label">Establecimientos</span>
          </div>
        </div>

        <div class="mineduc-banner">
          <i class="bi bi-info-circle me-2" style="color:#1B4F9C"></i>
          <span>16.768 RBDs disponibles en el directorio MINEDUC 2025</span>
        </div>
      </section>

      <!-- CARD ESTABLECIMIENTO ACTIVO -->
      <section class="section-block">
        <div class="section-header">
          <h2 class="section-title">Mi establecimiento</h2>
        </div>

        <div v-if="cargandoEstab" class="text-center py-5 text-muted">
          <div class="spinner-border text-primary mb-3" />
          <p>Cargando datos del establecimiento...</p>
        </div>

        <div v-else-if="errorEstab" class="alert alert-warning">
          <i class="bi bi-exclamation-triangle me-2"></i>
          {{ errorEstab }}
        </div>

        <div v-else-if="establecimiento" class="estab-card-destacada">
          <div class="estab-card-top">
            <div class="estab-avatar-lg">
              {{ establecimiento.nombre?.charAt(0) ?? 'E' }}
            </div>
            <div class="estab-header-info">
              <h3 class="estab-nombre">{{ establecimiento.nombre }}</h3>
              <span class="estab-rbd">RBD {{ establecimiento.rbd }}</span>
            </div>
            <span
              class="estab-status"
              :class="establecimiento.estado === 'ACTIVO' ? 'active' : 'inactive'"
            >
              {{ establecimiento.estado ?? '—' }}
            </span>
          </div>

          <div class="estab-details">
            <div class="detail-item">
              <i class="bi bi-geo-alt" style="color:#1B4F9C"></i>
              <span>{{ establecimiento.ciudad || establecimiento.comuna || '—' }}</span>
            </div>
            <div class="detail-item">
              <i class="bi bi-hash" style="color:#1B4F9C"></i>
              <span>ID {{ establecimiento.idEstablecimiento }}</span>
            </div>
          </div>
        </div>
      </section>

    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import api from '@/api/axios'

const router = useRouter()
const authStore = useAuthStore()

const cargandoTotal = ref(false)
const totalEstablecimientos = ref(0)

const cargandoEstab = ref(false)
const establecimiento = ref(null)
const errorEstab = ref('')

const iniciales = computed(() => {
  const name = authStore.displayName ?? ''
  return name.split(' ').map((w) => w[0]).join('').toUpperCase().slice(0, 2)
})

onMounted(async () => {
  // Contar total de establecimientos
  cargandoTotal.value = true
  try {
    const idEstab = authStore.establecimientoId
    const { data } = await api.get('/establecimiento', {
      params: idEstab ? { idEstablecimiento: idEstab } : {},
    })
    totalEstablecimientos.value = Array.isArray(data) ? data.length : 0
  } catch {
    totalEstablecimientos.value = 0
  } finally {
    cargandoTotal.value = false
  }

  // Cargar establecimiento activo
  const idEstab = authStore.establecimientoId
  if (!idEstab) {
    errorEstab.value = 'No se encontró el establecimiento asociado a tu cuenta.'
    return
  }

  cargandoEstab.value = true
  try {
    const { data } = await api.get(`/isEstablecimiento/${idEstab}`)
    establecimiento.value = data
  } catch (err) {
    const status = err?.response?.status
    if (status === 404) {
      errorEstab.value = 'Establecimiento no encontrado.'
    } else {
      errorEstab.value = 'No se pudo cargar la información del establecimiento.'
    }
  } finally {
    cargandoEstab.value = false
  }
})

const cerrarSesion = () => {
  authStore.logout()
  router.push('/')
}
</script>

<style scoped>
/* ── Layout ── */
.sostenedor-layout {
  display: flex;
  min-height: 100vh;
  background: #F0F4FA;
  font-family: 'Segoe UI', sans-serif;
}

/* ── Sidebar ── */
.sidebar {
  width: 260px;
  min-height: 100vh;
  background: #1B4F9C;
  display: flex;
  flex-direction: column;
  position: fixed;
  top: 0; left: 0; bottom: 0;
  z-index: 100;
}

.sidebar-logo {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 24px 20px;
  border-bottom: 1px solid rgba(255,255,255,0.12);
}

.logo-circle {
  width: 42px; height: 42px;
  background: #F5C518;
  border-radius: 10px;
  display: flex; align-items: center; justify-content: center;
  font-size: 1.3rem; font-weight: 800;
  color: #1B4F9C;
  flex-shrink: 0;
}

.logo-text {
  display: flex;
  flex-direction: column;
}

.logo-title {
  color: #fff;
  font-weight: 700;
  font-size: 1rem;
  line-height: 1.2;
}

.logo-badge {
  background: #CC1F2D;
  color: #fff;
  font-size: 0.65rem;
  font-weight: 700;
  padding: 2px 8px;
  border-radius: 20px;
  letter-spacing: 0.04em;
  width: fit-content;
  margin-top: 3px;
}

.sidebar-nav {
  flex: 1;
  padding: 16px 12px;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 14px;
  border-radius: 8px;
  color: rgba(255,255,255,0.72);
  text-decoration: none;
  font-size: 0.88rem;
  font-weight: 500;
  transition: all 0.15s;
}

.nav-item:hover {
  background: rgba(255,255,255,0.1);
  color: #fff;
}

.nav-item.active {
  background: rgba(255,255,255,0.18);
  color: #fff;
  font-weight: 600;
}

.nav-item i {
  font-size: 1rem;
  width: 18px;
  text-align: center;
}

.nav-divider {
  height: 1px;
  background: rgba(255,255,255,0.12);
  margin: 8px 0;
}

.sidebar-footer {
  padding: 16px 20px;
  border-top: 1px solid rgba(255,255,255,0.12);
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-avatar {
  width: 36px; height: 36px;
  background: #F5C518;
  color: #1B4F9C;
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  font-weight: 700;
  font-size: 0.8rem;
  flex-shrink: 0;
}

.user-data {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.user-name {
  color: #fff;
  font-size: 0.82rem;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-role {
  color: rgba(255,255,255,0.55);
  font-size: 0.72rem;
}

.btn-logout {
  background: none;
  border: none;
  color: rgba(255,255,255,0.55);
  cursor: pointer;
  padding: 4px;
  font-size: 1.1rem;
  transition: color 0.15s;
}
.btn-logout:hover { color: #fff; }

/* ── Main ── */
.main-content {
  margin-left: 260px;
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* ── Topbar ── */
.topbar {
  background: #fff;
  padding: 20px 32px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #e2e8f0;
  box-shadow: 0 1px 4px rgba(0,0,0,0.06);
}

.topbar-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0;
}

.topbar-sub {
  font-size: 0.82rem;
  color: #718096;
  margin: 2px 0 0;
}

/* ── Métricas ── */
.metrics-row {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 28px 32px 0;
  flex-wrap: wrap;
}

.metric-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.06);
  min-width: 200px;
}

.metric-icon {
  width: 52px; height: 52px;
  border-radius: 12px;
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0;
}

.metric-value {
  font-size: 1.75rem;
  font-weight: 700;
  color: #1a202c;
  line-height: 1;
}

.metric-label {
  font-size: 0.78rem;
  color: #718096;
  margin-top: 4px;
  display: block;
}

.mineduc-banner {
  background: #EBF0FB;
  border-left: 4px solid #1B4F9C;
  border-radius: 8px;
  padding: 14px 20px;
  font-size: 0.87rem;
  color: #1a202c;
  font-weight: 500;
  display: flex;
  align-items: center;
}

/* ── Sección ── */
.section-block {
  margin: 24px 32px;
  background: #fff;
  border-radius: 14px;
  padding: 24px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.06);
}

.section-header {
  margin-bottom: 20px;
}

.section-title {
  font-size: 1rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0;
}

/* ── Card destacada establecimiento ── */
.estab-card-destacada {
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 24px;
  border-top: 4px solid #1B4F9C;
}

.estab-card-top {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  margin-bottom: 20px;
}

.estab-avatar-lg {
  width: 56px; height: 56px;
  background: #EBF0FB;
  color: #1B4F9C;
  border-radius: 12px;
  display: flex; align-items: center; justify-content: center;
  font-weight: 700;
  font-size: 1.4rem;
  flex-shrink: 0;
}

.estab-header-info {
  flex: 1;
  min-width: 0;
}

.estab-nombre {
  font-size: 1.05rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 4px;
}

.estab-rbd {
  font-size: 0.8rem;
  color: #718096;
  font-weight: 500;
}

.estab-status {
  font-size: 0.72rem;
  font-weight: 700;
  padding: 4px 12px;
  border-radius: 20px;
  flex-shrink: 0;
  letter-spacing: 0.04em;
}
.estab-status.active  { background: #F0FFF4; color: #276749; }
.estab-status.inactive { background: #FFF5F5; color: #C53030; }

.estab-details {
  display: flex;
  gap: 28px;
  padding-top: 16px;
  border-top: 1px solid #f0f4fa;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.85rem;
  color: #4a5568;
}

/* ── Responsive ── */
@media (max-width: 768px) {
  .sidebar { width: 60px; }
  .sidebar .logo-text, .sidebar .nav-item span,
  .sidebar .user-data { display: none; }
  .main-content { margin-left: 60px; }
  .metrics-row { flex-direction: column; align-items: stretch; }
}
</style>
