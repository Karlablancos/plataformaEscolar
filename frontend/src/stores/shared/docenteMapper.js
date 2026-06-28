export const mapDocenteFromApi = (d) => ({
  id: d.idDocente,
  id_docente: d.idDocente,
  id_establecimiento: Number(d.idEstablecimiento),
  establecimientoId: Number(d.idEstablecimiento),
  nombres: d.nombres?.trim() ?? '',
  apellido_paterno: d.apellidoPaterno?.trim() ?? '',
  apellido_materno: d.apellidoMaterno?.trim() ?? '',
  nombreCompleto: d.nombreCompleto?.trim() ?? '',
  rut: d.rut?.trim() ?? '',
  dv: d.dv?.trim() ?? '',
  correo_electronico: d.correoElectronico ?? '',
  correoElectronico: d.correoElectronico ?? '',
  telefono: d.telefono ?? '',
  estado: d.estado
    ? d.estado.charAt(0).toUpperCase() + d.estado.slice(1).toLowerCase()
    : 'Activo',
})

export const mapDocenteToApi = (data) => ({
  rut: data.rut,
  dv: data.dv || null,
  nombres: data.nombres,
  apellidoPaterno: data.apellido_paterno ?? data.apellidoPaterno,
  apellidoMaterno: data.apellido_materno ?? data.apellidoMaterno ?? '',
  fechaNacimiento: data.fecha_nacimiento || data.fechaNacimiento || null,
  correoElectronico: data.correo_electronico || data.correoElectronico || null,
  telefono: data.telefono || null,
  calle: data.calle || null,
  numero: data.numero || null,
  idComuna: data.id_comuna ?? data.idComuna ?? null,
  idCategoriaSned: data.id_categoria_sned ?? data.idCategoriaSned ?? null,
  anioEvaluacionSned: data.anio_evaluacion_sned ?? data.anioEvaluacionSned ?? new Date().getFullYear(),
  fechaContratacion: data.fecha_contratacion || data.fechaContratacion || null,
  estado: data.estado || 'Activo',
})
