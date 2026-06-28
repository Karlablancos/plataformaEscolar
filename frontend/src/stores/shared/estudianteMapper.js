const formatearRut = (rut, dv) => {
  const numero = String(rut ?? '').replace(/\D/g, '')
  const dvLimpio = String(dv ?? '').trim().toUpperCase()
  if (!numero) return dvLimpio ? `-${dvLimpio}` : ''
  const conPuntos = numero.replace(/\B(?=(\d{3})+(?!\d))/g, '.')
  return dvLimpio ? `${conPuntos}-${dvLimpio}` : conPuntos
}

export const mapEstudianteFromApi = (e) => ({
  id: e.idEstudiante,
  id_alumno: e.idEstudiante,
  id_establecimiento: Number(e.idEstablecimiento),
  establecimientoId: Number(e.idEstablecimiento),
  nombres: e.nombres?.trim() ?? '',
  apellido_paterno: e.apellidoPaterno?.trim() ?? '',
  apellidoPaterno: e.apellidoPaterno?.trim() ?? '',
  apellido_materno: e.apellidoMaterno?.trim() ?? '',
  apellidoMaterno: e.apellidoMaterno?.trim() ?? '',
  nombreCompleto: e.nombreCompleto?.trim() ?? '',
  rut: e.dv ? `${e.rut}-${e.dv}` : e.rut?.trim() ?? '',
  dv: e.dv?.trim() ?? '',
  rutFormateado: formatearRut(e.rut, e.dv),
  correo_electronico: e.correoElectronico ?? '',
  correoElectronico: e.correoElectronico ?? '',
  telefono: e.telefono ?? '',
  calle: e.calle ?? '',
  numero: e.numero ?? '',
  id_comuna: e.idComuna ?? null,
  idComuna: e.idComuna ?? null,
  colegio_procedente: e.colegioProcedente ?? '',
  colegioProcedente: e.colegioProcedente ?? '',
  fecha_nacimiento: e.fechaNacimiento ?? null,
  fechaNacimiento: e.fechaNacimiento ?? null,
  fecha_matricula: e.fechaMatricula ?? null,
  fechaMatricula: e.fechaMatricula ?? null,
  estado: e.estado
    ? e.estado.charAt(0).toUpperCase() + e.estado.slice(1).toLowerCase()
    : 'Activo',
  prioritario: e.prioritario ?? false,
  preferente: e.preferente ?? false,
  tieneNee: e.tieneNee ?? false,
  idTipoNee: e.idTipoNee ?? null,
  enPie: e.enPie ?? false,
  cursoId: e.idCurso ?? null,
  id_curso: e.idCurso ?? null,
  asistencia: 0,
  promedio: 0,
})

export const mapEstudianteToApi = (data) => ({
  rut: data.rut ?? '',
  dv: data.dv ?? '',
  nombres: data.nombres ?? '',
  apellidoPaterno: data.apellidoPaterno ?? data.apellido_paterno ?? '',
  apellidoMaterno: data.apellidoMaterno ?? data.apellido_materno ?? '',
  fechaNacimiento: data.fechaNacimiento ?? data.fecha_nacimiento ?? null,
  correoElectronico: data.correoElectronico ?? data.correo_electronico ?? null,
  telefono: data.telefono ?? null,
  calle: data.calle ?? null,
  numero: data.numero ?? null,
  idComuna: data.comunaId ?? data.idComuna ?? data.id_comuna ?? null,
  colegioProcedente: data.colegioProcedente ?? data.colegio_procedente ?? null,
  fechaMatricula: data.fechaMatricula ?? data.fecha_matricula ?? null,
  prioritario: Boolean(data.prioritario),
  preferente: Boolean(data.preferente),
  tieneNee: Boolean(data.tieneNee),
  idTipoNee: data.tieneNee ? (data.tipoNeeId ?? data.idTipoNee ?? null) : null,
  enPie: Boolean(data.enPie),
  estado: data.estado ?? 'Activo',
  idCurso: data.cursoId ? Number(data.cursoId) : null,
})
