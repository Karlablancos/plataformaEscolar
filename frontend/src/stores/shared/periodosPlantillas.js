export const plantillasPorModoAula = (modo) => {
  const normalizado = String(modo || 'NORMAL').trim().toUpperCase()

  if (normalizado === 'TRIMESTRAL') {
    return [
      { nombre: 'Primer Trimestre' },
      { nombre: 'Segundo Trimestre' },
      { nombre: 'Tercer Trimestre' },
    ]
  }

  if (normalizado === 'ANUAL' || normalizado === 'NORMAL') {
    return [{ nombre: 'Año Académico' }]
  }

  return [{ nombre: 'Primer Semestre' }, { nombre: 'Segundo Semestre' }]
}

export const normalizarNombrePeriodo = (nombre) =>
  String(nombre || '')
    .toLowerCase()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .trim()

export const filtrarPeriodosPorModoAula = (periodos, modo) => {
  const nombresPermitidos = new Set(
    plantillasPorModoAula(modo).map((plantilla) => normalizarNombrePeriodo(plantilla.nombre)),
  )

  return periodos.filter((periodo) =>
    nombresPermitidos.has(normalizarNombrePeriodo(periodo.nombre)),
  )
}

export const calcularEstadoPeriodo = (fechaInicio, fechaTermino) => {
  if (!fechaInicio || !fechaTermino) return 'PENDIENTE'

  const hoy = new Date().toISOString().slice(0, 10)
  if (hoy < fechaInicio) return 'PENDIENTE'
  if (hoy > fechaTermino) return 'CERRADO'
  return 'ACTIVO'
}

export const fechasDefectoPeriodo = (anio, modo, index) => {
  const year = Number(anio)
  const normalizado = String(modo || 'NORMAL').trim().toUpperCase()

  if (normalizado === 'TRIMESTRAL') {
    if (index === 0) return { fecha_inicio: `${year}-03-01`, fecha_termino: `${year}-05-31` }
    if (index === 1) return { fecha_inicio: `${year}-06-01`, fecha_termino: `${year}-08-31` }
    return { fecha_inicio: `${year}-09-01`, fecha_termino: `${year}-12-20` }
  }

  if (normalizado === 'ANUAL' || normalizado === 'NORMAL') {
    return { fecha_inicio: `${year}-03-01`, fecha_termino: `${year}-12-20` }
  }

  if (index === 0) {
    return { fecha_inicio: `${year}-03-01`, fecha_termino: `${year}-07-15` }
  }

  return { fecha_inicio: `${year}-08-01`, fecha_termino: `${year}-12-20` }
}
