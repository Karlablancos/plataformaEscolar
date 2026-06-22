import { useAuthStore } from '../authStore'

export const getEstablecimientoId = () => {
  const auth = useAuthStore()

  const user = auth.user
  const establecimiento = user?.establecimiento

  const fromUser =
    user?.establecimientoId ??
    user?.id_establecimiento ??
    establecimiento?.id_establecimiento ??
    establecimiento?.establecimientoId ??
    establecimiento?.id ??
    null

  if (fromUser != null) return Number(fromUser)

  try {
    const activo = JSON.parse(localStorage.getItem('establecimientoActivo') || 'null')
    return activo?.idEstablecimiento != null ? Number(activo.idEstablecimiento) : null
  } catch {
    return null
  }
}

export const getCursoId = (curso) => curso.id_curso ?? curso.id

export const getCursoEstablecimientoId = (curso) => {
  return curso.id_establecimiento ?? curso.establecimientoId
}

export const getCursoAnio = (curso) => {
  return curso.anio_academico ?? curso.anio
}

export const getCursoNombre = (curso) => {
  if (!curso) return ''

  if (curso.nombre) return curso.nombre

  const numero = curso.numero ? `${curso.numero}°` : ''
  const tipo = curso.tipo_ensenanza || ''
  const letra = curso.letra || ''

  return `${numero} ${tipo} ${letra}`.replace(/\s+/g, ' ').trim()
}

export const getDocenteId = (docente) => {
  return docente.id_docente ?? docente.id
}

export const getAsignaturaId = (asignatura) => {
  return asignatura.id_asignatura ?? asignatura.id
}

export const getAsignaturaEstablecimientoId = (asignatura) => {
  return asignatura.id_establecimiento ?? asignatura.establecimientoId
}

export const getNombreCompleto = (persona) => {
  if (!persona) return ''

  if (persona.nombre) return persona.nombre

  return [
    persona.nombres,
    persona.apellido_paterno || persona.apellidoPaterno,
    persona.apellido_materno || persona.apellidoMaterno,
  ]
    .filter(Boolean)
    .join(' ')
    .replace(/\s+/g, ' ')
    .trim()
}
