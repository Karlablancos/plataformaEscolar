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
